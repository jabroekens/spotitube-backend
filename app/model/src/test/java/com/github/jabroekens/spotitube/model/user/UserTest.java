package com.github.jabroekens.spotitube.model.user;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void equalsFollowsContract() {
        EqualsVerifier.forClass(User.class)
          .suppress(Warning.NONFINAL_FIELDS)
          .withOnlyTheseFields("id")
          .verify();
    }

}
