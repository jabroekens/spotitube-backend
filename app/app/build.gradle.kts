import com.github.jabroekens.spotitube.JacocoDumpCommand

plugins {
	id("jakartaee-common-conventions")
	id("testcontainers-common-conventions")

	war
	`jacoco-report-aggregation`
}

dependencies {
	implementation(project(":app:service-api"))
	runtimeOnly(project(":app:service-impl"))
	runtimeOnly(project(":app:persistence-impl-jpa"))

	implementation("jakarta.inject:jakarta.inject-api")
	implementation("jakarta.ws.rs:jakarta.ws.rs-api")

	integrationTestImplementation(libs.jackson.databind)
	integrationTestImplementation(libs.jsonassert)

	implementation(libs.jjwt.api)
	runtimeOnly(libs.jjwt.impl)
	runtimeOnly(libs.jjwt.jackson)
}

tasks {
	val copyExecutionData by registering(JacocoDumpCommand::class) {
		port.set(6300)

		val it by integrationTest
		destinationFile.set(it.extensions.getByType(JacocoTaskExtension::class).destinationFile)
	}

	integrationTest {
		dependsOn(war)
		finalizedBy(copyExecutionData)
	}

	jacocoTestCoverageVerification {
		violationRules {
			repeat(rules.size) {
				rules[it].excludes = mutableListOf(
					"com.github.jabroekens.spotitube.app.config.**",
					"com.github.jabroekens.spotitube.app.**.dto.*"
				).apply {
					addAll(rules[it].excludes)
				}
			}
		}
	}
}
