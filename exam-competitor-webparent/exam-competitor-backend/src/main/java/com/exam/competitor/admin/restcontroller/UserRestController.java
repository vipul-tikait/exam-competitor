package com.exam.competitor.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.service.UserService;

@RestController
public class UserRestController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/users/check_email")
	public String checkduplicateEmail( @Param("id") Integer id, @Param("email") String email) {
		return userService.isEmailUnique(id,email)?"OK":"Duplicated";
	}

}
