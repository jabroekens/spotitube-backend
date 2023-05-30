package com.github.jabroekens.spotitube.app.config.security;

public enum Right {
	/**
	 * Allows a user to view other users' playlists.
	 */
	ViewOtherPlaylist,

	/**
	 * Allows a user to create a playlist that will belong to them.
	 */
	CreatePlaylist,

	/**
	 * Allows a user to modify their own playlists (without changing ownership).
	 */
	ModifyPlaylist,

	/**
	 * Allows a user to delete their own playlist.
	 */
	DeletePlaylist,

	/**
	 * Allows a user to modify other users' playlists (without changing ownership).
	 */
	ModifyOtherPlaylist,

	/**
	 * Allows a user to delete other users' playlists.
	 */
	DeleteOtherPlaylist,

	/**
	 * Allows a user to modify other performers' tracks (without changing ownership).
	 */
	ModifyOtherTrack,

	/**
	 * Allows a user to delete other performers' tracks.
	 */
	DeleteOtherTrack,

	/**
	 * Allows a user to access administrator-only resources.
	 */
	Admin
}
