package com.github.jabroekens.spotitube.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates a domain type. Types annotated with this annotation <b>must</b> have a package-private no-args constructor.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface Entity {

}
