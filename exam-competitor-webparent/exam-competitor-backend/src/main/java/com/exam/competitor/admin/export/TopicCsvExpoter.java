package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


import com.exam.competitor.admin.common.entity.Topic;

public class TopicCsvExpoter extends AbstactExporter{
	
	public void export(List<Topic> listTopics, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv","topic_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] vcsvHeader = {"Id", "Name", "Alias", "Image", "Enabled"};
		String[] fieldMapping = {"id","name","alias","image","enabled"};
		csvWriter.writeHeader(vcsvHeader);
		for (Topic topic : listTopics) {
			topic.setName(topic.getName().replace("--", "  "));
			
			csvWriter.write(topic, fieldMapping);
			
		}
		
		csvWriter.close();
		
	}

}
