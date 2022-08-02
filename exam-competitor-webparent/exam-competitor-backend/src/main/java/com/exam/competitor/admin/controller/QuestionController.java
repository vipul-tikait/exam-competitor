package com.exam.competitor.admin.controller;

import java.io.IOException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.exception.QuestionNotFoundException;
import com.exam.competitor.admin.exception.QuestionSetNotFoundException;
import com.exam.competitor.admin.exception.TopicNotFoundException;
import com.exam.competitor.admin.service.ExamLevelService;
import com.exam.competitor.admin.service.ExamService;
import com.exam.competitor.admin.service.MainExamService;
import com.exam.competitor.admin.service.MainExamTypeService;
import com.exam.competitor.admin.service.QuestionService;
import com.exam.competitor.admin.service.QuestionSetService;
import com.exam.competitor.admin.service.TopicService;


@Controller
public class QuestionController {

	@Autowired
	private MainExamTypeService mainExamTypeService;
	@Autowired
	private MainExamService mainExamService;
	@Autowired
	private ExamService examService;
	@Autowired
	private ExamLevelService examLevelService;
	
	@Autowired private QuestionService questionService;
	@Autowired private QuestionSetService questionSetService;
	@Autowired private TopicService topicService;
	private String defaultRedirectURL = "redirect:/questions/page/1?sortField=id&sortDir=asc&topicId=0";
	private String defaultRedirectURLAssignQuestion = "redirect:/questions/assign/page/1?sortField=id&sortDir=asc&topicId=0";


	@GetMapping("/questions")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/questions/assign")
	public String listFirstPageAssignQue(@Param("qSetId") Integer qSetId, Model model) {
		return defaultRedirectURLAssignQuestion+"&qSetId="+qSetId;
	}
	
	/*
	 * @GetMapping("/courses/type/{courseType}") public String
	 * listFirstPage(@PathVariable(name = "courseType") int courseType, Model model)
	 * { List<Course> lstCourse = courseService.findAllCourseList();
	 * model.addAttribute(null, lstCourse) }
	 */
	/*
	 * @GetMapping("/courses/{courseType}") public String
	 * getAllCoursesListByCoursetType(@PathVariable(name = "courseType") Integer
	 * courseType, Model model) {
	 * 
	 * List<Course> lstCourse = courseService.listByCourseType(courseType);
	 * model.addAttribute("courseList", lstCourse); return "courses/courses_list";
	 * 
	 * }
	 */
	@GetMapping("/questions/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,
			@Param("topicId") Integer topicId,@Param("qSetId") Integer qSetId ) {

		Page<Question> page = questionService.listByPage(pageNum, sortField, sortDir, keyword,topicId);
		List<Question> questionList = page.getContent();
		List<Topic> listTopics = topicService.listTopicUsedInForm();
		List<QuestionSet> listQset = questionSetService.findAllQuestionSetList();

		long startCount = (pageNum - 1) * questionService.QUESTION_PER_PAGE + 1;
		long endCount = startCount + questionService.QUESTION_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		//if (topicId != null) {
			model.addAttribute("topicId", topicId);
		//	model.addAttribute("qSetId", qSetId);
		//}
		model.addAttribute("questionList", questionList);
		model.addAttribute("sortField", sortField);
 		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listTopics", listTopics);
		model.addAttribute("qSetId", qSetId);
		if (qSetId != null) {
			model.addAttribute("listQset", listQset);
			
			return "questions/question_set_form";
		} else {
			return "questions/questions";
		}

	}

	@GetMapping("/questions/assign/page/{pageNum}")
	public String listByPageSave(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,
			@Param("topicId") Integer topicId,@Param("qSetId") Integer qSetId) throws QuestionSetNotFoundException, TopicNotFoundException {
		
		QuestionSet questionSet = questionSetService.getQuestionSetById(qSetId);
		Set<Topic> listTopicAddMore  = new HashSet<>();
		
		if (topicId !=null && topicId == 0) {
			listTopicAddMore = questionSet.getExamLevel().getTopics();
		} else {
			listTopicAddMore.add(topicService.getTopicById(topicId));
	 		
		}
		
		
		Page<Question> page = questionService.listByPageForTopics(pageNum, sortField, sortDir, keyword, listTopicAddMore);
		List<Question> questionListAddMore = page.getContent();
		
		long startCount = (pageNum - 1) * questionService.QUESTION_PER_PAGE + 1;
		long endCount = startCount + questionService.QUESTION_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		
		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("topicId", topicId);
		model.addAttribute("questionListAddMore", questionListAddMore);
		model.addAttribute("sortField", sortField);
 		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("listTopicAddMore", listTopicAddMore);
		model.addAttribute("qSetId", qSetId);
		model.addAttribute("questionSet", questionSet);
		model.addAttribute("pageTitle", "Assign Questins to Set (ID - " + qSetId + ")");
		
		return "questions/questions_assign_to_question_set";
		

	}

		

	@GetMapping("/questions/new")
	public String createNewQuestion(Model model) {

		//List<QuestionSet> listQuestionType = questionSetService.findAllQuestionSetList();
		List<Topic> listTopic= topicService.listTopicUsedInForm();
		Question question = new Question();
		question.setEnabled(true);

		model.addAttribute("question", question);
		//model.addAttribute("listQuestionType", listQuestionType);
		model.addAttribute("listTopic", listTopic);
		model.addAttribute("pageTitle", "Create New Question");
		return "questions/question_form";
	}

	@PostMapping("/questions/save")
	public String saveQuestion(Question question, RedirectAttributes redirectAttributes) throws IOException {
		Question savedQuestion = questionService.save(question);

		redirectAttributes.addFlashAttribute("message", "Questions has been saved successfully.");

		String name = savedQuestion.getName();
		 return "redirect:/questions/page/1?sortField=id&sortDir=asc&keyword="+name;
		//return "redirect:/questions";
	}

	@GetMapping("/questions/edit/{id}")
	public String editQuestion(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			//List<QuestionSet> listQuestionSet = questionSetService.findAllQuestionSetList();
		//	List<TopicSample> topicList = topicService.findAllTopicList();
			List<Topic> listTopic= topicService.listTopicUsedInForm();
			Question question = questionService.getQuestionById(id);
			
			model.addAttribute("question", question);
			//model.addAttribute("listQuestionSet", listQuestionSet);
			model.addAttribute("listTopic", listTopic);
			model.addAttribute("pageTitle", "Edit Question (ID - " + id + ")");
			return "questions/question_form";
		} catch (QuestionNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/questions";
		}

	}

	@GetMapping("/questions/delete/{id}")
	public String deletQuestin(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) throws QuestionNotFoundException {
		redirectAttributes.addFlashAttribute("message",
				"Ths Question with id " + id + " has been deleted successfully.");
		questionService.deleteQuestion(id);
		return "redirect:/questions";

	}

	
	@GetMapping("/questions/{id}/enabled/{status}")
	public String enableQuestion(@PathVariable(name = "id") Integer id, @PathVariable("status") Boolean status,
			Model model, RedirectAttributes redirectAttributes) {

		questionService.updateEnabledStatus(id, status);

		String state = status ? "enabled" : "disabled";
		String msg = "The question id : " + id + " has been " + state;
		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/questions";

	}
	 
	@GetMapping("/questions/{qsid}/assign/{qid}")
	public String assignQuestion(@PathVariable(name = "qsid") Integer id, @PathVariable("qid") Boolean status,
			Model model, RedirectAttributes redirectAttributes) {

		questionService.updateEnabledStatus(id, status);

		String state = status ? "enabled" : "disabled";
		String msg = "The question id : " + id + " has been " + state;
		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/questions";

	}
	/*
	 * @GetMapping("/questions/{cid}/assign/{qsId}/{status}") public String
	 * assignQsetCourse(@PathVariable(name = "cid") Integer id, @PathVariable(name =
	 * "qsId") Integer qsId,
	 * 
	 * @PathVariable("status") Boolean status,Model model, RedirectAttributes
	 * redirectAttributes) { try { Course course = courseService.getCourseById(id);
	 * QuestionSet qSet = questionSetService.getQuestionSetById(qsId);
	 * List<CourseType> listCourseType = courseService.findAllCourseType(); String
	 * state = status ? "assigned" : "removed"; if (status) {
	 * course.setQuestionSet(qSet); } else { course.setQuestionSet(null); }
	 * 
	 * courseService.save(course); if (course != null && course.getQuestionSet() ==
	 * null) { List<QuestionSet> listQset =
	 * questionSetService.findAllQuestionSetList(); model.addAttribute("listQset",
	 * listQset); } model.addAttribute("course", course);
	 * model.addAttribute("listCourseType", listCourseType);
	 * model.addAttribute("pageTitle", "Edit Course (ID - " + id + ")");
	 * 
	 * 
	 * String msg = "The QuestionSet with id : " + qsId +
	 * " has been "+state+" to course " + id;
	 * redirectAttributes.addFlashAttribute("message", msg);
	 * 
	 * return "courses/course_form"; } catch (QuestionNotFoundException |
	 * QuestionSetNotFoundException e) {
	 * redirectAttributes.addFlashAttribute("message", e.getMessage()); return
	 * "redirect:/courses"; } }
	 */	/*
	 * @GetMapping("/courses/export/csv") public void
	 * exportToCsv(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = courseService.findAllUserList(); UserCsvExpoter exporter
	 * = new UserCsvExpoter(); exporter.export(listAll, httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/courses/export/excel") public void
	 * exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = courseService.findAllUserList(); UserExcelExpoter
	 * exporter = new UserExcelExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/courses/export/pdf") public void
	 * exportToPdf(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = courseService.findAllUserList(); UserPdfExpoter exporter
	 * = new UserPdfExpoter(); exporter.export(listAll, httpServletResponse);
	 * 
	 * }
	 */

}
