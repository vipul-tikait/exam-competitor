package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.exception.MainExamTypeNotFoundException;
import com.exam.competitor.admin.service.MainExamTypeService;

@Controller
public class MainExamTypeController {
	
	@Autowired
	MainExamTypeService mainExamTypeService;

	@GetMapping("/mainExamTypes")
	public String getAllUserList(Model model) {
		
		return getAllUserListByPage(1, model, "name", "asc", null);//"users";
	}
	
	@GetMapping("/mainExamTypes/page/{pageNum}")
	public String getAllUserListByPage(@PathVariable(name ="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<MainExamType> page = mainExamTypeService.listByPage(pageNum,sortField,sortDir,keyword);
		List<MainExamType> lstMainExamType = page.getContent();
		
		long startCount = (pageNum-1) * mainExamTypeService.MAIN_EXAM_TYPE_PER_PAGE + 1;
		long endCount = startCount + mainExamTypeService.MAIN_EXAM_TYPE_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String revSortDir = sortDir.equals("asc")?"desc":"asc"; 
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("mainExamTypeList", lstMainExamType);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		
		return "exams/main_exam_types";
	}
	
	
	@GetMapping("/mainExamTypes/new")
	public String createNewUser(Model model) {
		
		MainExamType mainExamType = new MainExamType(); 
		model.addAttribute("mainExamType", mainExamType);
		model.addAttribute("pageTitle", "Create New MainExamType");
		return "exams/main_exam_type_form";
	}
	
	@PostMapping("/mainExamTypes/save")
	public String saveUser(MainExamType user, RedirectAttributes redirectAttributes) throws IOException {

		mainExamTypeService.save(user);
		
		redirectAttributes.addFlashAttribute("message", "MainExamType has been saved successfully.");
		
		String nameFirstPart = user.getName();
		return "redirect:/mainExamTypes/page/1?sortField=name&sortDir=asc&keyword="+nameFirstPart;
	}
	
	@GetMapping("/mainExamTypes/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			MainExamType MainExamType = mainExamTypeService.getMainExamTypeById(id);
			model.addAttribute("mainExamType", MainExamType);
			model.addAttribute("pageTitle", "Edit Main Exam Type (ID - "+id+")");
			return "exams/main_exam_type_form";
		} catch (MainExamTypeNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/main_exam_type_form";
		}
		
	}
	
	@GetMapping("/mainExamTypes/delete/{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("message", 
					"Ths MainExamType with id "+id+" has been deleted successfully.");
			mainExamTypeService.deleteUser(id);
		} catch (MainExamTypeNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/mainExamTypes";
		
	}
	
	
	/*
	 * @GetMapping("/users/export/csv") public void exportToCsv(HttpServletResponse
	 * httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = mainExamTypeService.findAllUserList(); UserCsvExpoter
	 * exporter = new UserCsvExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/users/export/excel") public void
	 * exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = mainExamTypeService.findAllUserList(); UserExcelExpoter
	 * exporter = new UserExcelExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/users/export/pdf") public void exportToPdf(HttpServletResponse
	 * httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = mainExamTypeService.findAllUserList(); UserPdfExpoter
	 * exporter = new UserPdfExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 */
}
