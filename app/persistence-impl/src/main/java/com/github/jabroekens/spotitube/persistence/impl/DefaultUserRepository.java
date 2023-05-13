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
public class DefaultUserRepository implements UserRepository {

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
          var results = stmt.executeQuery("SELECT id, passwordHash, name FROM \"User\"")
        ) {
            while (results.next()) {
                users.add(
                  new User(
                    results.getString("id"),
                    results.getString("passwordHash"),
                    results.getString("name")
                  )
                );
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
            conn.prepareStatement("SELECT id, passwordHash, name FROM \"User\" WHERE id=?"),
            userId
          );
          var results = stmt.executeQuery()
        ) {
            if (results.next()) {
                return Optional.of(
                  new User(
                    results.getString("id"),
                    results.getString("passwordHash"),
                    results.getString("name")
                  )
                );
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findByName(String name) {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(
            conn.prepareStatement("SELECT id, passwordHash, name FROM \"User\" WHERE name=?"),
            name
          );
          var results = stmt.executeQuery()
        ) {
            if (results.next()) {
                return Optional.of(
                  new User(
                    results.getString("id"),
                    results.getString("passwordHash"),
                    results.getString("name")
                  )
                );
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }

        return Optional.empty();
    }

    @Override
    public User save(User user) {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(
            conn.prepareStatement("""
              INSERT INTO "User" (id, passwordHash, name)
              VALUES (?, ?, ?)
              ON CONFLICT (id) DO UPDATE SET passwordHash=?, name=?
              """),
            user.getId(), user.getPasswordHash(), user.getName(),
            user.getPasswordHash(), user.getName()
          );
        ) {
            stmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean remove(String id) {
        try (
          var conn = dataSource.getConnection();
          var stmt = withParams(
            conn.prepareStatement("DELETE FROM \"User\" WHERE id=?"),
            id
          );
        ) {
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
