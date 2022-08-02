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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.exception.MainExamNotFoundException;
import com.exam.competitor.admin.service.MainExamService;
import com.exam.competitor.admin.service.MainExamTypeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainExamController {
	
	@Autowired MainExamService mainExamService;
	@Autowired MainExamTypeService mainExamTypeService;

	@GetMapping("/mainExams")
	public String getAllUserList(Model model) {
		
		return getAllUserListByPage(1, model, "name", "asc", null);//"users";
	}
	
	@GetMapping("/mainExams/page/{pageNum}")
	public String getAllUserListByPage(@PathVariable(name ="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<MainExam> page = mainExamService.listByPage(pageNum,sortField,sortDir,keyword);
		List<MainExam> lstMainExam = page.getContent();
		
		long startCount = (pageNum-1) * mainExamService.MAIN_EXAM_TYPE_PER_PAGE + 1;
		long endCount = startCount + mainExamService.MAIN_EXAM_TYPE_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String revSortDir = sortDir.equals("asc")?"desc":"asc"; 
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("mainExamList", lstMainExam);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		
		return "exams/main_exams";
	}
	
	
	@GetMapping("/mainExams/new")
	public String createNewUser(Model model) {
		
		List<MainExamType> mainExamTypeList  = mainExamTypeService.findAllMainExamTypeList();
		
		MainExam mainExam = new MainExam(); 
		model.addAttribute("mainExam", mainExam);
		model.addAttribute("mainExamTypeList", mainExamTypeList);
		model.addAttribute("pageTitle", "Create New MainExam");
		return "exams/main_exam_form";
	}
	
	@PostMapping("/mainExams/save")
	public String saveUser(MainExam mainExam, RedirectAttributes redirectAttributes
			) throws IOException {

		mainExamService.save(mainExam);
		
		redirectAttributes.addFlashAttribute("message", "MainExam has been saved successfully.");
		
		String nameFirstPart = mainExam.getName();
		return "redirect:/mainExams/page/1?sortField=name&sortDir=asc&keyword="+nameFirstPart;
	}
	
	@GetMapping("/mainExams/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			List<MainExamType> mainExamTypeList  = mainExamTypeService.findAllMainExamTypeList();
			
			MainExam MainExam = mainExamService.getMainExamById(id);
			model.addAttribute("mainExam", MainExam);
			model.addAttribute("mainExamTypeList", mainExamTypeList);
			model.addAttribute("pageTitle", "Edit Main Exam  (ID - "+id+")");
			return "exams/main_exam_form";
		} catch (MainExamNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/main_exam_form";
		}
		
	}
	
	@GetMapping("/mainExams/delete/{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("message", 
					"Ths MainExam with id "+id+" has been deleted successfully.");
			mainExamService.deleteUser(id);
		} catch (MainExamNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/mainExams";
		
	}
	
	/*
	 * @GetMapping("/mainExams/getMainExams") public String getStates(@RequestParam
	 * Integer id, Model model) { List<MainExam> listMainExam =
	 * mainExamService.getMainExamByMainExamType(id);
	 * model.addAttribute("listMainExam", listMainExam); return
	 * "redirect:/exams/exam_form"; }
	 */
	
	/*
	 * @GetMapping("/users/export/csv") public void exportToCsv(HttpServletResponse
	 * httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = mainExamService.findAllUserList(); UserCsvExpoter
	 * exporter = new UserCsvExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/users/export/excel") public void
	 * exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = mainExamService.findAllUserList(); UserExcelExpoter
	 * exporter = new UserExcelExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/users/export/pdf") public void exportToPdf(HttpServletResponse
	 * httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = mainExamService.findAllUserList(); UserPdfExpoter
	 * exporter = new UserPdfExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 */
}
