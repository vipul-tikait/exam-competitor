package com.exam.competitor.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class ExamCompetitorBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamCompetitorBackendApplication.class, args);
	}

	
}
