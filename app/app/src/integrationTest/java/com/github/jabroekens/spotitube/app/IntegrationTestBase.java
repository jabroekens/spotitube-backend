package com.github.jabroekens.spotitube.app;

import java.io.File;
import java.net.http.HttpResponse;
import org.testcontainers.containers.DockerComposeContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

abstract class IntegrationTestBase {

    private static final DockerComposeContainer<?> SPOTITUBE_CONTAINER = new DockerComposeContainer<>(
      new File("../../docker/docker-compose.yaml")
    )
      .withExposedService("backend", 8080)
      .withExposedService("db", 5432)
      .withLocalCompose(true)
      .withOptions("--compatibility");

    protected static final HttpClient httpClient;

    static {
        SPOTITUBE_CONTAINER.start();
        httpClient = new HttpClient(
          SPOTITUBE_CONTAINER.getServiceHost("backend", 8080),
          SPOTITUBE_CONTAINER.getServicePort("backend", 8080)
        );
    }

    protected static <T> void assertResponse(int statusCode, String body, HttpResponse<T> response) {
        assertAll(
          () -> assertEquals(statusCode, response.statusCode()),
          () -> assertEquals(body, response.body())
        );
    }

}
