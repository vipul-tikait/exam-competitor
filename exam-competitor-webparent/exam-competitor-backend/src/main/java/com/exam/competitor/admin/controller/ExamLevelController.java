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

import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.exception.ExamLevelNotFoundException;
import com.exam.competitor.admin.service.ExamLevelService;
import com.exam.competitor.admin.service.ExamService;
import com.exam.competitor.admin.service.MainExamService;
import com.exam.competitor.admin.service.MainExamTypeService;
import com.exam.competitor.admin.service.TopicService;

@Controller
public class ExamLevelController {

	@Autowired ExamLevelService examLevelService;
	@Autowired ExamService examService;
	@Autowired MainExamService mainExamService;
	@Autowired MainExamTypeService mainExamTypeService;
	@Autowired TopicService topicService;

	@GetMapping("/exam-levels")
	public String getAllExamLevelList(Model model) {

		return getAllExamLevelListByPage(1, model, "name", "asc", null);// "users";
	}

	@GetMapping("/exam-levels/page/{pageNum}")
	public String getAllExamLevelListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<ExamLevel> page = examLevelService.listByPage(pageNum, sortField, sortDir, keyword);
		List<ExamLevel> lstExamLevel = page.getContent();

		long startCount = (pageNum - 1) * examLevelService.EXAM_LEVEL_PER_PAGE + 1;
		long endCount = startCount + examLevelService.EXAM_LEVEL_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("examLevelList", lstExamLevel);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);

		return "exams/exam_lvl";
	}

	@GetMapping("/exam-levels/new")
	public String createNewUser(Model model) {

		List<MainExamType> mainExamTypeList = mainExamTypeService.findAllMainExamTypeList();
		List<MainExam> mainExamList = new ArrayList<MainExam>();
		List<Topic> listTopic = topicService.listTopicUsedInForm();
		ExamLevel examLevel = new ExamLevel();
		model.addAttribute("listTopic", listTopic);
		model.addAttribute("examLevel", examLevel);
		model.addAttribute("mainExamTypeList", mainExamTypeList);
		model.addAttribute("mainExamList", mainExamList);
		model.addAttribute("pageTitle", "Create New ExamLevel");
		return "exams/exam_lvl_form";
	}

	@PostMapping("/exam-levels/save")
	public String saveExamLevel(ExamLevel exam, RedirectAttributes redirectAttributes) throws IOException {

		examLevelService.save(exam);

		redirectAttributes.addFlashAttribute("message", "ExamLevel has been saved successfully.");

		String nameFirstPart = exam.getName();
		return "redirect:/exam-levels/page/1?sortField=name&sortDir=asc&keyword=" + nameFirstPart;
	}

	@GetMapping("/exam-levels/edit/{id}")
	public String editExamLevel(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			ExamLevel examLevel = examLevelService.getExamLevelById(id);

			List<MainExamType> mainExamTypeList = mainExamTypeService.findAllMainExamTypeList();
			List<MainExam> mainExamList = mainExamService
					.getMainExamByMainExamType(examLevel.getExam().getMainExam().getMainExamType().getId());
			List<Exam> examList = examService.getExamByMainExam(examLevel.getExam().getMainExam().getId());
			List<Topic> listTopic = topicService.listTopicUsedInForm();
			
			model.addAttribute("listTopic", listTopic);
			model.addAttribute("examLevel", examLevel);
			model.addAttribute("mainExamTypeList", mainExamTypeList);
			model.addAttribute("mainExamList", mainExamList);
			model.addAttribute("examList", examList);
			model.addAttribute("pageTitle", "Edit  ExamLevel  (ID - " + id + ")");
			return "exams/exam_lvl_form";
		} catch (ExamLevelNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/exams/exam_lvl_form";
		}

	}

	@GetMapping("/exam-levels/delete/{id}")
	public String deletExamLevel(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("message",
					"Ths ExamLevel with id " + id + " has been deleted successfully.");
			examLevelService.deleteUser(id);
		} catch (ExamLevelNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/exam-levels";

	}
}
