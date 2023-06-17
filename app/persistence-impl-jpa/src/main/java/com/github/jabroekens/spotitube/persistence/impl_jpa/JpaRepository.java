package com.github.jabroekens.spotitube.persistence.impl_jpa;

import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

public abstract class JpaRepository<T, K> {

	protected EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected Collection<T> findAll(String queryString, Class<T> type) throws PersistenceException {
		try {
			return entityManager.createQuery(queryString, type).getResultList();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	protected Optional<T> findById(String queryString, K k, Class<T> resultType) throws PersistenceException {
		var query = withParams(queryString, resultType, k);
		return tryGetSingleResult(query);
	}

	protected <Q> Optional<Q> tryGetSingleResult(TypedQuery<Q> typedQuery) {
		try {
			return Optional.of(typedQuery.getSingleResult());
		} catch (NoResultException ignored) {
			return Optional.empty();
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	protected T add(T t) throws PersistenceException {
		try {
			entityManager.persist(t);
			return t;
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	protected T merge(T t) throws PersistenceException {
		try {
			return entityManager.merge(t);
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	protected boolean remove(String queryString, K k) throws PersistenceException {
		try {
			var query = withParams(queryString, k);
			return query.executeUpdate() > 0;
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}

	public <R> TypedQuery<R> withParams(String queryString, Class<R> resultType, Object... params) {
		var query = entityManager.createQuery(queryString, resultType);
		for (int i = 0; i < params.length; i++) {
			var obj = params[i];
			query.setParameter(i + 1, obj);
		}
		return query;
	}

	public Query withParams(String queryString, Object... params) {
		var query = entityManager.createQuery(queryString);
		for (int i = 0; i < params.length; i++) {
			var obj = params[i];
			query.setParameter(i + 1, obj);
		}
		return query;
	}

}
