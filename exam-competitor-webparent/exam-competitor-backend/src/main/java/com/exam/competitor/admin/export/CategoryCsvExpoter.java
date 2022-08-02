package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.exam.competitor.admin.common.entity.Category;

public class CategoryCsvExpoter extends AbstactExporter{
	
	public void export(List<Category> listCategories, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv","category_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] vcsvHeader = {"Category Id", "Name", "Alias", "Image", "Enabled"};
		String[] fieldMapping = {"id","name","alias","image","enabled"};
		csvWriter.writeHeader(vcsvHeader);
		for (Category category : listCategories) {
			category.setName(category.getName().replace("--", "  "));
			
			csvWriter.write(category, fieldMapping);
			
		}
		
		csvWriter.close();
		
	}

}
