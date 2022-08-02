package com.exam.competitor.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.repo.UserRepository;
import com.exam.competitor.admin.security.ZoopkanUserDetails;

public class ZoopkanUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		User user  = userRepo.getUserByEmail(email);
		if (user != null) {
			return new ZoopkanUserDetails(user);
		} else {
			throw new UsernameNotFoundException("User not found with email:"+email);
		}
	}

}
