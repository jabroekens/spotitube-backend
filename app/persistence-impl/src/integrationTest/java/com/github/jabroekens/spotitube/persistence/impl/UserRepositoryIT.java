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
        var savedUser = sut.save(Users.JANE_DOE);
        assertEquals(Users.JANE_DOE, savedUser);
    }

    @Test
    @Override
    void updatesIfExists() {
        sut.save(Users.JOHN_SMITH);

        var savedUser = sut.findById(Users.JOHN_DOE.getId());

        savedUser.ifPresentOrElse(
          (u) -> assertAll(
            // The `equals()` implementation only looks at the business key, so we
            // must assert the name separately: https://stackoverflow.com/a/1638886
            () -> assertEquals(Users.JOHN_SMITH, u),
            () -> assertEquals(Users.JOHN_SMITH.getName(), u.getName())
          ),
          () -> fail("No value present")
        );
    }

    @Test
    @Override
    void removesIfExists() {
        assertTrue(sut.remove(Users.JOHN_DOE.getId()));
        assertFalse(sut.remove(Users.JOHN_DOE.getId()));
    }

    @Test
    @Override
    void findsAll() {
        sut.save(Users.JANE_DOE);
        var users = sut.findAll();

        assertEquals(
          Set.of(Users.JOHN_DOE, Performers.SMALLPOOLS, Performers.KURZGESAGT, Users.JANE_DOE),
          users
        );
    }

    @Test
    @Override
    void findsById() {
        sut.save(Users.JANE_DOE);

        var user1 = sut.findById(Users.JOHN_DOE.getId());
        var user2 = sut.findById(Users.JANE_DOE.getId());

        assertAll(
          () -> user1.ifPresentOrElse(u -> assertEquals(Users.JOHN_DOE, u), () -> fail("No value present")),
          () -> user2.ifPresentOrElse(u -> assertEquals(Users.JANE_DOE, u), () -> fail("No value present"))
        );
    }

    @Test
    void findsByName() {
        sut.save(Users.JANE_DOE);

        var user1 = sut.findByName(Users.JOHN_DOE.getName());
        var user2 = sut.findByName(Users.JANE_DOE.getName());

        assertAll(
          () -> user1.ifPresentOrElse(u -> assertEquals(Users.JOHN_DOE, u), () -> fail("No value present")),
          () -> user2.ifPresentOrElse(u -> assertEquals(Users.JANE_DOE, u), () -> fail("No value present"))
        );
    }

}
