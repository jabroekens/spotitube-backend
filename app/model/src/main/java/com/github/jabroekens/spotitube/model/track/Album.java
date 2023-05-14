package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.Entity;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * One or more {@link Track track(s)} belong to an album.
 */
@Entity
public class Album {

	private String name;

	/**
	 * @deprecated Internal no-args constructor used by framework.
	 */
	@Deprecated
	protected Album() {
	}

	public Album(String name) {
		this.name = name;
	}

	/**
	 * Returns a deep copy of {@code album}.
	 */
	public Album(Album album) {
		this.name = album.name;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(@NotBlank String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Album album)) return false;
		return Objects.equals(getName(), album.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}

}
