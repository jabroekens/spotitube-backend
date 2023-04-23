package com.github.jabroekens.spotitube.app;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClient {

    private static final java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();

    private final String baseURI;

    public HttpClient(String host, int port) {
        this.baseURI = "http://" + host + ":" + port;
    }

    private HttpRequest.Builder request(String uri) {
        return HttpRequest.newBuilder(URI.create(baseURI + uri));
    }

    public HttpResponse<String> get(String uri) throws IOException, InterruptedException {
        return httpClient.send(request(uri).GET().build(), HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> post(String uri, String jsonBody) throws IOException, InterruptedException {
        return httpClient.send(
          request(uri).header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build(),
          HttpResponse.BodyHandlers.ofString()
        );
    }

    public HttpResponse<String> delete(String uri) throws IOException, InterruptedException {
        return httpClient.send(request(uri).DELETE().build(), HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> put(String uri, String jsonBody) throws IOException, InterruptedException {
        return httpClient.send(
          request(uri)
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build(),
          HttpResponse.BodyHandlers.ofString()
        );
    }

}
