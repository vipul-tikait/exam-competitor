package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.exception.ExamNotFoundException;
import com.exam.competitor.admin.service.ExamService;
import com.exam.competitor.admin.service.MainExamService;
import com.exam.competitor.admin.service.MainExamTypeService;


@Controller
public class ExamController {
	
	@Autowired ExamService examService;
	@Autowired MainExamService mainExamService;
	@Autowired MainExamTypeService mainExamTypeService;

	@GetMapping("/exams")
	public String getAllExamList(Model model) {
		
		return getAllExamListByPage(1, model, "name", "asc", null);//"users";
	}
	
	@GetMapping("/exams/page/{pageNum}")
	public String getAllExamListByPage(@PathVariable(name ="pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<Exam> page = examService.listByPage(pageNum,sortField,sortDir,keyword);
		List<Exam> lstExam = page.getContent();
		
		long startCount = (pageNum-1) * examService.EXAM_PER_PAGE + 1;
		long endCount = startCount + examService.EXAM_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String revSortDir = sortDir.equals("asc")?"desc":"asc"; 
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("examList", lstExam);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		
		return "exams/exam";
	}
	
	
	@GetMapping("/exams/new")
	public String createNewUser(Model model) {
		
		List<MainExamType> mainExamTypeList  = mainExamTypeService.findAllMainExamTypeList();
		List<MainExam> mainExamList = new ArrayList<MainExam>();
		
		Exam exam = new Exam(); 
		model.addAttribute("exam", exam);
		model.addAttribute("mainExamTypeList", mainExamTypeList);
		model.addAttribute("mainExamList", mainExamList);
		model.addAttribute("pageTitle", "Create New Exam");
		return "exams/exam_form";
	}
	
	@PostMapping("/exams/save")
	public String saveUser(Exam exam, RedirectAttributes redirectAttributes) throws IOException {

		examService.save(exam);
		
		redirectAttributes.addFlashAttribute("message", "Exam has been saved successfully.");
		
		String nameFirstPart = exam.getName();
		return "redirect:/exams/page/1?sortField=name&sortDir=asc&keyword="+nameFirstPart;
	}
	
	@GetMapping("/exams/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Exam exam = examService.getExamById(id);
			
			List<MainExamType> mainExamTypeList  = mainExamTypeService.findAllMainExamTypeList();
			List<MainExam> mainExamList = mainExamService.getMainExamByMainExamType(exam.getMainExam().getMainExamType().getId());
			
			model.addAttribute("exam", exam);
			model.addAttribute("mainExamTypeList", mainExamTypeList);
			model.addAttribute("mainExamList", mainExamList);
			model.addAttribute("pageTitle", "Edit  Exam  (ID - "+id+")");
			return "exams/exam_form";
		} catch (ExamNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/exam_form";
		}
		
	}
	
	@GetMapping("/exams/delete/{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("message", 
					"Ths Exam with id "+id+" has been deleted successfully.");
			examService.deleteUser(id);
		} catch (ExamNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/exams";
		
	}
	
	
	
	/*
	 * @GetMapping("/users/export/csv") public void exportToCsv(HttpServletResponse
	 * httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = ExamService.findAllUserList(); UserCsvExpoter
	 * exporter = new UserCsvExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/users/export/excel") public void
	 * exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = ExamService.findAllUserList(); UserExcelExpoter
	 * exporter = new UserExcelExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/users/export/pdf") public void exportToPdf(HttpServletResponse
	 * httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = ExamService.findAllUserList(); UserPdfExpoter
	 * exporter = new UserPdfExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 */
}
