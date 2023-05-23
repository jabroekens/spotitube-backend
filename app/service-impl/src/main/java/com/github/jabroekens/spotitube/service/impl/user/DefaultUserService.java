package com.github.jabroekens.spotitube.service.impl.user;

import com.github.jabroekens.spotitube.model.user.User;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import com.github.jabroekens.spotitube.service.api.user.IncorrectPasswordException;
import com.github.jabroekens.spotitube.service.api.user.UserService;
import jakarta.inject.Inject;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Inject
    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String userId, String password) throws EntityNotFoundException, IncorrectPasswordException {
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(User.class, Map.of("id", userId)));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IncorrectPasswordException(user.getId());
        } else {
            return user;
        }
    }

}
