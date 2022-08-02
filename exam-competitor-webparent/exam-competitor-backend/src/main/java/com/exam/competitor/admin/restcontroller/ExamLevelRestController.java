package com.exam.competitor.admin.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.exam.competitor.admin.service.ExamLevelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ExamLevelRestController {
	
	@Autowired private ExamLevelService examLevelService;
	
	@GetMapping("/getExamLevels")
	public @ResponseBody String getExamLevels(@RequestParam Integer examId)
	{
		String json = null;
		List<ExamLevel> list = examLevelService.getExamLevelByExam(examId);
		try {
			json = new ObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
}
