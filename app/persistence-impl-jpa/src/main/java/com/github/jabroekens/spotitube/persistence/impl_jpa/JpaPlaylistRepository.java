package com.github.jabroekens.spotitube.persistence.impl_jpa;

import com.github.jabroekens.spotitube.model.track.playlist.Playlist;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.PlaylistRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
public class JpaPlaylistRepository extends JpaRepository<Playlist, Integer> implements PlaylistRepository {

	@Override
	public Collection<Playlist> findAll() throws PersistenceException {
		return super.findAll("select p from Playlist p", Playlist.class);
	}

	@Override
	public Optional<Playlist> findById(Integer id) throws PersistenceException {
		return super.findById("select p from Playlist p where p.id = ?1", id, Playlist.class);
	}

	@Override
	public Playlist add(Playlist playlist) throws PersistenceException {
		return super.add(playlist);
	}

	@Override
	public Playlist merge(Playlist playlist) throws PersistenceException {
		return super.merge(playlist);
	}

	@Override
	public boolean remove(Integer id) throws PersistenceException {
		return super.remove("delete from Playlist p where p.id = ?1", id);
	}

}
