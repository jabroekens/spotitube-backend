package com.github.jabroekens.spotitube.app;

import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TrackResourceIT extends IntegrationTestBase {

	private static String AUTH_TOKEN;

	@BeforeAll
	static void setUpAll() throws IOException, InterruptedException {
		AUTH_TOKEN = authenticate();
	}

	@Test
	void listsAllTracks() throws IOException, InterruptedException {
		var expectedResponse = """
          {
            "tracks": [
              {
                "id": 1,
                "title": "American Love",
                "duration": 179,
                "offlineAvailable": true,
                "performer": "Smallpools",
                "album": "Lovetap!"
              },
              {
                "id": 2,
                "title": "The Egg - A Short Story",
                "duration": 474,
                "offlineAvailable": false,
                "playCount": 28613533,
                "description": "The Egg. Story by Andy Weir, Animated by Kurzgesagt",
                "performer": "Kurzgesagt - In a Nutshell",
                "publicationDate": "09-01-2019"
              }
            ]
          }
          """;

		assertRequiresAuthentication(httpClient.get("/tracks"));
		assertResponse(200, expectedResponse, httpClient.get("/tracks?token=" + AUTH_TOKEN));
	}

	@Test
	void listsAllTracksNotInSpecifiedPlaylist() throws IOException, InterruptedException {
		var expectedResponse = """
          {
            "tracks": [
              {
                "id": 2,
                "title": "The Egg - A Short Story",
                "duration": 474,
                "offlineAvailable": false,
                "playCount": 28613533,
                "description": "The Egg. Story by Andy Weir, Animated by Kurzgesagt",
                "performer": "Kurzgesagt - In a Nutshell",
                "publicationDate": "09-01-2019"
              }
            ]
          }
          """;

		assertRequiresAuthentication(httpClient.get("/tracks?forPlaylist=1"));
		assertResponse(200, expectedResponse, httpClient.get("/tracks?forPlaylist=1&token=" + AUTH_TOKEN));
	}

}
