package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.exam.competitor.admin.common.entity.Role;
import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.common.entity.course.AttemptHistory;
import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.admin.exception.AttemptHistoryNotFoundException;
import com.exam.competitor.admin.exception.CourseNotFoundException;
import com.exam.competitor.admin.exception.CustomerNotFoundException;
import com.exam.competitor.admin.exception.QuestionNotFoundException;
import com.exam.competitor.admin.export.FileUploadUtil;
import com.exam.competitor.admin.service.AttemptHistoryService;
import com.exam.competitor.admin.service.CourseService;
import com.exam.competitor.admin.service.CustomerService;
import com.exam.competitor.admin.service.QuestionService;
import com.exam.competitor.admin.service.UserService;

@Controller
public class AttemptHistoryController {

	@Autowired private CourseService courseService;
	@Autowired private CustomerService customerService;
	@Autowired private QuestionService questionService;
	@Autowired private AttemptHistoryService attemptHistoryService;
	@Autowired private UserService userService;
	
	@GetMapping("/attempts")
	public String listFirstPage(Model model,Principal principal) {
		return getAllAttemptListByPage(1, model, "id", "asc", null,principal);
	}
	@GetMapping("/attempts/page/{pageNum}")
	public String getAllAttemptListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,Principal principal) {

		boolean isAdmin = false;
		Set<Role> roleList = new HashSet<>();
		List<User> moderatorList = new ArrayList<>();
		Page<AttemptHistory> page = null;
		User loggedInUser = userService.getUserByEmail(principal.getName());
		if (loggedInUser.hasRole("Admin")) {
			moderatorList = userService.getModaratorList();
			page = attemptHistoryService.listByPage(pageNum, sortField, sortDir, keyword);
		}else {
			moderatorList.add(loggedInUser);
			page = attemptHistoryService.listByPage(pageNum, sortField, sortDir, keyword,loggedInUser);
		}
		
		List<AttemptHistory> lstCourse = page.getContent();

		long startCount = (pageNum - 1) * attemptHistoryService.COURSE_PER_PAGE + 1;
		long endCount = startCount + attemptHistoryService.COURSE_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		
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
		model.addAttribute("moderatorList", moderatorList);
		model.addAttribute("isAdmin", isAdmin);
		
		return "attempts/attempts_list";

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

	@GetMapping("/attempts/{id}/status/{stage}")
	public String enableCourse(@PathVariable(name = "id") Integer id, @PathVariable("stage") String stage,
			Model model, RedirectAttributes redirectAttributes) {

		attemptHistoryService.updateAttemptStatus(id, stage);

		String state = stage=="accepted" ? "accepted" : "rejected";
		String msg = "The attempt id : " + id + " has been " + state;
		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/attempts";

	}

	@PostMapping("/attempts/new")
	public String newAttempt(@RequestParam(name = "questionId") Integer questionId,
			@RequestParam(name = "custId") Integer custId,
			@RequestParam(name = "courseId") Integer courseId,
			Model model, RedirectAttributes redirectAttributes) throws AttemptHistoryNotFoundException, CourseNotFoundException, QuestionNotFoundException, CustomerNotFoundException {
		AttemptHistory attemptHistory = new AttemptHistory();

		Customer cust = customerService.get(custId);
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
		model.addAttribute("pageTitle", "Edit attemptHistory (ID - " + id + ")");
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

	@PostMapping("/attempts/upload/evaluatedPdf")
	public String saveEvaluatedPdf(@RequestParam(name = "attemptId") Integer attemptId, 
			RedirectAttributes redirectAttributes,
			@RequestParam("evaluatedPdf") MultipartFile multipartFile) throws IOException, CourseNotFoundException, QuestionNotFoundException, CustomerNotFoundException {
		
		
		AttemptHistory attempt = attemptHistoryService.getAttemptById(attemptId).get();

		if (!(multipartFile.isEmpty())) {
			String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename()).replaceAll("\\s","");
			
			/*
			 * "course_"+attempt.getCourse().getId() +"_que_"+attempt.getQuestion().getId()
			 * +"_attempt_"+attempt.getId()+"_evaluatedcopy.pdf"
			 */
			attempt.setEvaluatedPdfName(fileName);
			
			AttemptHistory savedAttempt = attemptHistoryService.save(attempt);
					
			String uploadDir = "../attempt-pdf/"+"course-"+attempt.getCourse().getId()+"/"
			+"cust-"+attempt.getCustomer().getId()+"/"+"que-"+attempt.getQuestion().getId()+"/"
					+"attempt-"+savedAttempt.getId()+"/evaluated";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			redirectAttributes.addFlashAttribute("message", "Answer book has been saved successfully.");
			
		}else {
			
			redirectAttributes.addFlashAttribute("message", "Evaluated copy not select from your local system.");
		}

		
		 return "redirect:/attempts/page/1?sortField=id&sortDir=asc";
		//return "redirect:/questions-sets/view/"+attempt.getCourse().getId();
	}

	/*
	 * @PostMapping("attempt/evaluatedpdf/upload") public String
	 * handleFileUpload(@RequestParam(name = "attemptId") Integer attemptId,
	 * 
	 * @RequestParam("file") MultipartFile multipartFile ) {
	 * 
	 * // String fileName = file.getOriginalFilename(); AttemptHistory attempt =
	 * attemptHistoryService.getAttemptById(attemptId).get();
	 * 
	 * if (!(multipartFile.isEmpty())) { String fileName =
	 * org.springframework.util.StringUtils.cleanPath(multipartFile.
	 * getOriginalFilename()).replaceAll("\\s","");
	 * 
	 * attempt.setEvaluatedPdfName("course_"+attempt.getCourse().getId()
	 * +"_que_"+attempt.getQuestion().getId()
	 * +"_attempt_"+attempt.getId()+"evaluatedcopy.pdf");
	 * 
	 * AttemptHistory savedAttempt = attemptHistoryService.save(attempt);
	 * 
	 * String uploadDir =
	 * "../attempt-pdf/"+"course-"+attempt.getCourse().getId()+"/"
	 * +"cust-"+attempt.getCustomer().getId()+"/"+"que-"+attempt.getQuestion().getId
	 * ()+"/" +"evaluated-"+savedAttempt.getId();
	 * FileUploadUtil.cleanDir(uploadDir); FileUploadUtil.saveFile(uploadDir,
	 * fileName, multipartFile); redirectAttributes.addFlashAttribute("message",
	 * "Answer book has been saved successfully.");
	 * 
	 * }else {
	 * 
	 * redirectAttributes.addFlashAttribute("message",
	 * "Evaluated copy not select from your local system."); }
	 * 
	 * 
	 * // return "redirect:/attempts/page/1?sortField=id&sortDir=asc&keyword="+name;
	 * return "redirect:/questions-sets/view/"+attempt.getCourse().getId(); }
	 */

}
