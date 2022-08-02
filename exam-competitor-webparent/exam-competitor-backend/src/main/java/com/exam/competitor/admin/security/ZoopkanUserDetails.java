package com.exam.competitor.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.exam.competitor.admin.common.entity.Role;
import com.exam.competitor.admin.common.entity.User;

public class ZoopkanUserDetails implements UserDetails {
	
	private User user;

	
	
	public ZoopkanUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		Set<Role> roles = user.getRoles();
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (Role role: roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
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
		return user.isEnabled();
	}
	
	public String getFullname() {
		return this.user.getFirstName()+" "+this.user.getLastName();
	}
	
	public String getPhotosImagePath() {
		if (user.getId() == null || user.getPhotos() == null )  return "/images/thumbnail-default.png";
		
		return "/user-photos/"+ this.user.getId() +"/"+this.user.getPhotos();
	}

	public void setFirstName(String fString) {
		this.user.setFirstName(fString);
	}
	public void setLastName(String lastName) {
		this.user.setLastName(lastName);
	}
	
	public void setId(Integer id) {
		this.user.setId(id);
	}
	
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
}
