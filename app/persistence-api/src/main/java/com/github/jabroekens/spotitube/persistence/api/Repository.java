package com.github.jabroekens.spotitube.persistence.api;

import java.util.Collection;
import java.util.Optional;

interface Repository<T, K> {

	/**
	 * Finds all of {@link T} in this repository.
	 *
	 * @return the complete collection of {@link T}.
	 */
	Collection<T> findAll();

	/**
	 * Finds {@link T} whose ID is {@link K}.
	 *
	 * @return an {@link Optional} of {@link T} if found, else an empty {@link Optional}.
	 */
	Optional<T> findById(K k);

	/**
	 * Saves {@code t} to this repository.
	 *
	 * @param t the {@link T} to be added.
	 *
	 * @return the saved instance of {@code t}.
	 */
	T save(T t);

	/**
	 * Removes {@code t} from this repository.
	 *
	 * @param t the {@link T} to be removed.
	 */
	boolean remove(T t);

}
