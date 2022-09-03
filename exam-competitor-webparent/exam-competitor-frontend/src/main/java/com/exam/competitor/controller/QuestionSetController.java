
package com.exam.competitor.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.Utility;
import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.common.entity.course.AttemptHistory;
import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.exception.CourseNotFoundException;
import com.exam.competitor.exception.QuestionSetNotFoundException;
import com.exam.competitor.service.AttemptHistoryService;
import com.exam.competitor.service.CourseService;
import com.exam.competitor.service.CustomerService;
import com.exam.competitor.service.QuestionSetService;

@Controller
public class QuestionSetController {

	@Autowired private QuestionSetService questionSetService;
	@Autowired private CourseService courseService;
	@Autowired private CustomerService customerService;
	@Autowired private AttemptHistoryService attemptHistoryService; 
	
	@GetMapping("/questions-sets")
	public String getAllUserList(Model model) {

		List<QuestionSet> listQset = questionSetService.findAllQuestionSetList();
		model.addAttribute("listQset", listQset);
		return getAllQuesSetListByPage(1, model, "name", "asc", null);// "users";
	}

	@GetMapping("/questions-sets/view/{courseId}")
	public String getAllUserListView(@PathVariable(name = "courseId") Integer courseId, Model model, HttpServletRequest httpServletRequest) throws CourseNotFoundException {

		String email = Utility.getEmailOfAuthenticatedCustomer(httpServletRequest);
		Customer customer = customerService.getCustomerByEmail(email);
		Course course = courseService.getCourseById(courseId);
		
		Set<AttemptHistory> setAttempt = attemptHistoryService.getByCustomerAndCourse(customer, course);
		List<Question> attemptedQueList = new ArrayList<>();
	
		
		for (AttemptHistory attemptHistory : setAttempt) {
			attemptedQueList.add(attemptHistory.getQuestion());
		}
		Map<Integer, AttemptHistory> mapQueAttempt = new HashMap<>();
		
		for (Question question : course.getQuestionSet().getQuestions()) {
			
			for (AttemptHistory attemptHistory : setAttempt) {
				if (question.getId() == attemptHistory.getQuestion().getId()) {
					mapQueAttempt.put(question.getId(), attemptHistory);
					break;
				} else {
					mapQueAttempt.put(question.getId(), null);
				}
			}	
		}
		
		//Set<Question> courseQues = course.getQuestionSet().getQuestions();
	//	List<Question> courseQues = course.getQuestionSet().getQuestions().stream().collect(Collectors.toList());
		//Collections.sort(new ArrayList<Question>(courseQues), Comparator.comparing(Question::getId));
		//course.getQuestionSet().setQuestions(courseQues);
		
		model.addAttribute("course",course);
		model.addAttribute("custId", customer.getId());
	//	model.addAttribute("courseQues",courseQues);
		model.addAttribute("setAttempt", setAttempt);
		model.addAttribute("mapQueAttempt", mapQueAttempt);
		return "questions/question_set_detail_modal";// "users";
	}

	@GetMapping("/questions-sets/page/{pageNum}")
	public String getAllQuesSetListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<QuestionSet> page = questionSetService.listByPage(pageNum, sortField, sortDir, keyword);
		List<QuestionSet> lstQuestionSet = page.getContent();

		long startCount = (pageNum - 1) * questionSetService.QUESTIONSET_PER_PAGE + 1;
		long endCount = startCount + questionSetService.QUESTIONSET_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);

		model.addAttribute("listQset", lstQuestionSet);

		return "questions/question_set";
	}

	
	@PostMapping("/questions-sets/save")
	public String saveUser(QuestionSet questionSet, RedirectAttributes redirectAttributes)
			throws IOException, QuestionSetNotFoundException {

		QuestionSet questionSet1 = questionSetService.getQuestionSetById(questionSet.getId());

		if (questionSet1.getExamLevel().getId() == -1 || questionSet1.getExamLevel().getExam().getId() == -1
				|| questionSet1.getExamLevel().getExam().getMainExam().getId() == -1
				|| questionSet1.getExamLevel().getExam().getMainExam().getMainExamType().getId() == -1) {
			redirectAttributes.addFlashAttribute("message", "Question set not saved, pls select all exam categories");

			return "redirect:/questions-sets/new";
		}
		questionSet1.setId(questionSet.getId());
		questionSet1.setQuestions(questionSet.getQuestions());
		QuestionSet qs = questionSetService.save(questionSet1);

		redirectAttributes.addFlashAttribute("message", "Question set has been saved successfully.");

		return "redirect:/questions-sets/page/1?sortField=id&sortDir=asc&keyword=" + qs.getName();
	}

	@GetMapping("/questions-sets/edit/{id}")
	public String editQueSet(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes,
			@Param("atopicId") Integer atopicId, @Param("amtopicId") Integer amtopicId) {
		try {
			QuestionSet questionSet = questionSetService.getQuestionSetById(id);

			/*
			 * List<MainExamType> mainExamTypeList =
			 * mainExamTypeService.findAllMainExamTypeList(); List<MainExam> mainExamList =
			 * mainExamService.getMainExamByMainExamType(
			 * questionSet.getExamLevel().getExam().getMainExam().getMainExamType().getId())
			 * ; List<Exam> examList = examService
			 * .getExamByMainExam(questionSet.getExamLevel().getExam().getMainExam().getId()
			 * ); List<ExamLevel> examLevelList = examLevelService
			 * .getExamLevelByExam(questionSet.getExamLevel().getExam().getId());
			 */
			Set<Topic> listTopicAddMore = questionSet.getExamLevel().getTopics();

			model.addAttribute("atopicId", atopicId);

			/*
			 * model.addAttribute("mainExamTypeList", mainExamTypeList);
			 * model.addAttribute("mainExamList", mainExamList);
			 * model.addAttribute("examList", examList); model.addAttribute("examLevelList",
			 * examLevelList);
			 */model.addAttribute("qSetId", id);
			model.addAttribute("questionSet", questionSet);
			model.addAttribute("questionSetAddMore", questionSet);

			model.addAttribute("questionSet", questionSet);
			model.addAttribute("pageTitle", "Edit Questin Set (ID - " + id + ")");
			return "questions/question_set_form";
		} catch (QuestionSetNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/questions-sets";
		}

	}

	@GetMapping("/questions-sets/delete/{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("message",
					"Ths Question Set with id " + id + " has been deleted successfully.");
			questionSetService.deleteUser(id);
		} catch (QuestionSetNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/questions-sets";

	}

	@GetMapping("/questions-sets/{id}/enabled/{status}")
	public String deletUser(@PathVariable(name = "id") Integer id, @PathVariable("status") Boolean status, Model model,
			RedirectAttributes redirectAttributes) {

		questionSetService.updateEnabledStatus(id, status);

		String state = status ? "enabled" : "disabled";
		String msg = "The user id : " + id + " has been " + state;
		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/questions-sets";

	}


}
