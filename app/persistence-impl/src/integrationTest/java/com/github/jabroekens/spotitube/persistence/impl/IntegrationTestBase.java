package com.github.jabroekens.spotitube.persistence.impl;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
abstract class IntegrationTestBase {

    @Container
    private final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:15.2")
      .withUsername("postgres")
      .withPassword("postgres")
      .withFileSystemBind("../../docker/db/", "/docker-entrypoint-initdb.d/");

    protected DataSource getDataSource() {
        var ds = new PGSimpleDataSource();
        ds.setURL(dbContainer.getJdbcUrl());
        ds.setUser("postgres");
        ds.setPassword("postgres");
        return ds;
    }

    // Required tests
    // See also: https://github.com/junit-team/junit5/issues/960

    abstract void savesSuccesfully();

    abstract void updatesIfExists();

    abstract void removesIfExists();

    abstract void findsAll();

    abstract void findsById();

}
