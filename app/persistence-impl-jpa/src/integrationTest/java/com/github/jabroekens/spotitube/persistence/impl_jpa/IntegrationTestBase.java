package com.github.jabroekens.spotitube.persistence.impl_jpa;

import jakarta.persistence.EntityManager;
import java.util.Map;
import java.util.Optional;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Testcontainers
abstract class IntegrationTestBase {

	@Container
	private final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:15.2")
	  .withUsername("postgres")
	  .withPassword("postgres")
	  .withFileSystemBind("../../docker/db/", "/docker-entrypoint-initdb.d/");

	protected EntityManager getEntityManager() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		var emf = new HibernatePersistenceProvider().createEntityManagerFactory(
		  "IntegrationTest",
		  Map.of("jakarta.persistence.jdbc.url", dbContainer.getJdbcUrl())
		);
		return emf.createEntityManager();
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	protected static <T> void assertMatchesValueInDataStore(T expected, T actual, Optional<T> dataStoreValue) {
		dataStoreValue.ifPresentOrElse(
		  (value) -> {
			  assertEquals(expected, actual);
			  assertEquals(value, actual);
		  },
		  () -> fail("No value present")
		);
	}

	// Required tests
	// See also: https://github.com/junit-team/junit5/issues/960

	abstract void addsSuccesfully();

	abstract void throwsExceptionWhenAddingExistent();

	abstract void mergesExistingSuccesfully();

	abstract void addsWhenMergingNonexistent();

	abstract void removesIfExists();

	abstract void findsAll();

	abstract void findsById();

	abstract void findsNothingByNonexistentId();

}
