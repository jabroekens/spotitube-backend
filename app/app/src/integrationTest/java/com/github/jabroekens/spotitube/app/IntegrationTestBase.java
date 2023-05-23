package com.github.jabroekens.spotitube.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jabroekens.spotitube.app.resource.user.LoginResponse;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;
import org.testcontainers.containers.DockerComposeContainer;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    protected static String authenticate() throws IOException, InterruptedException {
        var response = httpClient.post("/login", "{\"user\":\"john\",\"password\":\"password\"}");
        return new ObjectMapper().readValue(response.body(), LoginResponse.class).token();
    }

    protected static void assertRequiresAuthentication(HttpResponse<String> tokenlessResponse) {
        assertEquals(401, tokenlessResponse.statusCode());
    }

    protected static void assertResponse(int statusCode, String body, HttpResponse<String> response) {
        assertAll(
          () -> assertEquals(statusCode, response.statusCode()),
          () -> assertEquals(body, response.body())
        );
    }

    protected static void assertResponse(int statusCode, Pattern bodyPattern, HttpResponse<String> response) {
        assertAll(
          () -> assertEquals(statusCode, response.statusCode()),
          () -> assertTrue(bodyPattern.matcher(response.body()).matches())
        );
    }

    protected static String minifyJson(String json) throws JsonProcessingException {
        return new ObjectMapper().readTree(json).toString();
    }

}
