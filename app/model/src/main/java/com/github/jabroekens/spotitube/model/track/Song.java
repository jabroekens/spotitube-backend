package com.github.jabroekens.spotitube.model.track;

import com.github.jabroekens.spotitube.model.NotNullAndValid;
import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class Song extends Track {

	private Album album;

	/**
	 * Creates a song.
	 *
	 * @param title            the title of the song.
	 * @param performer        the performer of the song.
	 * @param duration         the duration of the song in seconds.
	 * @param offlineAvailable if the track is available for offline use.
	 * @param album            the album the song is a part of.
	 */
	public Song(
	  String title,
	  Performer performer,
	  int duration,
	  boolean offlineAvailable,
	  Album album
	) {
		super(title, performer, duration, offlineAvailable);
		this.album = album;
	}

	/**
	 * Returns a deep copy of {@code song}.
	 */
	public Song(Song song) {
		super(song);
		this.album = song.album != null ? new Album(song.album) : null;
	}

	@NotNullAndValid
	public Album getAlbum() {
		return album;
	}

	@Override
	public Track copy(Track track) {
		return track instanceof Song s
		  ? new Song(s)
		  : track.copy(track);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Song song)) return false;
		if (!super.equals(o)) return false;
		return Objects.equals(getAlbum(), song.getAlbum());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getAlbum());
	}

}
