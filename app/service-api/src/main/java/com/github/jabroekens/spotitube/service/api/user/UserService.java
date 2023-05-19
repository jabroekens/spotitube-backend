package com.github.jabroekens.spotitube.service.api.user;

import com.github.jabroekens.spotitube.model.user.User;
import com.github.jabroekens.spotitube.model.user.UserId;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;

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
	User getUser(@UserId String userId, @NotBlank String password) throws EntityNotFoundException, IncorrectPasswordException;

}
