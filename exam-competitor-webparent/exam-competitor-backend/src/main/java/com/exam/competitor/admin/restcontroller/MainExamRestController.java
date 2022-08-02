package com.exam.competitor.admin.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.common.entity.exam.MainExam;
import com.exam.competitor.admin.service.MainExamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MainExamRestController {

	@Autowired
	MainExamService mainExamService;

	@PostMapping("/mainExams/check_main_exam_unique")
	public String checkduplicateEmail(@Param("id") Integer id, @Param("name") String name) {
		System.out.println("Inside MainExamRestController");
		return mainExamService.isMainExamNameUnique(id, name) ? "OK" : "Duplicated";
	}
	
	@GetMapping("/getMainExams")
	public @ResponseBody String getMainExams(@RequestParam Integer mainExamTypeId)
	{
		String json = null;
		List<MainExam> list = mainExamService.getMainExamByMainExamType(mainExamTypeId);
		try {
			json = new ObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}


	
}
