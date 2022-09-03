package com.exam.competitor.restController;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.exam.competitor.admin.common.entity.User;
import com.exam.competitor.admin.common.entity.course.AttemptHistory;
import com.exam.competitor.exception.UserNotFoundException;
import com.exam.competitor.service.AttemptHistoryService;
import com.exam.competitor.service.UserService;

@RestController
public class AttemptHistoryRestController {

	@Autowired AttemptHistoryService attemptHistoryService;
	@Autowired UserService userService;
	

	@RequestMapping(method = { RequestMethod.GET }, value = { "/attempts/answered/file/{attemptId}" })
	public ResponseEntity<InputStreamResource> downloadAnsweredPdf(@PathVariable("attemptId") Integer attemptId) {
		try {
			AttemptHistory attempt = attemptHistoryService.getAttemptById(attemptId).get();

			String uploadDir = "../attempt-pdf/"+"course-"+attempt.getCourse().getId()+"/"
			+"cust-"+attempt.getCustomer().getId()+"/"+"que-"+attempt.getQuestion().getId()+"/"
					+"attempt-"+attempt.getId()+"/answerbook";
			
							
			Path uploadPath = Paths.get(uploadDir);
			File file = new File(uploadPath + "\\" + attempt.getAnsweredPdfName());
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

	@RequestMapping(method = { RequestMethod.GET }, value = { "/attempts/evaluated/file/{attemptId}" })
	public ResponseEntity<InputStreamResource> downloadEvaluatedPdf(@PathVariable("attemptId") Integer attemptId) {
		try {
			AttemptHistory attempt = attemptHistoryService.getAttemptById(attemptId).get();

			String uploadDir = "../attempt-pdf/"+"course-"+attempt.getCourse().getId()+"/"
			+"cust-"+attempt.getCustomer().getId()+"/"+"que-"+attempt.getQuestion().getId()+"/"
					+"attempt-"+attempt.getId()+"/evaluated";
			
							
			Path uploadPath = Paths.get(uploadDir);
			File file = new File(uploadPath + "\\" + attempt.getEvaluatedPdfName());
			HttpHeaders respHeaders = new HttpHeaders();
			MediaType mediaType = MediaType.parseMediaType("application/pdf");
			respHeaders.setContentType(mediaType);
			respHeaders.setContentLength(file.length());
			respHeaders.setContentDispositionFormData("attachment", file.getName());
			InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
			return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
		} catch (Exception e) {
			String message = "Errore nel download del file " + ".pdf; " + e.getMessage();
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("attempts/assign/moderator")
	public String checkduplicateEmail( @Param("attemptId") Integer attemptId, @Param("moderatorId") Integer moderatorId)
	throws UserNotFoundException{
		
		User user = userService.getUserById(moderatorId);
		AttemptHistory attemptHistory = attemptHistoryService.getAttemptById(attemptId).get();
		attemptHistory.setUser(user);
		
		AttemptHistory attemptHistorySaved = attemptHistoryService.save(attemptHistory);
		return attemptHistorySaved.getUser().getId() == moderatorId ?"OK":"Error";
	}
	
	
	
	
}

