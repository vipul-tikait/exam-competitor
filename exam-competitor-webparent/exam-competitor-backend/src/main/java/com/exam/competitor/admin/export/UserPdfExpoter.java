package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.swing.GroupLayout.Alignment;
import javax.swing.plaf.synth.ColorType;
import javax.swing.text.AttributeSet.ColorAttribute;

import org.apache.poi.ss.usermodel.Color;

import com.exam.competitor.admin.common.entity.User;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;

public class UserPdfExpoter  extends AbstactExporter{

	public void export(List<User> listAll, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf","users_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		
		Paragraph paragraph = new Paragraph("List of Users", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		
		writeTableHeader(table);
		writeTableData(table, listAll);
		document.add(table);
		
		document.close();
		
	}

	private void writeTableData(PdfPTable table, List<User> listAll) {
		
		for (User user : listAll) {
			table.addCell(String.valueOf(user.getId()));
			table.addCell(user.getEmail());
			table.addCell(user.getFirstName());
			table.addCell(user.getLastName());
			table.addCell(user.getRoles().toString());
			table.addCell(String.valueOf(user.isEnabled()));
			
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);

		cell.setPhrase(new Phrase("User ID", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("First Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Last Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Roles", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);
	}
}
