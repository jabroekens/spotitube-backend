package com.github.jabroekens.spotitube.persistence.impl_jpa;

import com.github.jabroekens.spotitube.model.track.Track;
import com.github.jabroekens.spotitube.persistence.api.PersistenceException;
import com.github.jabroekens.spotitube.persistence.api.TrackRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
public class JpaTrackRepository extends JpaRepository<Track, Integer> implements TrackRepository {

	@Override
	public Collection<Track> findAll() throws PersistenceException {
		return super.findAll("select t from Track t", Track.class);
	}

	@Override
	public Optional<Track> findById(Integer trackId) throws PersistenceException {
		return super.findById("select t from Track t where t.id = ?1", trackId, Track.class);
	}

	@Override
	public Track add(Track track) throws PersistenceException {
		return super.add(track);
	}

	@Override
	public Track merge(Track track) throws PersistenceException {
		return super.merge(track);
	}

	@Override
	public boolean remove(Integer id) throws PersistenceException {
		return super.remove("delete from Track t where t.id = ?1", id);
	}

}
