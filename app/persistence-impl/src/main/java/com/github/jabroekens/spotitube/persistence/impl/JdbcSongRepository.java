package com.github.jabroekens.spotitube.persistence.impl;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JdbcSongRepository extends JdbcTrackRepository {

	private static final String FIND_ALL_SONGS = buildFindAllQuery(
      "a.name AS Album_name",
	  """
      LEFT JOIN Song s ON t.id = s.id
      LEFT JOIN Album a on s.album = a.name
      """);

	private static final String INSERT_SONG = """
      INSERT INTO Song (id, album) VALUES (?, ?)
      """;

	private static final String UPDATE_SONG = """
      UPDATE Song SET album=?, WHERE id=?
      """;

	@Override
	public String findAllQueryString() {
		return FIND_ALL_SONGS;
	}

	@Override
	public String insertQueryString() {
		return null;
	}

	@Override
	public Object[] insertParams() {
		return new Object[0];
	}

	@Override
	public String updateQueryString() {
		return null;
	}

	@Override
	public Object[] updateParams() {
		return new Object[0];
	}

}
