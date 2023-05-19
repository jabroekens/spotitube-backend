package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Performers;
import com.github.jabroekens.spotitube.model.Users;
import com.github.jabroekens.spotitube.model.user.User;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class UserRepositoryIT extends IntegrationTestBase {

    private UserRepository sut;

    // `Users.JohnDoe()` is inserted by `create_tables.sql`
    @BeforeEach
    void setUp() {
        var repository = new JdbcUserRepository();
        repository.setDataSource(getDataSource());
        sut = repository;
    }

    @Test
    @Override
    void addsSuccesfully() {
        var savedUser = sut.add(Users.JaneDoe());
        assertUser(Users.JaneDoe(), savedUser);
    }

    @Test
    @Override
    void mergesSuccesfully() {
        sut.merge(Users.JohnSmith());

        var savedUser = sut.findById(Users.JohnDoe().getId());

        savedUser.ifPresentOrElse(
          (u) -> assertUser(Users.JohnSmith(), u),
          () -> fail("No value present")
        );
    }

    @Test
    @Override
    void removesIfExists() {
        assertTrue(sut.remove(Users.JohnDoe().getId()));
        assertFalse(sut.remove(Users.JohnDoe().getId()));
    }

    @Test
    @Override
    void findsAll() {
        sut.add(Users.JaneDoe());
        var users = sut.findAll();

        assertEquals(
          Set.of(Users.JohnDoe(), Performers.Smallpools(), Performers.Kurzgesagt(), Performers.Exurb1a(), Users.JaneDoe()),
          users
        );
    }

    @Test
    @Override
    void findsById() {
        sut.add(Users.JaneDoe());

        var user1 = sut.findById(Users.JohnDoe().getId());
        var user2 = sut.findById(Users.JaneDoe().getId());

        assertAll(
          () -> user1.ifPresentOrElse(u -> assertEquals(Users.JohnDoe(), u), () -> fail("No value present")),
          () -> user2.ifPresentOrElse(u -> assertEquals(Users.JaneDoe(), u), () -> fail("No value present"))
        );
    }

    @Test
    void findsByName() {
        sut.add(Users.JaneDoe());

        var user1 = sut.findByName(Users.JohnDoe().getName());
        var user2 = sut.findByName(Users.JaneDoe().getName());

        assertAll(
          () -> user1.ifPresentOrElse(u -> assertEquals(Users.JohnDoe(), u), () -> fail("No value present")),
          () -> user2.ifPresentOrElse(u -> assertEquals(Users.JaneDoe(), u), () -> fail("No value present"))
        );
    }

    private static void assertUser(User expected, User actual, Executable... additionalAssertions) {
        // The `equals()` implementation only looks at the business key, so we
        // must assert all other fields separately: https://stackoverflow.com/a/1638886
        assertAll(
          Stream.concat(
            Stream.of(additionalAssertions),
            Stream.of(
              () -> assertEquals(expected.getPasswordHash(), actual.getPasswordHash()),
              () -> assertEquals(expected.getName(), actual.getName())
            )
          )
        );
    }

}
