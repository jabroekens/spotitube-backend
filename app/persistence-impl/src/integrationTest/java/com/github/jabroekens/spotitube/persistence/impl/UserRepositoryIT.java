package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.Performers;
import com.github.jabroekens.spotitube.model.Users;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.commons.collections4.CollectionUtils.isEqualCollection;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        var user = Users.JaneDoe();
        var savedUser = sut.add(user);
        assertMatchesValueInDataStore(user, savedUser, sut.findById(user.getId()));
    }

    @Test
    @Override
    void throwsExceptionWhenAddingExistent() {
        assertThrows(PersistenceException.class, () -> sut.add(Users.JohnDoe()));
    }

    @Test
    @Override
    void mergesExistingSuccesfully() {
        var user = Users.JohnDoe();
        user.setName("John Smith");

        var mergedUser = sut.merge(user);
        assertMatchesValueInDataStore(user, mergedUser, sut.findById(user.getId()));
    }

    @Test
    @Override
    void addsWhenMergingNonexistent() {
        var user = Users.JaneDoe();
        var merged = sut.merge(user);
        assertMatchesValueInDataStore(user, merged, sut.findById(user.getId()));
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
        var expectedUsers = Stream.of(
          Users.JohnDoe(), Performers.Smallpools(),
          Performers.Kurzgesagt(), Performers.Exurb1a()
        ).collect(Collectors.toCollection(ArrayList::new));

        assertTrue(isEqualCollection(expectedUsers, sut.findAll()));

        expectedUsers.add(sut.add(Users.JaneDoe()));
        assertTrue(isEqualCollection(expectedUsers, sut.findAll()));
    }

    @Test
    @Override
    void findsById() {
        var existingUser = Users.JohnDoe();
        var addedUser = sut.add(Users.JaneDoe());

        var user1 = sut.findById(existingUser.getId());
        var user2 = sut.findById(addedUser.getId());

        assertAll(
          () -> user1.ifPresentOrElse(u -> assertEquals(existingUser, u), () -> fail("No value present")),
          () -> user2.ifPresentOrElse(u -> assertEquals(addedUser, u), () -> fail("No value present"))
        );
    }

    @Test
    @Override
    void findsNothingByNonexistentId() {
        assertEquals(Optional.empty(), sut.findById("_"));
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

    @Test
    void findsNothingByNonexistentName() {
        assertEquals(Optional.empty(), sut.findByName("Clair Patterson"));
    }

}
