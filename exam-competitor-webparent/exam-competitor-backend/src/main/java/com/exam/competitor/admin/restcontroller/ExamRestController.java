package com.exam.competitor.admin.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.common.entity.exam.Exam;
import com.exam.competitor.admin.repo.MainExamTypeRepository;
import com.exam.competitor.admin.service.ExamService;
import com.exam.competitor.admin.service.MainExamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ExamRestController {
	
	@Autowired ExamService examService;
	@Autowired MainExamService mainExamService;
	
	@Autowired MainExamTypeRepository mainExamTypeRepository;
	
	@PostMapping("/exams/check_exam_unique")
	public String checkduplicateEmail( @Param("id") Integer id, @Param("name") String name) {
		return examService.isExamNameUnique(id,name)?"OK":"Duplicated";
	}
	
	
	@GetMapping("/getExams")
	public @ResponseBody String getExams(@RequestParam Integer mainExamId)
	{
		String json = null;
		List<Exam> list = examService.getExamByMainExam(mainExamId);
		try {
			json = new ObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
}
