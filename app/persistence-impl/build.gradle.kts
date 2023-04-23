plugins {
	id("jakartaee-common-conventions")
	id("testcontainers-common-conventions")
}

dependencies {
	implementation(project(":app:persistence-api"))

	implementation("jakarta.enterprise:jakarta.enterprise.cdi-api")
	implementation("jakarta.annotation:jakarta.annotation-api")
	implementation("jakarta.inject:jakarta.inject-api")

	integrationTestImplementation(project(":app:persistence-api"))
	integrationTestImplementation("org.testcontainers:postgresql")
	integrationTestImplementation(libs.postgresql)
}

tasks {
	jacocoTestCoverageVerification {
		violationRules {
			repeat(rules.size) {
				rules[it].excludes = mutableListOf(
					"com.github.jabroekens.spotitube.persistence.impl.JdbcHelper"
				).apply {
					addAll(rules[it].excludes)
				}
			}
		}
	}
}
