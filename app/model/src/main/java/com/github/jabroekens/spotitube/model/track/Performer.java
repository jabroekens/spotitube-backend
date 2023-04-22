package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.user.User;

/**
 * A performer is a {@link User user} who can also publish {@link Track tracks}.
 */
public class Performer extends User {

	public Performer(String id, String passwordHash, String name) {
		super(id, passwordHash, name);
	}

}
