package com.javadev.organizer.security.jwt.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUserDetails implements UserDetails {

	private Long id;
    private String email;
    private String token;
    private Collection<? extends GrantedAuthority> authorities;


    public JwtUserDetails(Long id, String email, String token, List<GrantedAuthority> grantedAuthorities) {
    	this.id = id;
        this.email = email;
        this.token= token;
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
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
    
    public Long getId() {
    	return id;
    }

    public String getToken() {
        return token;
    }
}
