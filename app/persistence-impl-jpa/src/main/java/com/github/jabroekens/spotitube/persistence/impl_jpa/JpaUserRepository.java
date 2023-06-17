package com.github.jabroekens.spotitube.persistence.impl_jpa;

import com.github.jabroekens.spotitube.model.user.User;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
public class JpaUserRepository extends JpaRepository<User, String> implements UserRepository {

	@Override
	public Collection<User> findAll() throws PersistenceException {
		return super.findAll("select u from User u", User.class);
	}

	@Override
	public Optional<User> findById(String id) throws PersistenceException {
		return super.findById("select u from User u where u.id = ?1", id, User.class);
	}

	@Override
	public Optional<User> findByName(String name) throws PersistenceException {
		var query = withParams("select u from User u where u.name = ?1", User.class, name);
		return super.tryGetSingleResult(query);
	}

	@Override
	public User add(User user) throws PersistenceException {
		return super.add(user);
	}

	@Override
	public User merge(User user) throws PersistenceException {
		return super.merge(user);
	}

	@Override
	public boolean remove(String id) {
		return super.remove("delete from User u where u.id = ?1", id);
	}

}
