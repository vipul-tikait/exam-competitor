package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.Role;
import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.exception.UserNotFoundException;
import com.exam.competitor.admin.export.FileUploadUtil;
import com.exam.competitor.admin.export.UserCsvExpoter;
import com.exam.competitor.admin.export.UserExcelExpoter;
import com.exam.competitor.admin.export.UserPdfExpoter;
import com.exam.competitor.admin.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;

	@GetMapping("/users")
	public String getAllUserList(Model model) {
		
		model.addAttribute("userList", userService.findAllUserList());
		
		return getAllUserListByPage(1, model, "firstName", "asc", null);//"users";
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String getAllUserListByPage(@PathVariable(name ="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<User> page = userService.listByPage(pageNum,sortField,sortDir,keyword);
		List<User> lstUser = page.getContent();
		
		long startCount = (pageNum-1) * userService.USER_PER_PAGE + 1;
		long endCount = startCount + userService.USER_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String revSortDir = sortDir.equals("asc")?"desc":"asc"; 
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("userList", lstUser);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		
		return "users/users";
	}
	
	
	@GetMapping("/users/new")
	public String createNewUser(Model model) {
		
		List<Role> listRoles = userService.findAllRolles();	
		System.out.println(listRoles);
		User user = new User(); 
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("profileImage") MultipartFile multipartFile) throws IOException {

		if (!(multipartFile.isEmpty())) {
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename()).replaceAll("\\s","");
			
			user.setPhotos(fileName);
			
			User savedUser = userService.save(user);
					
			String uploadDir = "user-photos/"+savedUser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if (user.getPhotos() == null || user.getPhotos().isEmpty()) 
				user.setPhotos(null);
			userService.save(user);
		}
		
		redirectAttributes.addFlashAttribute("message", "User has been saved successfully.");
		
		String emailFirstPart = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc&keyword="+emailFirstPart;
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			List<Role> listRoles = userService.findAllRolles();	
			User user = userService.getUserById(id);
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Edit User (ID - "+id+")");
			return "users/user_form";
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
		
	}
	
	@GetMapping("/users/delete/{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("message", 
					"Ths User with id "+id+" has been deleted successfully.");
			userService.deleteUser(id);
		} catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
		
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String deletUser(@PathVariable(name = "id") Integer id, 
			@PathVariable("status") Boolean status,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		userService.updateEnabledStatus(id, status);
		
		String state = status?"enabled":"disabled";
		String msg = "The user id : "+id+" has been "+state;
		redirectAttributes.addFlashAttribute("message", msg);
				
		return "redirect:/users";
		
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCsv(HttpServletResponse httpServletResponse) throws IOException {
		
		List<User> listAll = userService.findAllUserList();
		UserCsvExpoter exporter = new UserCsvExpoter();
		exporter.export(listAll, httpServletResponse);
		
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
		
		List<User> listAll = userService.findAllUserList();
		UserExcelExpoter exporter = new UserExcelExpoter();
		exporter.export(listAll, httpServletResponse);
		
	}
	@GetMapping("/users/export/pdf")
	public void exportToPdf(HttpServletResponse httpServletResponse) throws IOException {
		
		List<User> listAll = userService.findAllUserList();
		UserPdfExpoter exporter = new UserPdfExpoter();
		exporter.export(listAll, httpServletResponse);
		
	}
	
	
}
