/*
 * This file is part of CCAFS Planning and Reporting Platform.
 * CCAFS P&R is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 * CCAFS P&R is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with CCAFS P&R. If not, see <http://www.gnu.org/licenses/>.
 */

package org.cgiar.ccafs.ap.action.summaries.pdf;

import org.cgiar.ccafs.ap.data.model.CaseStudy;
import org.cgiar.ccafs.ap.util.BasePdf;
import org.cgiar.ccafs.ap.util.HeaderFooterPdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CaseStudySummaryPdf {

  // Logger
  private static final Logger LOG = LoggerFactory.getLogger(CaseStudySummaryPdf.class);

  // Attributes
  private BasePdf basePdf;
  private String summaryTitle;
  private InputStream inputStream;
  private int contentLength;

  @Inject
  public CaseStudySummaryPdf(BasePdf basePdf) {
    this.basePdf = basePdf;
  }

  public void generatePdf(List<CaseStudy> caseStudies) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Document document = new Document(PageSize.A4);

    PdfWriter writer = basePdf.initializePdf(document, outputStream, basePdf.PORTRAIT);

    // Adding the event to include header and footer on each page
    HeaderFooterPdf event = new HeaderFooterPdf(summaryTitle, basePdf.PORTRAIT);
    writer.setPageEvent(event);

    // Open document
    document.open();

    // Cover page
    basePdf.addCover(document, summaryTitle);

    // Summary title
    basePdf.addTitle(document, summaryTitle);

    // Add content
    try {
      PdfPTable table = new PdfPTable(5);

      // Set table widths
      table.setLockedWidth(true);
      table.setTotalWidth(500);
      table.setWidths(new int[] {1, 5, 2, 2, 2});

      // Repeat header in every page
      table.setHeaderRows(1);

      // Add table headers
      basePdf.addTableHeaderCell(table, "ID");
      basePdf.addTableHeaderCell(table, "Title");
      basePdf.addTableHeaderCell(table, "Leader");
      basePdf.addTableHeaderCell(table, "Countries");
      basePdf.addTableHeaderCell(table, "type");

      // Add table body
      CaseStudy csTemp;
      for (int c = 0; c < caseStudies.size(); c++) {
        csTemp = caseStudies.get(c);
        basePdf.addTableBodyCell(table, String.valueOf(csTemp.getId()), Element.ALIGN_CENTER, c % 2);
        basePdf.addTableBodyCell(table, csTemp.getTitle(), Element.ALIGN_LEFT, c % 2);
        basePdf.addTableBodyCell(table, csTemp.getLeader().getAcronym(), Element.ALIGN_CENTER, c % 2);
        basePdf.addTableBodyCell(table, csTemp.getCountries().get(0).getName(), Element.ALIGN_LEFT, c % 2);
        basePdf.addTableBodyCell(table, csTemp.getTypes().get(0).getName(), Element.ALIGN_LEFT, c % 2);
      }

      document.add(table);
    } catch (DocumentException e) {
      LOG.error("-- generatePdf() > There was an error adding the table with content for case study summary. ", e);
    }

    // Close document
    document.close();

    // Setting result file attributes
    contentLength = outputStream.size();
    inputStream = (new ByteArrayInputStream(outputStream.toByteArray()));
  }

  public int getContentLength() {
    return contentLength;
  }

  public String getFileName() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String fileName;

    fileName = summaryTitle.replace(' ', '_');
    fileName += "_" + dateFormat.format(new Date());
    fileName += ".pdf";

    return fileName;
  }

  public String getFileTitle() {
    return summaryTitle;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public void setSummaryTitle(String title) {
    this.summaryTitle = title;
  }
}
