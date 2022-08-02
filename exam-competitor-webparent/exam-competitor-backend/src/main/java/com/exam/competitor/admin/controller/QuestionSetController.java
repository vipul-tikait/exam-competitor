
package com.exam.competitor.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.exception.CourseNotFoundException;
import com.exam.competitor.admin.exception.QuestionSetNotFoundException;
import com.exam.competitor.admin.service.CourseService;
import com.exam.competitor.admin.service.ExamLevelService;
import com.exam.competitor.admin.service.ExamService;
import com.exam.competitor.admin.service.MainExamService;
import com.exam.competitor.admin.service.MainExamTypeService;
import com.exam.competitor.admin.service.QuestionService;
import com.exam.competitor.admin.service.QuestionSetService;
import com.exam.competitor.admin.service.TopicService;

@Controller
public class QuestionSetController {

	@Autowired
	private QuestionSetService questionSetService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private MainExamTypeService mainExamTypeService;
	@Autowired
	private MainExamService mainExamService;
	@Autowired
	private ExamService examService;
	@Autowired
	private ExamLevelService examLevelService;
	@Autowired
	private TopicService topicService;
	
	@Autowired CourseService courseService;

	@GetMapping("/questions-sets")
	public String getAllUserList(Model model) {

		List<QuestionSet> listQset = questionSetService.findAllQuestionSetList();
		model.addAttribute("listQset", listQset);
		return getAllQuesSetListByPage(1, model, "name", "asc", null);// "users";
	}

	@GetMapping("/questions-sets/view/{courseId}")
	public String getAllUserListView(@PathVariable(name = "courseId") Integer courseId, Model model) throws CourseNotFoundException {

		Course course = courseService.getCourseById(courseId);
		List<QuestionSet> listQset = questionSetService.findQuestionSetByExamLevel(course.getExamLevel());
		model.addAttribute("listQset", listQset);
		model.addAttribute("courseId",courseId);
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

	@GetMapping("/questions-sets/new")
	public String createNewQueSet(Model model) {

		List<MainExamType> mainExamTypeList = mainExamTypeService.findAllMainExamTypeList();
		List<MainExam> mainExamList = new ArrayList<MainExam>();
		List<Exam> examList = new ArrayList<>();
		List<ExamLevel> examLevelList = new ArrayList<>();
		List<Question> questionList = questionService.findAllQuestionList();
		long countQSet = questionSetService.getQsetNextCount() + 1;

		QuestionSet qs = new QuestionSet();
		qs.setEnabled(true);
		qs.setId((int) countQSet);

		model.addAttribute("mainExamTypeList", mainExamTypeList);
		model.addAttribute("mainExamList", mainExamList);
		model.addAttribute("examList", examList);
		model.addAttribute("examLevelList", examLevelList);
		model.addAttribute("questionList", questionList);
		model.addAttribute("questionSet", qs);
		model.addAttribute("pageTitle", "Create New Question Set");
		return "questions/question_set_form";
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

			List<MainExamType> mainExamTypeList = mainExamTypeService.findAllMainExamTypeList();
			List<MainExam> mainExamList = mainExamService.getMainExamByMainExamType(
					questionSet.getExamLevel().getExam().getMainExam().getMainExamType().getId());
			List<Exam> examList = examService
					.getExamByMainExam(questionSet.getExamLevel().getExam().getMainExam().getId());
			List<ExamLevel> examLevelList = examLevelService
					.getExamLevelByExam(questionSet.getExamLevel().getExam().getId());

			Set<Topic> listTopicAddMore = questionSet.getExamLevel().getTopics();

			/*
			 * Page<Question> ampage = questionService.listByPageForTopics(1, "id", "asc",
			 * null, listTopicAddMore); List<Question> questionListAddMore =
			 * ampage.getContent(); long amstartCount = (1 - 1) *
			 * questionService.QUESTION_PER_PAGE + 1; long amendCount = amstartCount +
			 * questionService.QUESTION_PER_PAGE - 1; if (amendCount >
			 * ampage.getTotalElements()) { amendCount = ampage.getTotalElements(); }
			 * 
			 * model.addAttribute("amtotalPage", ampage.getTotalPages());
			 * model.addAttribute("amcurrentPage", 1); model.addAttribute("amstartCount",
			 * amstartCount); model.addAttribute("amendCount", amendCount);
			 * model.addAttribute("amtotalCount", ampage.getTotalElements());
			 * model.addAttribute("amtopicId", 0); model.addAttribute("questionListAddMore",
			 * questionListAddMore); model.addAttribute("listTopicAddMore",
			 * listTopicAddMore); model.addAttribute("sortField", "id");
			 * model.addAttribute("sortDir", "asc");
			 * 
			 */ model.addAttribute("atopicId", atopicId);

			model.addAttribute("mainExamTypeList", mainExamTypeList);
			model.addAttribute("mainExamList", mainExamList);
			model.addAttribute("examList", examList);
			model.addAttribute("examLevelList", examLevelList);
			model.addAttribute("qSetId", id);
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

	/*
	 * @GetMapping("/questions-sets/addMore/questions/{id}") public String
	 * addMoreQuesToQset(@PathVariable(name = "id") Integer id, Model model,
	 * RedirectAttributes redirectAttributes, @PathVariable(name = "pageNum") int
	 * pageNum,
	 * 
	 * @Param("sortField") String sortField, @Param("sortDir") String
	 * sortDir, @Param("keyword") String keyword, Integer topicId) { try {
	 * 
	 * Page<Question> page = questionService.listByPage(pageNum, sortField, sortDir,
	 * keyword, topicId); List<Question> lstQuestion = page.getContent();
	 * 
	 * long startCount = (pageNum - 1) * questionService.QUESTION_PER_PAGE + 1; long
	 * endCount = startCount + questionService.QUESTION_PER_PAGE - 1; if (endCount >
	 * page.getTotalElements()) { endCount = page.getTotalElements(); }
	 * 
	 * QuestionSet ques = questionSetService.getQuestionSetById(id);
	 * 
	 * List<MainExamType> mainExamTypeList =
	 * mainExamTypeService.findAllMainExamTypeList(); List<MainExam> mainExamList =
	 * mainExamService
	 * .getMainExamByMainExamType(ques.getExamLevel().getExam().getMainExam().
	 * getMainExamType().getId()); List<Exam> examList =
	 * examService.getExamByMainExam(ques.getExamLevel().getExam().getMainExam().
	 * getId()); List<ExamLevel> examLevelList =
	 * examLevelService.getExamLevelByExam(ques.getExamLevel().getExam().getId());
	 * List<Topic> topicList = topicService.listTopicUsedInForm(); List<Question>
	 * questionList = questionService.findAllQuestionList();
	 * 
	 * model.addAttribute("mainExamTypeList", mainExamTypeList);
	 * model.addAttribute("mainExamList", mainExamList);
	 * model.addAttribute("examList", examList); model.addAttribute("examLevelList",
	 * examLevelList); model.addAttribute("questionList", lstQuestion);
	 * model.addAttribute("topicList", topicList); model.addAttribute("questionSet",
	 * ques); model.addAttribute("pageTitle", "Edit Questin Set (ID - " + id + ")");
	 * 
	 * String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
	 * model.addAttribute("totalPage", page.getTotalPages());
	 * model.addAttribute("currentPage", pageNum); model.addAttribute("startCount",
	 * startCount); model.addAttribute("endCount", endCount);
	 * model.addAttribute("totalCount", page.getTotalElements());
	 * model.addAttribute("sortField", sortField); model.addAttribute("sortDir",
	 * sortDir); model.addAttribute("revSortDir", revSortDir);
	 * model.addAttribute("keyword", keyword);
	 * 
	 * return "questions/question_set_form"; } catch (QuestionSetNotFoundException
	 * e) { redirectAttributes.addFlashAttribute("message", e.getMessage()); return
	 * "redirect:/questions-sets"; }
	 * 
	 * }
	 */
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

	/*
	 * @GetMapping("/question-sets/export/csv") public void
	 * exportToCsv(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = questionSetService.findAllUserList(); UserCsvExpoter
	 * exporter = new UserCsvExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/question-sets/export/excel") public void
	 * exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = questionSetService.findAllUserList(); UserExcelExpoter
	 * exporter = new UserExcelExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 * 
	 * @GetMapping("/question-sets/export/pdf") public void
	 * exportToPdf(HttpServletResponse httpServletResponse) throws IOException {
	 * 
	 * List<User> listAll = questionSetService.findAllUserList(); UserPdfExpoter
	 * exporter = new UserPdfExpoter(); exporter.export(listAll,
	 * httpServletResponse);
	 * 
	 * }
	 */

}
