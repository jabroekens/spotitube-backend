package com.github.jabroekens.spotitube.model.user;

import com.github.jabroekens.spotitube.model.Entity;
import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * A user can manage one or more {@link Playlist playlist(s)}.
 */
@Entity
public class User {

    private String id;
    private String passwordHash;
    private String name;

    /**
     * @deprecated Internal no-args constructor used by framework.
     */
    @Deprecated
    protected User() {
    }

    public User(String id, String passwordHash, String name) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    /**
     * Returns a deep copy of {@code user}.
     */
    public User(User user) {
        this(user.id, user.passwordHash, user.name);
    }

    @UserId
    public String getId() {
        return id;
    }

    @NotBlank
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId())
               && Objects.equals(getPasswordHash(), user.getPasswordHash())
               && Objects.equals(getName(), user.getName());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }

}
