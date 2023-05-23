package com.github.jabroekens.spotitube.app.config.mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class UserMixin {

	@JsonIgnore
	String passwordHash;

}
