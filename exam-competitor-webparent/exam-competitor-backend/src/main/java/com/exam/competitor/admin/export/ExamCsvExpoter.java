package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.exam.competitor.admin.common.entity.exam.ExamLevel;

public class ExamCsvExpoter extends AbstactExporter{
	
	public void export(List<ExamLevel> listExams, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv","exam_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] vcsvHeader = {"Id", "Name", "Logo", "Topics"};
		String[] fieldMapping = {"id","name","logo","topics"};
		csvWriter.writeHeader(vcsvHeader);
		for (ExamLevel examLevel : listExams) {
			csvWriter.write(examLevel, fieldMapping);
		}
		
		csvWriter.close();
		
	}

}
