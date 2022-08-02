package com.exam.competitor.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.service.CourseTypeService;

@RestController
public class CourseTypeRestController {
	
	@Autowired CourseTypeService courseTypeService ;
	
	@PostMapping("/courses/type/check_course_type")
	public String checkDuplicateCourseType( @Param("id") Integer id, @Param("name") String name) {
		return courseTypeService.checkDuplicateCourseType(id,name)?"OK":"Duplicated";
	}

}
