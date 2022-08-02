package com.exam.competitor.admin.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.service.TopicService;

@RestController
public class TopicRestController {

	@Autowired
	private TopicService topicsService;
	
	@PostMapping("/topics/check_unique")
	public String checkUnique(@Param("id") Integer id,@Param("name") String name,
			@Param("alias") String alias) {
		
		return topicsService.checkUnique(id, name, alias);
	}
	
}
