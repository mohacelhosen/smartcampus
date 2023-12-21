package com.smartcampus.security.model;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
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
	@Serial
    private static final long serialVersionUID = 1L;
	@Id
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id;
	private String academicId;
	private String password;
	private String email;
	private String fullName;
	private String role;
	@JsonIgnore
	private List<String> previousPassword;
	@JsonIgnore
	private ModelLocalDateTime lastPasswordResetDate;
	@JsonIgnore
	private ModelLocalDateTime accountCreationDateTime;
	@JsonIgnore
	private List<String> authorities;
	@JsonIgnore
	private boolean accountNonExpired = true;
	@JsonIgnore
	private boolean accountNonLocked = true;
	@JsonIgnore
	private boolean credentialsNonExpired = true;
	@JsonIgnore
	private boolean enabled = true;

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	@Override
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return this.academicId;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}
}
