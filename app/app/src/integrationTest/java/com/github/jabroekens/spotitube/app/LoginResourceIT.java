package com.github.jabroekens.spotitube.app;

import java.io.IOException;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class LoginResourceIT extends IntegrationTestBase {

    @Test
    void canLoginSuccesfullyWithCorrectCredentials() throws IOException, InterruptedException {
        var response = httpClient.post("/login", "{\"user\":\"john\",\"password\":\"password\"}");
        assertResponse(200, Pattern.compile("\\{\"token\":\".+?\",\"user\":\"John Doe\"}"), response);
    }

    @Test
    void returnsErrorForIncorrectPassword() throws IOException, InterruptedException {
        var response = httpClient.post("/login", "{\"user\":\"john\",\"password\":\"incorrect\"}");
        assertResponse(401, "{\"error\":\"Incorrect password given for user with ID 'john'.\"}", response);
    }

    @Test
    void returnsErrorForUnknownUser() throws IOException, InterruptedException {
        var response = httpClient.post("/login", "{\"user\":\"unknown\",\"password\":\"password\"}");
        assertResponse(404, "{\"error\":\"Entity of type 'User' with details 'id=unknown' has not been found.\"}", response);
    }

}
