package com.exam.competitor.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Exam not found")
public class ExamNotFoundRestException extends Exception {
	public ExamNotFoundRestException(String message) {
		super(message);
	}
	
	public ExamNotFoundRestException() {
	}
	
}
