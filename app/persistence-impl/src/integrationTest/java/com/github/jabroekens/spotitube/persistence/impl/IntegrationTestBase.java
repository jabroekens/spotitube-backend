package com.github.jabroekens.spotitube.persistence.impl;

import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.postgresql.ds.PGSimpleDataSource;
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

    @BeforeEach
    void waitUntilReady() throws InterruptedException {
        Thread.sleep(2000);
    }

    protected DataSource getDataSource() {
        var ds = new PGSimpleDataSource();
        ds.setURL(dbContainer.getJdbcUrl());
        ds.setUser("postgres");
        ds.setPassword("postgres");
        return ds;
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
