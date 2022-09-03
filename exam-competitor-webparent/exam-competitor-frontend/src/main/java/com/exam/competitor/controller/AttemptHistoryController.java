package com.exam.competitor.controller;

import java.io.IOException;
import java.security.Principal;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.common.entity.course.AttemptHistory;
import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.exception.AttemptHistoryNotFoundException;
import com.exam.competitor.exception.CourseNotFoundException;
import com.exam.competitor.exception.QuestionNotFoundException;
import com.exam.competitor.export.FileUploadUtil;
import com.exam.competitor.service.AttemptHistoryService;
import com.exam.competitor.service.CourseService;
import com.exam.competitor.service.CustomerService;
import com.exam.competitor.service.QuestionService;

@Controller
public class AttemptHistoryController {

	@Autowired private CourseService courseService;
	@Autowired private CustomerService customerService;
	@Autowired private QuestionService questionService;
	@Autowired private AttemptHistoryService attemptHistoryService;
	
	@GetMapping("/attempts")
	public String listFirstPage(Model model,Principal principal) {
		return getAllAttemptListByPage(1, model, "name", "asc", null,principal);
	}
	@GetMapping("/attempts/page/{pageNum}")
	public String getAllAttemptListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,Principal principal) {

		Page<AttemptHistory> page = attemptHistoryService.listByPage(pageNum, sortField, sortDir, keyword);
		List<AttemptHistory> lstCourse = page.getContent();

		long startCount = (pageNum - 1) * attemptHistoryService.COURSE_PER_PAGE + 1;
		long endCount = startCount + attemptHistoryService.COURSE_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		Customer cust = customerService.getCustomerByEmail(principal.getName());
		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("attemptList", lstCourse);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("cust", cust);
		model.addAttribute("custId", cust.getId());
		return "courses/attempts_list";

	}




	@GetMapping("/attempts/{id}/enabled/{status}")
	public String enableCourse(@PathVariable(name = "id") Integer id, @PathVariable("status") Boolean status,
			Model model, RedirectAttributes redirectAttributes) {

		attemptHistoryService.updateEnabledStatus(id, status);

		String state = status ? "enabled" : "disabled";
		String msg = "The attempt id : " + id + " has been " + state;
		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/attempts";

	}

	@PostMapping("/attempts/new")
	public String newAttempt(@RequestParam(name = "questionId") Integer questionId,
			@RequestParam(name = "custId") Integer custId,
			@RequestParam(name = "courseId") Integer courseId,
			Model model, RedirectAttributes redirectAttributes) throws AttemptHistoryNotFoundException, CourseNotFoundException, QuestionNotFoundException {
		AttemptHistory attemptHistory = new AttemptHistory();

		Customer cust = customerService.getCustomerById(custId).get();
		Course course = courseService.getCourseById(courseId);
		Question question  = questionService.getQuestionById(questionId);
		attemptHistory.setCustomer(cust);
		attemptHistory.setCourse(course);
		attemptHistory.setQuestion(question);

		
		model.addAttribute("attemptHistory", attemptHistory);
		model.addAttribute("custId", custId);
		model.addAttribute("courseId", courseId);
		model.addAttribute("questionId", questionId);
		
		model.addAttribute("pageTitle", "Upload your answerbook for TEST(ID - " + questionId + ")");
		return "attempts/attempt_form";

	}
	
	@GetMapping("/attempts/edit/{id}")
	public String editAttempt(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) throws AttemptHistoryNotFoundException {
		AttemptHistory attemptHistory = attemptHistoryService.getAttemptById(id).get();
		
		model.addAttribute("attemptHistory", attemptHistory);
		model.addAttribute("custId", attemptHistory.getCustomer().getId());
		model.addAttribute("courseId", attemptHistory.getCourse().getId());
		model.addAttribute("questionId", attemptHistory.getQuestion().getId());
		
		model.addAttribute("pageTitle", "Edit attemptHistory (ID - " + id + ")");
		return "attempts/attempt_form";

	}
	
	@GetMapping("/re-attempts/{attemptId}")
	public String reAttempt(@PathVariable(name = "attemptId") Integer attemptId, Model model,
			RedirectAttributes redirectAttributes,@RequestParam("answeredPdf") MultipartFile multipartFile) throws AttemptHistoryNotFoundException, IOException {
		AttemptHistory attemptHistory = attemptHistoryService.getAttemptById(attemptId).get();
		attemptHistory.setStatus(null);
		if (!(multipartFile.isEmpty())) {
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename()).replaceAll("\\s","");
			
			attemptHistory.setAnsweredPdfName(fileName);
			
			AttemptHistory savedAttempt = attemptHistoryService.save(attemptHistory);
					
			String uploadDir = "../attempt-pdf/"+"course-"+attemptHistory.getCourse().getId()+"/"
			+"cust-"+attemptHistory.getCustomer().getId()+"/"+"que-"+attemptHistory.getQuestion().getId()+"/"
			+"attempt-"+savedAttempt.getId()+"/answerbook/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if (attemptHistory.getAnsweredPdfName() == null || attemptHistory.getAnsweredPdfName().isEmpty()) 
				attemptHistory.setAnsweredPdfName(null);
			attemptHistoryService.save(attemptHistory);
		}

		
		redirectAttributes.addFlashAttribute("message", "Answer book has been resubmit successfully.");

		
		model.addAttribute("attemptHistory", attemptHistory);
		model.addAttribute("pageTitle", "Edit attemptHistory (ID - " + attemptId + ")");
		return "attempts/attempt_form_details";

	}
	
	@GetMapping("/attempts/details/{id}")
	public String detailsAttempt(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) throws AttemptHistoryNotFoundException {
		AttemptHistory attemptHistory = attemptHistoryService.getAttemptById(id).get();
		
		model.addAttribute("attemptHistory", attemptHistory);
		model.addAttribute("pageTitle", "Edit attemptHistory (ID - " + id + ")");
		return "attempts/attempt_form_details";

	}

	@PostMapping("attempts/save")
	public String saveUser(AttemptHistory attemptHistory, @RequestParam(name = "custId") Integer custId,
			@RequestParam(name = "courseId") Integer courseId,@RequestParam(name = "questionId") Integer questionId,
			RedirectAttributes redirectAttributes,@RequestParam("answeredPdf") MultipartFile multipartFile) throws IOException, CourseNotFoundException, QuestionNotFoundException {
		
		Customer cust = customerService.getCustomerById(custId).get();
		Course course = courseService.getCourseById(courseId);
		Question question  = questionService.getQuestionById(questionId);
		attemptHistory.setCustomer(cust);
		attemptHistory.setCourse(course);
		attemptHistory.setQuestion(question);

		if (!(multipartFile.isEmpty())) {
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename()).replaceAll("\\s","");
			
			attemptHistory.setAnsweredPdfName(fileName);
			
			AttemptHistory savedAttempt = attemptHistoryService.save(attemptHistory);
					
			String uploadDir = "../attempt-pdf/"+"course-"+courseId+"/"+"cust-"+custId+"/"+"que-"+questionId+"/"
			+"attempt-"+savedAttempt.getId()+"/answerbook/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if (attemptHistory.getAnsweredPdfName() == null || attemptHistory.getAnsweredPdfName().isEmpty()) 
				attemptHistory.setAnsweredPdfName(null);
			attemptHistoryService.save(attemptHistory);
		}

		
		redirectAttributes.addFlashAttribute("message", "Answer book has been saved successfully.");

		// return "redirect:/attempts/page/1?sortField=id&sortDir=asc&keyword="+name;
		return "redirect:/questions-sets/view/"+courseId;
	}


}
