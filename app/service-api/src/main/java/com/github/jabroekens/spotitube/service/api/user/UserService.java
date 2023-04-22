package com.github.jabroekens.spotitube.service.api.user;

import com.github.jabroekens.spotitube.model.user.User;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;

public interface UserService {

	/**
	 * {@return the user's information if the password matches}
	 *
	 * @param userId   the {@link String ID} of the user.
	 * @param password the password of the user.
	 *
	 * @throws EntityNotFoundException    when no user has been found with ID {@code userId}.
	 * @throws IncorrectPasswordException when the given password {@code password} is incorrect for the user with ID
	 *                                    {@code userId}.
	 */
	User getUser(String userId, String password) throws EntityNotFoundException, IncorrectPasswordException;

}
