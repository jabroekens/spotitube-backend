plugins {
	id("jakartaee-common-conventions")
	id("testcontainers-common-conventions")
}

dependencies {
	implementation(project(":app:persistence-api"))

	integrationTestImplementation(project(":app:persistence-api"))
	integrationTestImplementation("org.testcontainers:postgresql")
	integrationTestImplementation(libs.postgresql)
}
