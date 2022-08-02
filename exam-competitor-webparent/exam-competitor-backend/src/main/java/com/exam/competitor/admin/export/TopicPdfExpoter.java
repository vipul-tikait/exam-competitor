package com.exam.competitor.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.exam.competitor.admin.common.entity.Topic;
import com.exam.competitor.admin.common.entity.User;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TopicPdfExpoter  extends AbstactExporter{

	public void export(List<Topic> listAll, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf","topic_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		
		Paragraph paragraph = new Paragraph("List of Topics", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		
		writeTableHeader(table);
		writeTableData(table, listAll);
		document.add(table);
		
		document.close();
		
	}

	private void writeTableData(PdfPTable table, List<Topic> listAll) {
		
		for (Topic topic : listAll) {
			table.addCell(String.valueOf(topic.getId()));
			table.addCell(topic.getName().replace("--"," "));
			table.addCell(topic.getAlias());
			table.addCell(topic.getImage());
			table.addCell(String.valueOf(topic.isEnabled()));
			
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);

		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("NAME", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("ALIAS", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("IMAGE", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);
	}
}
