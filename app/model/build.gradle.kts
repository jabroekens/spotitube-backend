plugins {
	id("jakartaee-common-conventions")

	`java-library`
}

dependencies {
	api("jakarta.validation:jakarta.validation-api")
}

tasks {
	jacocoTestCoverageVerification {
		violationRules {
			repeat(rules.size) {
				rules[it].excludes = mutableListOf(
					"com.github.jabroekens.spotitube.model.track.Album",
					"com.github.jabroekens.spotitube.model.track.Performer",
					"com.github.jabroekens.spotitube.model.track.Track",
					"com.github.jabroekens.spotitube.model.track.playlist.Playlist",
					"com.github.jabroekens.spotitube.model.track.playlist.PlaylistCollection"
				).apply {
					addAll(rules[it].excludes)
				}
			}
		}
	}
}
