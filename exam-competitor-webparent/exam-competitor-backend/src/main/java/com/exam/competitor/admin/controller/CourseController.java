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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.common.entity.course.CourseType;
import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.common.entity.exam.MainExamType;
import com.exam.competitor.admin.course.CourseSaveHelper;
import com.exam.competitor.admin.exception.CourseNotFoundException;
import com.exam.competitor.admin.exception.QuestionSetNotFoundException;
import com.exam.competitor.admin.product.ProductSaveHelper;
import com.exam.competitor.admin.service.CourseService;
import com.exam.competitor.admin.service.ExamLevelService;
import com.exam.competitor.admin.service.ExamService;
import com.exam.competitor.admin.service.MainExamService;
import com.exam.competitor.admin.service.MainExamTypeService;
import com.exam.competitor.admin.service.QuestionSetService;

@Controller
public class CourseController {

	@Autowired private CourseService courseService;
	@Autowired private QuestionSetService questionSetService;
	@Autowired private MainExamTypeService mainExamTypeService;
	@Autowired
	private MainExamService mainExamService;
	@Autowired
	private ExamService examService;
	@Autowired
	private ExamLevelService examLevelService;

	@GetMapping("/courses")
	public String listFirstPage(Model model) {
		return getAllCoursesListByPage(1, model, "name", "asc", null);
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
	@GetMapping("/courses/page/{pageNum}")
	public String getAllCoursesListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Course> page = courseService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Course> lstCourse = page.getContent();

		long startCount = (pageNum - 1) * courseService.COURSE_PER_PAGE + 1;
		long endCount = startCount + courseService.COURSE_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("courseList", lstCourse);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);

		return "courses/courses_list";

	}

	@GetMapping("/courses/new")
	public String createNewUser(Model model) {

		//List<CourseType> listCourseType = courseService.findAllCourseType();
		List<MainExamType> mainExamTypeList = mainExamTypeService.findAllMainExamTypeList();
		List<MainExam> mainExamList = new ArrayList<MainExam>();
		List<Exam> examList = new ArrayList<>();
		List<ExamLevel> examLevelList = new ArrayList<>();
		Course course = new Course();
		course.setEnabled(true);
		model.addAttribute("mainExamTypeList", mainExamTypeList);
		model.addAttribute("mainExamList", mainExamList);
		model.addAttribute("examList", examList);
		model.addAttribute("examLevelList", examLevelList);
		
		model.addAttribute("course", course);
		//model.addAttribute("listCourseType", listCourseType);
		model.addAttribute("pageTitle", "Create New Course");
		return "courses/course_form";
	}

	@PostMapping("courses/assign")
	public String saveUser(Course course, 
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			RedirectAttributes redirectAttributes) throws IOException {
		// course.setStartDate(new Date());
		// course.setEndDate(new Date());

		/*int courseId = courseService.getMaxCourseId();
		 *  String date =
		 * course.getStartDate().getYear() + "_" + course.getStartDate().getMonth();
		 * 
		 * String courseCode = date + "_" + course.getDuration() + "TEST_" + courseId;
		 * course.setCourseCode(courseCode);
		 */
		CourseSaveHelper.setCourseDetails(detailIDs, detailNames, detailValues, course);

		Course savedcourse = courseService.save(course);

		redirectAttributes.addFlashAttribute("message", "Course has been saved successfully.");

		String name = savedcourse.getName();
		// return "redirect:/courses/page/1?sortField=id&sortDir=asc&keyword="+name;
		return "redirect:/courses";
	}

	@GetMapping("/courses/edit/{id}")
	public String editCourse(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			//List<CourseType> listCourseType = courseService.findAllCourseType();
			Course course = courseService.getCourseById(id);
			
			List<MainExamType> mainExamTypeList = mainExamTypeService.findAllMainExamTypeList();
			List<MainExam> mainExamList = mainExamService.getMainExamByMainExamType(
					course.getExamLevel().getExam().getMainExam().getMainExamType().getId());
			List<Exam> examList = examService
					.getExamByMainExam(course.getExamLevel().getExam().getMainExam().getId());
			List<ExamLevel> examLevelList = examLevelService
					.getExamLevelByExam(course.getExamLevel().getExam().getId());

			/*
			 * if (course != null && course.getQuestionSet() == null) { List<QuestionSet>
			 * listQset = questionSetService.findAllQuestionSetList();
			 * model.addAttribute("listQset", listQset); } model.addAttribute("course",
			 * course);
			 *///model.addAttribute("listCourseType", listCourseType);
			model.addAttribute("mainExamTypeList", mainExamTypeList);
			model.addAttribute("mainExamList", mainExamList);
			model.addAttribute("examList", examList);
			model.addAttribute("examLevelList", examLevelList);
			
			model.addAttribute("course", course);
			model.addAttribute("pageTitle", "Edit Course (ID - " + id + ")");
			return "courses/course_form";
		} catch (CourseNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/courses";
		}

	}

	@GetMapping("/courses/delete/{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("message",
					"Ths Course with id " + id + " has been deleted successfully.");
			courseService.deleteCourse(id);
		} catch (CourseNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/courses";

	}

	@GetMapping("/courses/{id}/enabled/{status}")
	public String enableCourse(@PathVariable(name = "id") Integer id, @PathVariable("status") Boolean status,
			Model model, RedirectAttributes redirectAttributes) {

		courseService.updateEnabledStatus(id, status);

		String state = status ? "enabled" : "disabled";
		String msg = "The course id : " + id + " has been " + state;
		redirectAttributes.addFlashAttribute("message", msg);

		return "redirect:/courses";

	}

	@GetMapping("/courses/{cid}/assign/{qsId}/{status}")
	public String assignQsetCourse(@PathVariable(name = "cid") Integer id, @PathVariable(name = "qsId") Integer qsId,
			@PathVariable("status") Boolean status,Model model, RedirectAttributes redirectAttributes) {
		try {
			Course course = courseService.getCourseById(id);
			QuestionSet qSet = questionSetService.getQuestionSetById(qsId);
			List<CourseType> listCourseType = courseService.findAllCourseType();
			List<MainExamType> mainExamTypeList = mainExamTypeService.findAllMainExamTypeList();
			List<MainExam> mainExamList = mainExamService.getMainExamByMainExamType(
					course.getExamLevel().getExam().getMainExam().getMainExamType().getId());
			List<Exam> examList = examService
					.getExamByMainExam(course.getExamLevel().getExam().getMainExam().getId());
			List<ExamLevel> examLevelList = examLevelService
					.getExamLevelByExam(course.getExamLevel().getExam().getId());


			String state = status ? "assigned" : "removed";
			
			  if (status) { 
				  course.setQuestionSet(qSet); 
				  } else {
					  course.setQuestionSet(null); 
			 }
			 
			courseService.save(course);

			if (course != null && course.getQuestionSet() == null) {
				List<QuestionSet> listQset = questionSetService.findAllQuestionSetList();
				model.addAttribute("listQset", listQset);
			}
			model.addAttribute("course", course);

			model.addAttribute("listCourseType", listCourseType);
			model.addAttribute("pageTitle", "Edit Course (ID - " + id + ")");
			model.addAttribute("mainExamTypeList", mainExamTypeList);
			model.addAttribute("mainExamList", mainExamList);
			model.addAttribute("examList", examList);
			model.addAttribute("examLevelList", examLevelList);
		
			
			String msg = "The QuestionSet with id : " + qsId + " has been "+state+" to course " + id;
			redirectAttributes.addFlashAttribute("message", msg);

			return "redirect:/courses/edit/"+id;
		} catch (CourseNotFoundException | QuestionSetNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/courses";
		}
	}
	/*
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
