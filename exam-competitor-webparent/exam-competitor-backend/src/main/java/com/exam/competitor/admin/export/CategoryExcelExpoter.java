package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.exam.competitor.admin.common.entity.Category;

public class CategoryExcelExpoter extends AbstactExporter{
	
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	public CategoryExcelExpoter() {
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Category");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createCell(row, 0, "Category Id", cellStyle);
		createCell(row, 1, "Name", cellStyle);
		createCell(row, 2, "Alias", cellStyle);
		createCell(row, 3, "Image", cellStyle);
		createCell(row, 4, "Enabled", cellStyle);
	}	
	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
		
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if (value instanceof Integer) {
			cell.setCellValue((Integer)value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean)value);
		} else {
			cell.setCellValue((String)value);
		}
		cell.setCellStyle(cellStyle);
		 
	}
	
	public void export(List<Category> listAll, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"application/octet-stream",".xlsx","category_");
		writeHeaderLine();
		writeDataLines(listAll);
		
		ServletOutputStream outputStream = response.getOutputStream();	
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
					
	}

	private void writeDataLines(List<Category> listAll) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		for (Category cat : listAll) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			createCell(row, columnIndex++, cat.getId(), cellStyle);
			createCell(row, columnIndex++, cat.getName(), cellStyle);
			createCell(row, columnIndex++, cat.getAlias(), cellStyle);
			createCell(row, columnIndex++, cat.getImage(), cellStyle);
			createCell(row, columnIndex++, cat.isEnabled(), cellStyle);
		}
		
		
	}

}
