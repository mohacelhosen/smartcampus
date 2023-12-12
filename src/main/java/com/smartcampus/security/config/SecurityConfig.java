package com.smartcampus.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.smartcampus.security.jwt.JwtAuthFilter;
import com.smartcampus.security.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private JwtAuthFilter authFilter;

	// Password Encoder Bean
	@Bean
	public PasswordEncoder passwordEncoder() {
		logger.info("SecurityConfig::passwordEncoder, 🧠");
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		logger.info("SecurityConfig::authenticationProvider, 👨‍💻");
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		logger.info("SecurityConfig::authenticationManager, 🕵️");
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("SecurityConfig::SecurityFilterChain, 🗃️");
		DefaultSecurityFilterChain fillterChain = http.cors(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authRequest -> authRequest.requestMatchers("/v3/api-docs/**", "/swagger-ui/**",
						"/swagger-ui/index.html", "/swagger-ui.html", "/swagger-ui/index.html", "/webjars/**",
						"/api/v1/university/auth/**", "/api/v1/university/**", "/api/v1/university/classroom/**",
						"/api/v1/university/library-card/save", "/api/v1/university/student/register-1",
						"/api/v1/university/filemngt/photos/add", "/api/v1/university/department-course",
						"/api/v1/university/teacher-staff/section/all", "/api/v1/university/teacher-staff/registration",
						"/api/v1/university/teacher-staff/section/register",
						"/api/v1/university/teacher-staff/authority/account-activate").permitAll().anyRequest()
						.authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
		return fillterChain;
	}

}