val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val javaVersion = libs.findVersion("java").get().requiredVersion
val junitJupiterVersion = libs.findVersion("junit-jupiter").get().requiredVersion

plugins {
	id("project-common-conventions")

	java
	`java-test-fixtures`
	jacoco
}

dependencies {
	testImplementation(libs.findLibrary("equalsverifier").get())
	testImplementation(libs.findLibrary("mockito-core").get())
	testImplementation(libs.findLibrary("mockito-junit-jupiter").get())
	testImplementation(testFixtures(project(":app:model")))

	testFixturesApi(project(":app:model"))
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(javaVersion))
	}
}

testing {
	suites {
		withType<JvmTestSuite> {
			useJUnitJupiter(junitJupiterVersion)
		}

		register<JvmTestSuite>("integrationTest") {
			dependencies {
				implementation(project())
				implementation(testFixtures(project(":app:model")))
				implementation(libs.findLibrary("commons-collections4").get())
				implementation(project.dependencies.testFixtures(project))
			}

			targets {
				all {
					testTask {
						shouldRunAfter("test")
					}
				}
			}
		}
	}
}

tasks {
	val integrationTest by named("integrationTest")

	withType<JavaCompile> {
		sourceCompatibility = javaVersion
		targetCompatibility = javaVersion

		options.apply {
			release.set(javaVersion.toInt())
			encoding = "UTF-8"
		}
	}

	withType<Javadoc> {
		options.encoding = "UTF-8"
	}

	val tests = withType<Test> {
		finalizedBy(jacocoTestReport)
	}

	check {
		dependsOn(integrationTest)
	}

	jacocoTestReport {
		dependsOn(tests)
		reports {
			xml.required.set(true)
		}

		executionData(integrationTest)
		sourceSets(sourceSets[integrationTest.name])
	}

	jacocoTestCoverageVerification {
		executionData(integrationTest)
		dependsOn(check)

		violationRules {
			rule {
				element = "CLASS"
				limit {
					counter = "BRANCH"
					value = "COVEREDRATIO"
					minimum = "0.8".toBigDecimal()
				}
				limit {
					counter = "CLASS"
					value = "MISSEDCOUNT"
					maximum = "0".toBigDecimal()
				}
				excludes = listOf(
					"com.github.jabroekens.spotitube.**.*Exception"
				)
			}
		}
	}
}
