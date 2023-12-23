package com.smartcampus.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smartcampus.security.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
//        String userEmail = null;
		String userId = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
//            userEmail = jwtService.extractUserEmail(token);
			userId = jwtService.extractUserAcademicID(token);
		}

		logger.info("JwtAuthFilter: Token: " + token);
//        logger.info("JwtAuthFilter: User Email: " + userEmail);
		logger.info("JwtAuthFilter: User userId: " + userId);

		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
			logger.info( "JwtAuthFilter::doFilterInternal,  Authorities: "+ userDetails.getAuthorities());
			if (jwtService.validateToken(token, userDetails)) {
				logger.info("JwtAuthFilter: Token validated successfully for user: " + userId);

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				System.out.println("Auth::"+authToken.getDetails().toString());

			} else {
				logger.info("JwtAuthFilter: Token validation failed for user: " + userId);
			}
		}
		filterChain.doFilter(request, response);
	}
}
