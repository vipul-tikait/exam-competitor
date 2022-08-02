package com.exam.competitor.admin.export;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstactExporter {
	
	public void setResponseHeader(HttpServletResponse response, String contentType, String extention,
			String prefix) {
		DateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStamp = dateFormat.format(new Date());
		String fileName = prefix +timeStamp + extention;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
	}

}
