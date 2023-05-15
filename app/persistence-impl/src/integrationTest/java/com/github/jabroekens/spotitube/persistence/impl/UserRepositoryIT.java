package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Performers;
import com.github.jabroekens.spotitube.model.Users;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class UserRepositoryIT extends IntegrationTestBase {

    private UserRepository sut;

    // Users.JOHN_DOE is inserted by `create_tables.sql`
    @BeforeEach
    void setUp() {
        var repository = new JdbcUserRepository();
        repository.setDataSource(getDataSource());
        sut = repository;
    }

    @Test
    @Override
    void savesSuccesfully() {
        var savedUser = sut.save(Users.JaneDoe());
        assertEquals(Users.JaneDoe(), savedUser);
    }

    @Test
    @Override
    void updatesIfExists() {
        sut.save(Users.JohnSmith());

        var savedUser = sut.findById(Users.JohnDoe().getId());

        savedUser.ifPresentOrElse(
          (u) -> assertAll(
            // The `equals()` implementation only looks at the business key, so we
            // must assert the name separately: https://stackoverflow.com/a/1638886
            () -> assertEquals(Users.JohnSmith(), u),
            () -> assertEquals(Users.JohnSmith().getName(), u.getName())
          ),
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
        sut.save(Users.JaneDoe());
        var users = sut.findAll();

        assertEquals(
          Set.of(Users.JohnDoe(), Performers.Smallpools(), Performers.Kurzgesagt(), Users.JaneDoe()),
          users
        );
    }

    @Test
    @Override
    void findsById() {
        sut.save(Users.JaneDoe());

        var user1 = sut.findById(Users.JohnDoe().getId());
        var user2 = sut.findById(Users.JaneDoe().getId());

        assertAll(
          () -> user1.ifPresentOrElse(u -> assertEquals(Users.JohnDoe(), u), () -> fail("No value present")),
          () -> user2.ifPresentOrElse(u -> assertEquals(Users.JaneDoe(), u), () -> fail("No value present"))
        );
    }

    @Test
    void findsByName() {
        sut.save(Users.JaneDoe());

        var user1 = sut.findByName(Users.JohnDoe().getName());
        var user2 = sut.findByName(Users.JaneDoe().getName());

        assertAll(
          () -> user1.ifPresentOrElse(u -> assertEquals(Users.JohnDoe(), u), () -> fail("No value present")),
          () -> user2.ifPresentOrElse(u -> assertEquals(Users.JaneDoe(), u), () -> fail("No value present"))
        );
    }

}
