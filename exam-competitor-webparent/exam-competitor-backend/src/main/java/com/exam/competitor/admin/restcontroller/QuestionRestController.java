package com.exam.competitor.admin.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.admin.exception.QuestionSetNotFoundException;
import com.exam.competitor.admin.exception.TopicNotFoundException;
import com.exam.competitor.admin.service.QuestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class QuestionRestController {
	
	@Autowired private QuestionService questionService;
	
	@GetMapping("/questions/getQuestionByTopic")
	public @ResponseBody String getQuestionByTopic(@RequestParam Integer topicId,@RequestParam Integer qSetId) throws TopicNotFoundException, QuestionSetNotFoundException
	{
		String json = null;
		List<Question> list = questionService.getQuestionByTopic(topicId,qSetId);
		try {
			json = new ObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
}
