package com.github.jabroekens.spotitube.app.config.security;

import com.github.jabroekens.spotitube.model.user.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public final class JwtHelper {

	private static final String ISSUER = "Spotitube";
	private static final String AUDIENCE = "Spotitube";
	private static final Duration TOKEN_DURATION = Duration.ofHours(3);

	private static final KeyPair JWT_KEY_PAIR = Keys.keyPairFor(SignatureAlgorithm.ES512);

	private JwtHelper() {
		throw new UnsupportedOperationException("Cannot instantiate util class");
	}

	public static String issueToken(User user) {
		var currentTime = Instant.now();
		var currentDate = java.util.Date.from(currentTime);
		var expirationDate = java.util.Date.from(currentTime.plus(TOKEN_DURATION));

		return Jwts.builder()
		  .setIssuer(ISSUER)
		  .setSubject(user.getId())
		  .setAudience(AUDIENCE)
		  .setIssuedAt(currentDate)
		  .setNotBefore(currentDate)
		  .setExpiration(expirationDate)
		  .setId(UUID.randomUUID().toString())
		  .signWith(JWT_KEY_PAIR.getPrivate())
		  .compact();
	}

	public static void validateToken(String token) throws JwtException {
		Jwts.parserBuilder()
		  .setSigningKey(JWT_KEY_PAIR.getPublic())
		  .build()
		  .parseClaimsJws(token);
	}

}
