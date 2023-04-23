package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.user.Users;
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

    @BeforeEach
    void setUp() {
        var repository = new DefaultUserRepository();
        repository.setDataSource(getDataSource());
        sut = repository;
    }

    @Test
    void insertsNewUserSuccesfully() {
        var savedUser = sut.save(Users.ALTERNATIVE);
        assertEquals(Users.ALTERNATIVE, savedUser);
    }

    @Test
    void updatesUserIfExists() {
        sut.save(Users.DEFAULT_CHANGED_NAME);

        var savedUser = sut.findById(Users.DEFAULT.getId());

        savedUser.ifPresentOrElse(
          (u) -> assertAll(
            // The `equals()` implementation only looks at the business key, so we
            // must assert the name separately: https://stackoverflow.com/a/1638886
            () -> assertEquals(Users.DEFAULT_CHANGED_NAME, u),
            () -> assertEquals(Users.DEFAULT_CHANGED_NAME.getName(), u.getName())
          ),
          () -> fail("No value present")
        );
    }

    @Test
    void removesUserIfExists() {
        assertTrue(sut.remove(Users.DEFAULT));
        assertFalse(sut.remove(Users.DEFAULT));
    }

    @Test
    void findsAllUsers() {
        sut.save(Users.ALTERNATIVE);
        var users = sut.findAll();
        assertEquals(Set.of(Users.DEFAULT, Users.ALTERNATIVE), users);
    }

    @Test
    void findsUserMatchingName() {
        sut.save(Users.ALTERNATIVE);

        var user1 = sut.findByName(Users.DEFAULT.getName());
        var user2 = sut.findByName(Users.ALTERNATIVE.getName());

        assertAll(
          () -> user1.ifPresentOrElse(u -> assertEquals(Users.DEFAULT, u), () -> fail("No value present")),
          () -> user2.ifPresentOrElse(u -> assertEquals(Users.ALTERNATIVE, u), () -> fail("No value present"))
        );
    }

}
