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
	runtimeOnly(project(":app:persistence-impl"))

	implementation("jakarta.inject:jakarta.inject-api")
	implementation("jakarta.ws.rs:jakarta.ws.rs-api")

	implementation(libs.jjwt.api)
	runtimeOnly(libs.jjwt.impl)
	runtimeOnly(libs.jjwt.jackson)
}

tasks {
	val copyExecutionData by registering(JacocoDumpCommand::class) {
		port.set(6300)

		val it by integrationTest
		testReportTask.set(it.extensions.getByType(JacocoTaskExtension::class))
	}

	integrationTest {
		dependsOn(war)
		finalizedBy(copyExecutionData)
	}

	jacocoTestCoverageVerification {
		violationRules {
			repeat(rules.size) {
				rules[it].excludes = mutableListOf(
					"com.github.jabroekens.spotitube.app.config.**"
				).apply {
					addAll(rules[it].excludes)
				}
			}
		}
	}
}
