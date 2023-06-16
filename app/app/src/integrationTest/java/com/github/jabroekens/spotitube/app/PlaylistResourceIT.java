package com.github.jabroekens.spotitube.app;

import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlaylistResourceIT extends IntegrationTestBase {

	private static String AUTH_TOKEN;

	@BeforeAll
	static void setUpAll() throws IOException, InterruptedException {
		AUTH_TOKEN = authenticate();
	}

	@Test
	@Order(1)
	void listsAllPlaylists() throws IOException, InterruptedException {
		var expectedResponse = """
          {
            "length": 653,
            "playlists": [
              { "id": 1, "name": "Empty", "tracks": [], "owner": true },
              {
                "id": 2,
                "name": "Favorites",
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
                ],
                "owner": true
              }
            ]
          }
          """;

		assertRequiresAuthentication(httpClient.get("/playlists"));
		assertResponse(200, expectedResponse, httpClient.get("/playlists?token=" + AUTH_TOKEN));
	}

	@Test
	@Order(2)
	void deletesPlaylistAndListsAllPlaylists() throws IOException, InterruptedException {
		var expectedResponse = """
          {
            "length": 0,
            "playlists": [
              { "id": 1, "name": "Empty", "tracks": [], "owner": true }
            ]
          }
          """;

		assertRequiresAuthentication(httpClient.delete("/playlists/2"));
		assertResponse(200, expectedResponse, httpClient.delete("/playlists/2?token=" + AUTH_TOKEN));
		assertResponse(200, expectedResponse, httpClient.get("/playlists?token=" + AUTH_TOKEN));
	}

	@Test
	@Order(3)
	void addsPlaylistAndListsAllPlaylists() throws IOException, InterruptedException {
		var expectedResponse = """
          {
            "length": 0,
            "playlists": [
              { "id": 1, "name": "Empty", "tracks": [], "owner": true },
              { "id": 3, "name": "Progressive Rock", "tracks": [], "owner": true }
            ]
          }
          """;

		var playlist = """
          { "id": -1, "name": "Progressive Rock", "tracks": [], "owner": false }
          """;

		assertRequiresAuthentication(httpClient.post("/playlists", playlist));
		assertResponse(200, expectedResponse, httpClient.post("/playlists?token=" + AUTH_TOKEN, playlist));
	}

	@Test
	@Order(4)
	void editsPlaylistAndListsAllPlaylists() throws IOException, InterruptedException {
		var expectedResponse = """
          {
            "length": 474,
            "playlists": [
              { "id": 3, "name": "Progressive Rock", "tracks": [], "owner": true },
              {
                "id": 1,
                "name": "Death Metal",
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
                ],
                "owner": true
              }
            ]
          }
          """;

		var playlist = """
          {
            "id": 1,
            "name": "Death Metal",
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
            ],
            "owner": false
          }
          """;

		assertRequiresAuthentication(httpClient.put("/playlists/1", playlist));
		assertResponse(200, expectedResponse, httpClient.put("/playlists/1?token=" + AUTH_TOKEN, playlist));
	}

	@Test
	@Order(5)
	void listsAvailableTracks() throws IOException, InterruptedException {
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

		assertRequiresAuthentication(httpClient.get("/playlists/1/tracks"));
		assertResponse(200, expectedResponse, httpClient.get("/playlists/1/tracks?token=" + AUTH_TOKEN));
	}

	@Test
	@Order(6)
	void addsTrackToPlaylistAndListsPlaylistTracks() throws IOException, InterruptedException {
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

		var track = """
          {
            "id": 1,
            "title": "American Love",
            "duration": 179,
            "offlineAvailable": true,
            "performer": "Smallpools",
            "album": "Lovetap!"
          }
          """;

		assertRequiresAuthentication(httpClient.post("/playlists/1/tracks", track));
		assertResponse(200, expectedResponse, httpClient.post("/playlists/1/tracks?token=" + AUTH_TOKEN, track));
	}

	@Test
	@Order(7)
	void removesTrackFromPlaylistAndListsPlaylistTracks() throws IOException, InterruptedException {
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
              }
            ]
          }
          """;

		assertRequiresAuthentication(httpClient.delete("/playlists/1/tracks/2"));
		assertResponse(200, expectedResponse, httpClient.delete("/playlists/1/tracks/2?token=" + AUTH_TOKEN));
	}

}
