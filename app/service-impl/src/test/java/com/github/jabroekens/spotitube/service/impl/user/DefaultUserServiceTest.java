package com.github.jabroekens.spotitube.service.impl.user;

import com.github.jabroekens.spotitube.model.Users;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import com.github.jabroekens.spotitube.service.api.EntityNotFoundException;
import com.github.jabroekens.spotitube.service.api.user.IncorrectPasswordException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private DefaultUserService sut;

	@Test
	void getsUserWhenGivenCorrectCredentials() {
		when(userRepository.findById(any())).thenReturn(Optional.of(Users.JOHN_DOE));
		when(passwordEncoder.matches(any(), any())).thenReturn(true);

		var password = Users.JOHN_DOE.getPasswordHash();
		var user = sut.getUser(Users.JOHN_DOE.getId(), password);

		assertEquals(Users.JOHN_DOE, user);
		verify(passwordEncoder).matches(Users.JOHN_DOE.getPasswordHash(), password);
	}

	@Test
	void throwsExceptionWhenGivenIncorrectCredentials() {
		when(userRepository.findById(any())).thenReturn(Optional.of(Users.JOHN_DOE));

		var password = Users.JOHN_DOE.getPasswordHash() + "1";
		assertThrows(IncorrectPasswordException.class, () -> sut.getUser(Users.JOHN_DOE.getId(), password));

		verify(passwordEncoder).matches(password, Users.JOHN_DOE.getPasswordHash());
	}

	@Test
	void throwsExceptionWhenUserNotFound() {
		when(userRepository.findById(any())).thenReturn(Optional.empty());
		assertThrows(
		  EntityNotFoundException.class,
		  () -> sut.getUser(Users.JOHN_DOE.getId(), Users.JOHN_DOE.getPasswordHash())
		);
	}

}
