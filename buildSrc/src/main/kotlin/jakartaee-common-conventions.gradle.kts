val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
	id("java-common-conventions")
}

dependencies {
	implementation(platform(libs.findLibrary("jakartaee-bom").get()))
}
