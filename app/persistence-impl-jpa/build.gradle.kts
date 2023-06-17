plugins {
	id("jakartaee-common-conventions")
	id("testcontainers-common-conventions")
}

dependencies {
	implementation(project(":app:persistence-api"))

	implementation("jakarta.persistence:jakarta.persistence-api")
	implementation("jakarta.enterprise:jakarta.enterprise.cdi-api")
	implementation("jakarta.annotation:jakarta.annotation-api")
	implementation("jakarta.inject:jakarta.inject-api")

//	runtimeOnly("org.hibernate.orm:hibernate-core:6.2.3.Final")

	integrationTestImplementation(project(":app:persistence-api"))
	integrationTestImplementation("org.testcontainers:postgresql")
	integrationTestImplementation(libs.postgresql)

	integrationTestImplementation(platform(libs.jakartaee.bom))
	integrationTestImplementation("jakarta.persistence:jakarta.persistence-api")
	integrationTestImplementation("org.hibernate.orm:hibernate-core:6.2.3.Final")
}
