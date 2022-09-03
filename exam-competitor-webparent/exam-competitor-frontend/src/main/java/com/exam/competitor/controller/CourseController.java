package com.exam.competitor.controller;

import java.io.IOException;
import java.security.Principal;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.exception.CourseNotFoundException;
import com.exam.competitor.service.CourseService;
import com.exam.competitor.service.CustomerService;

@Controller
public class CourseController {

	@Autowired private CourseService courseService;
	@Autowired private CustomerService customerService;
	
	@GetMapping("/courses")
	public String listFirstPage(Model model,Principal principal) {
		return getAllCoursesListByPage(1, model, "name", "asc", null,principal);
	}
	@GetMapping("/courses/page/{pageNum}")
	public String getAllCoursesListByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,Principal principal) {

		Page<Course> page = courseService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Course> lstCourse = page.getContent();

		long startCount = (pageNum - 1) * courseService.COURSE_PER_PAGE + 1;
		long endCount = startCount + courseService.COURSE_PER_PAGE - 1;
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
		model.addAttribute("courseList", lstCourse);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("revSortDir", revSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("cust", cust);
		model.addAttribute("custId", cust.getId());
		return "courses/courses_list";

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


	@GetMapping("/courses/details/{id}")
	public String editCourse(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Course course = courseService.getCourseById(id);
			
			model.addAttribute("course", course);
			model.addAttribute("pageTitle", "Edit Course (ID - " + id + ")");
			return "courses/course_form_details";
		} catch (CourseNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/courses";
		}

	}

	@PostMapping("courses/assign")
	public String saveUser(Course course, Principal principal,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			RedirectAttributes redirectAttributes) throws IOException, CourseNotFoundException {
		

		//System.out.println("principal ID:"+principal.getName());
		Set<Customer> custSet = new HashSet<>();
		Customer cust = customerService.getCustomerByEmail(principal.getName());
		Course getCourse = courseService.getCourseById(course.getId());
		custSet.add(cust);
		getCourse.setCustomers(custSet);

		Course savedcourse = courseService.save(getCourse);
		
		redirectAttributes.addFlashAttribute("message", "Course has been saved successfully.");

		String name = savedcourse.getName();
		// return "redirect:/courses/page/1?sortField=id&sortDir=asc&keyword="+name;
		return "redirect:/courses";
	}


}
