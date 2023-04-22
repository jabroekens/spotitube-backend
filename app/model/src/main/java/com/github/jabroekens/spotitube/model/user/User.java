package com.github.jabroekens.spotitube.model.user;

import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * A user can manage one or more {@link Playlist playlist(s)}.
 */
public class User {

    private final String id;

    private String passwordHash;

    private String name;

    public User(String id, String passwordHash, String name) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    public User(User user) {
        this.id = user.id;
        this.passwordHash = user.passwordHash;
        this.name = user.name;
    }

    @UserId
    public String getId() {
        return id;
    }

    @NotBlank
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(@NotBlank String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}