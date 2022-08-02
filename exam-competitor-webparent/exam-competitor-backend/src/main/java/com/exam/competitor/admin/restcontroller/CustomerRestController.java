package com.exam.competitor.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.service.CustomerService;

@RestController
public class CustomerRestController {
	@Autowired
	private CustomerService service;
	
	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(Integer id, String email) {
		if (service.isEmailUnique(id, email)) {
			return "OK";
		} else {
			return "Duplicated";
		}
	}
}
