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
}
