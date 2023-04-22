plugins {
	id("jakartaee-common-conventions")

	`java-library`
}

dependencies {
	api(project(":app:model"))
}
