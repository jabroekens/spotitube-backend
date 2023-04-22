package com.github.jabroekens.spotitube.model.user;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void equalsFollowsContract() {
        EqualsVerifier.forClass(User.class)
                .withOnlyTheseFields("id")
                .verify();
    }

}
