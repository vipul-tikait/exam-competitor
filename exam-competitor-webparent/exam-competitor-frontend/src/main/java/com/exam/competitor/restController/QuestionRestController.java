package com.exam.competitor.restController;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.common.entity.course.Question;
import com.exam.competitor.service.QuestionService;

@RestController
public class QuestionRestController {

	@Autowired
	private QuestionService questionService;

	@RequestMapping(method = { RequestMethod.GET }, value = { "/questions/file/{qid}" })
	public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable("qid") Integer id) {
		try {
			Question question = questionService.getQuestionById(id);

			String uploadDir = "../question-pdf/" + question.getId();
			Path uploadPath = Paths.get(uploadDir);
			File file = new File(uploadPath + "/" + question.getQuestionPdfName());
			HttpHeaders respHeaders = new HttpHeaders();
			MediaType mediaType = MediaType.parseMediaType("application/pdf");
			respHeaders.setContentType(mediaType);
			respHeaders.setContentLength(file.length());
			respHeaders.setContentDispositionFormData("attachment", file.getName());
			InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
			return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Errore nel download del file " + ".csv; " + e.getMessage();
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
