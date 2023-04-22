val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    id("java-common-conventions")
}

testing.suites.named<JvmTestSuite>("integrationTest") {
    dependencies {
        implementation(project.dependencies.platform(libs.findLibrary("testcontainers-bom").get()))
        implementation("org.testcontainers:junit-jupiter")
    }
}
