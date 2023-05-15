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

    private static final String SAVE_USER = """
	  INSERT INTO "User" (id, passwordHash, name)
	  VALUES (?, ?, ?)
	  ON CONFLICT (id) DO UPDATE SET passwordHash=?, name=?
	  """;

    private DataSource dataSource;

    @Resource(name = "SpotitubeDb")
    protected void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Collection<User> findAll() {
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
    public Optional<User> findById(String userId) {
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
    public Optional<User> findByName(String name) {
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
    public User save(User user) {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(
            conn.prepareStatement(SAVE_USER),
            user.getId(), user.getPasswordHash(), user.getName(),
            user.getPasswordHash(), user.getName()
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
