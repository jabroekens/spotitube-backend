# Spotitube
_All your Spotify and YouTube playlists in one conventient overview_

## Run
```
./gradlew build -x check
docker compose -f docker/docker-compose.yaml up --build -d
```

After the containers have started and are ready, visit <https://hanica-dea.github.io/spotitube/> and fill in:
* Server URL: `http://localhost:8080`
* User: `john`
* Password: `password`

## Documentation
See [Design Document](docs/design-document.md) for documentation.
