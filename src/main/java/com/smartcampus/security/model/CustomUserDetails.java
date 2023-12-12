package com.smartcampus.security.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smartcampus.common.ModelLocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String userId;
	private String password;
	private String email;
	private String fullName;
	private String role;
	@JsonIgnore
	private String randomPassword;
	@JsonIgnore
	private ModelLocalDateTime lastPasswordResetDate;
	@JsonIgnore
	private ModelLocalDateTime accountCreationDateTime;
	@JsonIgnore
	private List<String> authorities;
	@JsonIgnore
	private boolean accountNonExpired;
	@JsonIgnore
	private boolean accountNonLocked;
	@JsonIgnore
	private boolean credentialsNonExpired;
	@JsonIgnore
	private boolean enabled;

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
