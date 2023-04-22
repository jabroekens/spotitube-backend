plugins {
	id("jakartaee-common-conventions")
}

dependencies {
	implementation(project(":app:service-api"))
	implementation(project(":app:persistence-api"))
}
