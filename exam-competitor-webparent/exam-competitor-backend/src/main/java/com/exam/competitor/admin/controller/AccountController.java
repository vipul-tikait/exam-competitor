package com.exam.competitor.admin.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.export.FileUploadUtil;
import com.exam.competitor.admin.security.ZoopkanUserDetails;
import com.exam.competitor.admin.service.UserService;

@Controller
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ZoopkanUserDetails loggedUser, Model model) {
		
		String email = loggedUser.getUsername();
		
		User user = userService.getUserByEmail(email);
		user.setPassword("");
		model.addAttribute("user", user);
		
		return "users/account_form";
	}

	@PostMapping("/account/update")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ZoopkanUserDetails loggedUser,
			@RequestParam("profileImage") MultipartFile multipartFile) throws IOException {

		if (!(multipartFile.isEmpty())) {
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename()).replaceAll("\\s","");
			
			user.setPhotos(fileName);
			
			User savedUser = userService.updateAccount(user);
					
			String uploadDir = "user-photos/"+savedUser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		}else {
			if (user.getPhotos() == null || user.getPhotos().isEmpty()) 
				user.setPhotos(null);
			userService.updateAccount(user);
		}
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		redirectAttributes.addFlashAttribute("message", "Your account details has been updated");
		
		return "redirect:/account";
	}
}

