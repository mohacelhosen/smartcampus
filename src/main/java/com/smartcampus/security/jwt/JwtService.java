package com.smartcampus.security.jwt;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.smartcampus.exception.TokenValidationException;
import com.smartcampus.exception.UserNotFoundException;
import com.smartcampus.security.model.CustomUserDetails;
import com.smartcampus.security.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	Logger logger = LoggerFactory.getLogger(JwtService.class);
	public static final String SECRET = "79a9ef48bc06cb4b9c9ac9867a3197fb13a98e0684b478b97cf931b6afdb6ad2";
	@Autowired
	private UserRepository repository;

	// generate token
	public String generateToken(String userId) {
		Optional<CustomUserDetails> userEntity = repository.findByUserId(userId);
		if (userEntity.isPresent()) {
			CustomUserDetails user = userEntity.get();
			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

			Map<String, Object> claims = new HashMap<>();
			claims.put("authorities", authorities); // Add the roles information to the claims
			claims.put("userName", user.getFullName());
			claims.put("userId", user.getAcademicId());

			return createToken(claims, userId);
		} else {
			// Handle the case when the user does not exist in the database
			throw new UserNotFoundException("User not found for this ID:: " + userId);
		}
	}

	// create a token
	private String createToken(Map<String, Object> claims, String userAcademicID) {
		return Jwts.builder().setClaims(claims).setSubject(userAcademicID)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	// VERIFY SIGNATURE (secret key with base64 decode and return HMAC-SHA256)
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// extract user name/ email
//    public String extractUserEmail(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
	public String extractUserAcademicID(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// extract expiration time
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		try {
			final String username = extractUserAcademicID(token);

			// Check if the token's subject (userID) matches the UserDetails'
			if (!username.equals(userDetails.getUsername())) {
				return false;
			}
			// Check if the token is expired
			return !isTokenExpired(token);
			// Additional validation logic, if needed
			// Token is valid
		} catch (MalformedJwtException e) {
			// Log the error for debugging
			logger.error("Malformed JWT: " + e.getMessage());

			// Handle the malformed JWT exception by returning a custom error response
			throw new TokenValidationException("Malformed JWT: " + e.getMessage());
		} catch (Exception e) {
			// Log the error for debugging
			logger.error("Error validating token: " + e.getMessage());

			// Throw a custom exception or return a meaningful error response
			throw new TokenValidationException("Token validation failed: " + e.getMessage());
		}
	}

}
