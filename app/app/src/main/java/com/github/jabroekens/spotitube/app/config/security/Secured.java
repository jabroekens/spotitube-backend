package com.github.jabroekens.spotitube.app.config.security;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NameBinding
@Documented
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface Secured {

}
