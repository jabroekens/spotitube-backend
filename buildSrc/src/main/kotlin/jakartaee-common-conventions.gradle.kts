val libs = versionCatalogs.named("libs")

plugins {
	id("java-common-conventions")
}

dependencies {
	implementation(platform(libs.findLibrary("jakartaee-bom").get()))
}
