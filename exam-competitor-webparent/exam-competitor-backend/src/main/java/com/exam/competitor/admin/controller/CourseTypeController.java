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

import com.exam.competitor.admin.common.entity.course.CourseType;
import com.exam.competitor.admin.exception.CourseTypeNotFoundException;
import com.exam.competitor.admin.service.CourseTypeService;

@Controller
public class CourseTypeController {

	@Autowired
	private CourseTypeService courseTypeService;

	@GetMapping("/courses/type")
	public String listFirstPage(Model model) {
		return getAllCoursesListByPage(1, model, "name", "asc", null);
	}

	@GetMapping("/courses/type/page/{pageNum}")
	public String getAllCoursesListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<CourseType> page = courseTypeService.listByPage(pageNum, sortField, sortDir, keyword);
		List<CourseType> lstCourseType = page.getContent();

		long startCount = (pageNum - 1) * courseTypeService.COURSETYPE_PER_PAGE + 1;
		long endCount = startCount + courseTypeService.COURSETYPE_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String revSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("totalPage", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalCount", page.getTotalElements());
		model.addAttribute("courseTypeList", lstCourseType);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);

		return "courses/courses_types";

	}

	
	@GetMapping("/courses/type/view")
	public String courseTypeView(Model model) {
		

		List<CourseType> lstCourseType = courseTypeService.findAllCourseType();
		model.addAttribute("courseTypeList", lstCourseType);
		
		return "courses/courses_types_view";
	}

	@GetMapping("/courses/type/new")
	public String createNewUser(Model model) {

		CourseType courseType = new CourseType();
		
		model.addAttribute("courseType", courseType);
		model.addAttribute("pageTitle", "Create New Course");
		return "courses/course_type_form";
	}

	@PostMapping("/courses/type/save")
	public String saveUser(CourseType courseType, RedirectAttributes redirectAttributes) throws IOException {
		CourseType savedcourse = courseTypeService.save(courseType);

		redirectAttributes.addFlashAttribute("message", "Course has been saved successfully.");

		String name = savedcourse.getName();
		// return "redirect:/courses/page/1?sortField=id&sortDir=asc&keyword="+name;
		return "redirect:/courses/type";
	}

	@GetMapping("/courses/type/edit/{id}")
	public String editCourse(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			CourseType courseType = courseTypeService.getCourseTypeById(id);
			model.addAttribute("courseType", courseType);
			model.addAttribute("pageTitle", "Edit Course Type (ID - " + id + ")");
			return "courses/course_type_form";
		} catch (CourseTypeNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/courses/type";
		}

	}

	@GetMapping("/courses/type/delete/{id}")
	public String deletUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			redirectAttributes.addFlashAttribute("message",
					"Ths Course Type with id " + id + " has been deleted successfully.");
			courseTypeService.deleteCourse(id);
		} catch (CourseTypeNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/courses/type";

	}


}
