package com.github.jabroekens.spotitube.app.config.security;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates a resource that requires authorization.
 * By default the authenticated user can manage their own data.
 */
@NameBinding
@Documented
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface Secured {

	/**
	 * {@return the required right(s)}
	 */
	Right[] value() default {Right.CreatePlaylist, Right.ModifyPlaylist, Right.DeletePlaylist};

}
