package com.exam.competitor.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.service.MainExamTypeService;

@RestController
public class MainExamTypeRestController {
	
	@Autowired
	MainExamTypeService examTypeService;
	
	@PostMapping("/mainExamTypes/check_main_exam_type_unique")
	public String checkduplicateEmail( @Param("id") Integer id, @Param("name") String name) {
		return examTypeService.isMainExamTypeNameUnique(id,name)?"OK":"DuplicateName";
	}
	
	
}
