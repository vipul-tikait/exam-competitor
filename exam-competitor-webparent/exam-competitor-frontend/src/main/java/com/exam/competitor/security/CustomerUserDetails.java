package com.exam.competitor.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.exam.competitor.admin.common.entity.Customer;


public class CustomerUserDetails implements UserDetails {
	private Customer customer;
	
	public CustomerUserDetails(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
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
		return customer.isEnabled();
	}

	public String getFullName() {
		return customer.getFirstName() + " " + customer.getLastName();
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
	
	public String getPhotosImagePath() {
		if (customer.getId() == null || customer.getPhotos() == null )  return "/images/thumbnail-default.png";
		
		return "/student-photos/"+ this.customer.getId() +"/"+this.customer.getPhotos();
	}
}
