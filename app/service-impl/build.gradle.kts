plugins {
	id("jakartaee-common-conventions")
}

dependencies {
	implementation(project(":app:service-api"))
	implementation(project(":app:persistence-api"))

	implementation("jakarta.enterprise:jakarta.enterprise.cdi-api")
	implementation("jakarta.inject:jakarta.inject-api")

	implementation(libs.spring.security.crypto)
}

tasks {
	jacocoTestCoverageVerification {
		violationRules {
			repeat(rules.size) {
				rules[it].excludes = mutableListOf(
					"com.github.jabroekens.spotitube.service.impl.PasswordEncoderProducer"
				).apply {
					addAll(rules[it].excludes)
				}
			}
		}
	}
}
