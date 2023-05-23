package com.github.jabroekens.spotitube.model.user;

import com.github.jabroekens.spotitube.model.Users;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void equalsFollowsContract() {
        EqualsVerifier.forClass(User.class)
          .suppress(Warning.STRICT_HASHCODE)
          .suppress(Warning.NONFINAL_FIELDS)
          .verify();
    }

    @Test
    void givesDeepCloneWithCopyConstructor() {
        var u1 = Users.JohnDoe();
        var u2 = new User(u1);
        assertEquals(u1, u2);
    }

}
