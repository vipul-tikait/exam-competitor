package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.exam.competitor.admin.common.entity.Customer;

public class CustomerCsvExporter extends AbstactExporter {
	
	public void export(List<Customer> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "customers_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"ID", "First Name", "Last Name","E-mail", "City","State", "Country", "Enabled"};
		String[] fieldMapping = {"id", "firstName", "lastName", "email", "city","state", "country", "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (Customer cust : listUsers) {
			csvWriter.write(cust, fieldMapping);
		}
		
		csvWriter.close();
	}
}
