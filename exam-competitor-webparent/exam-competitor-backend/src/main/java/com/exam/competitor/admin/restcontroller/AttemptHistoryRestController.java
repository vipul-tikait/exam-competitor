package com.exam.competitor.admin.restcontroller;

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
import com.exam.competitor.admin.exception.UserNotFoundException;
import com.exam.competitor.admin.service.AttemptHistoryService;
import com.exam.competitor.admin.service.UserService;

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
			String message = "Errore nel download del file " + ".csv; " + e.getMessage();
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
	
	
	/*
	 * @RequestMapping("/questions/file/{qid}") public ResponseEntity<?>
	 * downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
	 * 
	 * @PathVariable("qid") Integer id) throws IOException,
	 * QuestionNotFoundException {
	 * 
	 * Question question = questionService.getQuestionById(id);
	 * 
	 * String uploadDir = "question-pdf/"+question.getId(); Path uploadPath =
	 * Paths.get(uploadDir); File file = new File(uploadPath +
	 * "/"+question.getQuestionPdfName()); if (file.exists()) {
	 * 
	 * //get the mimetype String mimeType =
	 * URLConnection.guessContentTypeFromName(file.getName()); if (mimeType == null)
	 * { //unknown mimetype so set the mimetype to application/octet-stream mimeType
	 * = "application/octet-stream"; }
	 * 
	 * response.setContentType(mimeType);
	 * 
	 *//**
		 * In a regular HTTP response, the Content-Disposition response header is a
		 * header indicating if the content is expected to be displayed inline in the
		 * browser, that is, as a Web page or as part of a Web page, or as an
		 * attachment, that is downloaded and saved locally.
		 * 
		 */
	/*
	
	*//**
		 * Here we have mentioned it to show inline
		 */
	/*
	 * response.setHeader("Content-Disposition", String.format("inline; filename=\""
	 * + file.getName() + "\""));
	 * 
	 * //Here we have mentioned it to show as attachment
	 * //response.setHeader("Content-Disposition",
	 * String.format("attachment; filename=\"" + file.getName() + "\""));
	 * 
	 * response.setContentLength((int) file.length());
	 * 
	 * InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	 * 
	 * HttpHeaders headers = new HttpHeaders(); headers.set("Content-Disposition",
	 * String.format("inline; filename=\"" + file.getName() + "\""));
	 * headers.set("Content-Length", ""+file.length());
	 * FileCopyUtils.copy(inputStream, response.getOutputStream());
	 * InputStreamResource resource = new InputStreamResource(new
	 * FileInputStream(file));
	 * 
	 * 
	 * return ResponseEntity.ok() .headers(headers) .contentLength(file.length())
	 * .contentType(MediaType.APPLICATION_OCTET_STREAM) .body(resource); } }
	 */
	
}

