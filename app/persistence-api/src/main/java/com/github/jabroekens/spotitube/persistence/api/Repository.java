package com.github.jabroekens.spotitube.persistence.api;

import java.util.Collection;
import java.util.Optional;

interface Repository<T, K> {

	/**
	 * Finds all of {@link T} in this repository.
	 *
	 * @return the complete collection of {@link T}.
	 */
	Collection<T> findAll() throws PersistenceException;

	/**
	 * Finds {@link T} whose ID is {@link K}.
	 *
	 * @return an {@link Optional} of {@link T} if found, else an empty {@link Optional}.
	 */
	Optional<T> findById(K k) throws PersistenceException;

	/**
	 * Adds {@code t} to this repository.
	 *
	 * @param t the {@link T} to be added.
	 *
	 * @return the added instance of {@code t}.
	 *
	 * @throws PersistenceException if {@code t} already exists in this repository.
	 */
	T add(T t) throws PersistenceException;

	/**
	 * Merges {@code t} into this repository.
	 *
	 * @param t the {@link T} to be merged.
	 *
	 * @return the merged instance of {@code t}.
	 *
	 * @throws PersistenceException if {@code t} does not yet exist in this repository.
	 */
	T merge(T t) throws PersistenceException;

	/**
	 * Removes {@link T} whose ID is {@code k} from this repository.
	 *
	 * @param k the ID of the {@link T} to be removed.
	 *
	 * @return {@code true} if a {@link T} whose ID is {@code k} was removed, {@code false} otherwise.
	 */
	boolean remove(K k) throws PersistenceException;

}
