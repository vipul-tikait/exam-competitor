package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.exam.competitor.admin.common.entity.User;

public class UserCsvExpoter extends AbstactExporter{
	
	public void export(List<User> listUser, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv","users_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] vcsvHeader = {"User Id", "Email", "First Name", "Last Name", "Roles", "Enabled"};
		String[] fieldMapping = {"id","email","firstName","lastName","roles","enabled"};
		csvWriter.writeHeader(vcsvHeader);
		for (User user : listUser) {
			csvWriter.write(user, fieldMapping);
			
		}
		
		csvWriter.close();
		
	}

}
