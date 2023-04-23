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
      // Users.DEFAULT is inserted by `create_tables.sql`
      .withFileSystemBind("../../docker/db/", "/docker-entrypoint-initdb.d/");

    protected DataSource getDataSource() {
        var ds = new PGSimpleDataSource();
        ds.setURL(dbContainer.getJdbcUrl());
        ds.setUser("postgres");
        ds.setPassword("postgres");
        return ds;
    }

}
