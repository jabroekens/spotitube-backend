package com.github.jabroekens.spotitube.persistence.impl;

public class JdbcVideoRepository extends JdbcTrackRepository {

	private static final String FIND_ALL_VIDEOS = buildFindAllQuery("""
      SELECT
      v.playCount        AS Track_playCount,
      v.publicationDate  AS Track_publicationDate,
      v.description      AS Track_description,
      """,
	  "LEFT JOIN Video v ON t.id = v.id"
	);

	private static final String INSERT_VIDEO = """
      INSERT INTO Video (id, playCount, publicationDate, description)
      VALUES (?, ?, ?, ?)
      """;

	private static final String UPDATE_VIDEO = """
      UPDATE Video SET
      playCount=?, publicationDate=?, description=?
      WHERE id=?
      """;

	@Override
	public String findAllQueryString() {
		return FIND_ALL_VIDEOS;
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
