plugins {
	id("jakartaee-common-conventions")

	`java-library`
}

dependencies {
	api(project(":app:model"))
	implementation("jakarta.validation:jakarta.validation-api")
}

tasks {
	jacocoTestCoverageVerification {
		violationRules {
			repeat(rules.size) {
				rules[it].excludes = mutableListOf(
					"com.github.jabroekens.spotitube.service.api.track.playlist.PlaylistRequest"
				).apply {
					addAll(rules[it].excludes)
				}
			}
		}
	}
}
