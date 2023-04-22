package com.github.jabroekens.spotitube.persistence.api;

import com.github.jabroekens.spotitube.model.user.User;
import java.util.Optional;

public interface UserRepository extends Repository<User, String> {

    /**
     * Finds the {@link User} whose name matches {@code name} exactly.
     *
     * @return an {@link Optional} of {@link User} if found, else an empty {@link Optional}.
     */
    Optional<User> findByName(String name);

}
