package com.github.jabroekens.spotitube.service.impl;

import jakarta.enterprise.inject.Produces;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordEncoderProducer {

    @Produces
    PasswordEncoder argon2PasswordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

}
