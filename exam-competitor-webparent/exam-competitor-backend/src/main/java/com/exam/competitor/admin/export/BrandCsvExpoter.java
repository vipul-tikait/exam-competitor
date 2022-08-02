package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.exam.competitor.admin.common.entity.Brand;

public class BrandCsvExpoter extends AbstactExporter{
	
	public void export(List<Brand> listBrands, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv","brand_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] vcsvHeader = {"Brand Id", "Name", "Logo", "Categories"};
		String[] fieldMapping = {"id","name","logo","categories"};
		csvWriter.writeHeader(vcsvHeader);
		for (Brand brand : listBrands) {
			csvWriter.write(brand, fieldMapping);
		}
		
		csvWriter.close();
		
	}

}
