package com.github.jabroekens.spotitube.persistence.impl;

import com.github.jabroekens.spotitube.model.user.User;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.UserRepository;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import javax.sql.DataSource;

import static com.github.jabroekens.spotitube.persistence.impl.JdbcHelper.withParams;

@ApplicationScoped
public class JdbcUserRepository implements UserRepository {

    private static final String FIND_ALL_USERS = """
	  SELECT id AS User_id, passwordHash AS User_passwordHash, name AS User_name
	  FROM "User"
	  """;

    private static final String INSERT_USER = """
	  INSERT INTO "User" (id, passwordHash, name) VALUES (?, ?, ?)
	  """;

    private static final String UPDATE_USER = """
	  UPDATE "User" SET passwordHash=?, name=? WHERE id=?
	  """;

    private DataSource dataSource;

    @Resource(name = "SpotitubeDb")
    protected void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Collection<User> findAll() throws PersistenceException {
        var users = new LinkedHashSet<User>();

        try (
          var conn = dataSource.getConnection();
          var stmt = conn.createStatement();
          var results = stmt.executeQuery(FIND_ALL_USERS)
        ) {
            while (results.next()) {
                users.add(JdbcHelper.toEntity(User.class, results));
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

        return users;
    }

    @Override
    public Optional<User> findById(String userId) throws PersistenceException {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(
            conn.prepareStatement(FIND_ALL_USERS + " WHERE id=?"),
            userId
          );
          var results = stmt.executeQuery()
        ) {
            return Optional.ofNullable(results.next() ? JdbcHelper.toEntity(User.class, results) : null);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<User> findByName(String name) throws PersistenceException {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(
            conn.prepareStatement(FIND_ALL_USERS + " WHERE name=?"),
            name
          );
          var results = stmt.executeQuery()
        ) {
            return Optional.ofNullable(results.next() ? JdbcHelper.toEntity(User.class, results) : null);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public User add(User user) throws PersistenceException {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(
            conn.prepareStatement(INSERT_USER),
            user.getId(), user.getPasswordHash(), user.getName()
          )
        ) {
            stmt.executeUpdate();
            return new User(user);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public User merge(User user) throws PersistenceException {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(
            conn.prepareStatement(UPDATE_USER),
            user.getPasswordHash(), user.getName(), user.getId()
          )
        ) {
            stmt.executeUpdate();
            return new User(user);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean remove(String id) {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(conn.prepareStatement("DELETE FROM \"User\" WHERE id=?"), id)
        ) {
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
