package com.exam.competitor.admin.service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.Role;
import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.exception.UserNotFoundException;
import com.exam.competitor.admin.repo.RoleRepository;
import com.exam.competitor.admin.repo.UserRepository;


@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public static final int USER_PER_PAGE = 10;
	
	public List<User> findAllUserList(){
		
		return (List<User>) userRepo.findAll();
	}
	
	public User getUserByEmail(String email) {
		return userRepo.getUserByEmail(email);
	}
	
	public List<Role> findAllRolles(){
		return (List<Role>) roleRepo.findAll();
	}
	
	public Page<User> listByPage(int paneNum,String sortField, String sortDir, String keyword){
	
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		org.springframework.data.domain.Pageable pageable = PageRequest.of(paneNum-1, USER_PER_PAGE,sort);
		
		if (keyword != null) {
			return userRepo.findAll(keyword, pageable);	
		}
		return userRepo.findAll(pageable);
	}


	public User save(User user) {
		
		boolean isUpdatingUser = (user.getId() != null);
		
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		}else {
			encodePassword(user);
		}
			
		return userRepo.save(user);
	}
	
	public User updateAccount(User userForm) {
		User userInDB = userRepo.findById(userForm.getId()).get();
		
		if (!userForm.getPassword().isEmpty()) {
			userInDB.setPassword(userForm.getPassword());
			encodePassword(userInDB);
		}
		
		if (userForm.getPhotos() != null) {
			userInDB.setPhotos(userForm.getPhotos());
		}
		
		userInDB.setFirstName(userForm.getFirstName());
		userInDB.setLastName(userForm.getLastName());
		return userRepo.save(userInDB);
	}
	
	private void encodePassword(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if (userByEmail == null)  return true;
		
		boolean isCreateNew = (id == null);
		
		if(isCreateNew) {
			if (userByEmail != null) return false;
		}else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}


	public User getUserById(Integer id) throws UserNotFoundException {
		
		try {
			return userRepo.findById(id).get();
		}catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user by ID:"+id);
		}
	}
	
	public void deleteUser(Integer id) throws UserNotFoundException {
		
		try {
			userRepo.deleteById(id);
		}catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user by ID:"+id);
		}
	}
	
	public void updateEnabledStatus(Integer id, boolean status) {
		userRepo.updateEnabledStatus(id, status);
	}
	
	public List<User> getModaratorList(){
		Role role = roleRepo.findById(6).get();
		return userRepo.findByRoles(role);
	}
	
	
}
