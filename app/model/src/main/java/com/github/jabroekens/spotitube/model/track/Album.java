package com.github.jabroekens.spotitube.model.track;

import jakarta.validation.constraints.NotBlank;

/**
 * One or more {@link Track track(s)} belong to an album.
 */
public class Album {

	private String name;

	public Album(String name) {
		this.name = name;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(@NotBlank String name) {
		this.name = name;
	}

}
