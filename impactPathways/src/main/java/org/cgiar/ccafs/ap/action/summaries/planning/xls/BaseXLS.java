/*****************************************************************
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
 *****************************************************************/


package org.cgiar.ccafs.ap.action.summaries.planning.xls;


import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.opensymphony.xwork2.DefaultTextProvider;
import com.opensymphony.xwork2.TextProvider;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages everything related to the creation of a XLS (Microsoft Excel) and the output streams needed to
 * write the
 * information.
 * 
 * @author Héctor Fabio Tobón R. - CIAT/CCAFS
 */
public class BaseXLS {

  public static final int COLUMN_TYPE_BUDGET = 1;
  public static final int COLUMN_TYPE_DECIMAL = 2;
  public static final int COLUMN_TYPE_TEXT_LONG = 3;
  public static final int COLUMN_TYPE_TEXT_SHORT = 4;
  public static final int COLUMN_TYPE_BOOLEAN = 5;
  public static final int COLUMN_TYPE_NUMERIC = 6;

  private static Logger LOG = LoggerFactory.getLogger(BaseXLS.class);

  // Excel template location.
  private static String EXCEL_TEMPLATE_FILE =
    ServletActionContext.getServletContext().getRealPath("resources/templates/template.xlsx");

  // Header Style
  private static final String HEADER_FONT_NAME = "Tahoma";
  private static final short HEADER_FONT_SIZE = 10;
  private static final String HEADER_FONT_COLOR_HEX = "#404040";
  private static final String HEADER_BG_COLOR_HEX = "#f5e8d8";
  private static final int HEADER_ROW_HEIGHT = 31;

  // Border Style


  // Cell Style
  private static final String CELL_DATE_FORMAT = "yyyy-MM-dd";
  private static final String CELL_TRUE_BOOLEAN = "Yes";
  private static final String CELL_FALSE_BOOLEAN = "No";
  private static final String CELL_BORDER_COLOR_HEX = "#c2a5a5";
  private static final short CELL_BORDER_TYPE_BOTTOM = XSSFCellStyle.BORDER_THIN;
  private static final short CELL_BORDER_TYPE_TOP = XSSFCellStyle.BORDER_THIN;
  private static final short CELL_BORDER_TYPE_LEFT = XSSFCellStyle.BORDER_THIN;
  private static final short CELL_BORDER_TYPE_RIGHT = XSSFCellStyle.BORDER_THIN;

  private TextProvider textProvider; // Internationalization file.
  private ByteArrayOutputStream outputStream; // byte stream.
  private Workbook workbook; // Excel high level model.
  private boolean usingTemplate;
  private int rowStart, columnStart, columnEnd, rowCounter, columnCounter;
  // private XSSFCellStyle styleDate, styleInteger, styleDecimal, styleBudget, styleLongString, styleBoolean,
  // styleHeader;
  private XSSFCellStyle styleHeader;
  private XSSFCellStyle[] columnStyles;


  /**
   * This method set-ups a header to the sheet page.
   * 
   * @param sheet where the header will be placed.
   */
  private void addHeader(Sheet sheet) {
    Header header = sheet.getHeader();
    String date = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(new Date());
    header.setLeft("CCAFS Planning and Reporting Platform");
    header.setRight("Report generated on " + date);
  }

  /**
   * This method closes all the streams opened in the process.
   * 
   * @throws IOException If some I/O error occurs.
   */
  public void closeStreams() throws IOException {
    outputStream.close();
    workbook.close();
  }

  /**
   * This method return the information that is in the outputStream as an array of bytes.
   * 
   * @return an array of bytes with the information located in the output stream.
   */
  public byte[] getBytes() {
    return outputStream.toByteArray();
  }

  /**
   * Method used to get the internationalized key that is in the properties file.
   * 
   * @param key to search
   * @return international key
   */
  protected String getText(String key) {
    return textProvider.getText(key);
  }

  /**
   * Method used to initialize the different styles according to the type of value
   */
  private void initializeStyles(int[] columnTypes) {

    CreationHelper createHelper = workbook.getCreationHelper();

    for (int c = 0; c < columnTypes.length; c++) {
      switch (columnTypes[c]) {
        case COLUMN_TYPE_NUMERIC:
          // Style integer
          XSSFCellStyle styleInteger = (XSSFCellStyle) workbook.createCellStyle();
          styleInteger.setAlignment(CellStyle.ALIGN_CENTER);
          styleInteger.setVerticalAlignment(VerticalAlignment.CENTER);
          this.setBorder(styleInteger);
          columnStyles[c] = styleInteger;
          break;
      }
    }

    // Style date
    XSSFCellStyle styleDate = (XSSFCellStyle) workbook.createCellStyle();
    styleDate.setDataFormat(createHelper.createDataFormat().getFormat(CELL_DATE_FORMAT));
    styleDate.setAlignment(CellStyle.ALIGN_CENTER);

    // Style decimal
    XSSFCellStyle styleDecimal = (XSSFCellStyle) workbook.createCellStyle();
    styleDecimal.setAlignment(CellStyle.ALIGN_CENTER);
    styleDecimal.setVerticalAlignment(VerticalAlignment.CENTER);
    styleDecimal.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

    // Style budget
    XSSFCellStyle styleBudget = (XSSFCellStyle) workbook.createCellStyle();
    styleBudget.setAlignment(CellStyle.ALIGN_CENTER);
    styleDecimal.setVerticalAlignment(VerticalAlignment.CENTER);

    // Style long string
    XSSFCellStyle styleLongString = (XSSFCellStyle) workbook.createCellStyle();
    styleLongString.setVerticalAlignment(VerticalAlignment.CENTER);
    styleLongString.setAlignment(HorizontalAlignment.LEFT);
    styleLongString.setWrapText(true);

    // Style short string
    // styleShortString = (XSSFCellStyle) workbook.createCellStyle();
    // styleShortString.setVerticalAlignment(VerticalAlignment.CENTER);
    // styleShortString.setAlignment(CellStyle.ALIGN_CENTER);

    // styleBoleean
    XSSFCellStyle styleBoolean = (XSSFCellStyle) workbook.createCellStyle();
    styleBoolean.setAlignment(CellStyle.ALIGN_CENTER);

    // Style header
    styleHeader = (XSSFCellStyle) workbook.createCellStyle();
    styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
    styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    styleHeader.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    styleHeader.setFillForegroundColor(new XSSFColor(Color.decode(HEADER_BG_COLOR_HEX)));

    XSSFFont font = (XSSFFont) workbook.createFont();
    font.setBold(true);
    font.setFontName(HEADER_FONT_NAME);
    font.setColor(new XSSFColor(Color.decode(HEADER_FONT_COLOR_HEX)));
    font.setFontHeightInPoints(HEADER_FONT_SIZE);
    styleHeader.setFont(font);

    // Set Border
    this.setBorder(styleBoolean);
    this.setBorder(styleBudget);
    this.setBorder(styleDate);
    this.setBorder(styleDecimal);
    this.setBorder(styleLongString);


  }

  /**
   * Method used for to initialize an Excel Workbook object.
   * It creates a Workbook object using a predefined template.
   * 
   * @param excelFormat is the format that you want to create (i.e. 'xls' or 'xlsx').
   * @param useTemplate is true if you want to use a templa, false if you want to create the Workbook empty.
   * @return a Workbook Object representing the Workbook instance where is going to be written all the information in
   *         XLS format.
   */
  public Workbook initializeXLS(boolean useTemplate, int[] columnTypes) {
    textProvider = new DefaultTextProvider();
    outputStream = new ByteArrayOutputStream();
    usingTemplate = useTemplate;
    try {
      // validating the type of format.
      if (useTemplate) {
        rowStart = 12;
        columnStart = 1;
        rowCounter = rowStart;
        columnCounter = columnStart;
        // opening excel template.
        InputStream templateStream = new FileInputStream(EXCEL_TEMPLATE_FILE);
        // creating workbook based on the template.
        workbook = new XSSFWorkbook(templateStream);
        // Initializing styles depending on the cell type.
        this.initializeStyles(columnTypes);
        // closing input stream.
        templateStream.close();
        // applying header.
        this.addHeader(workbook.getSheetAt(0));
      } else {
        workbook = new XSSFWorkbook();
      }
      return workbook;
    } catch (IOException e) {
      LOG.error("There was a problem trying to create the Excel Workbook: ", e.getMessage());
    }
    return null;
  }

  /**
   * This method move the cursor to the next column.
   */
  public void nextColumn() {
    columnCounter++;
  }

  /**
   * This method move the cursor to the beginning of the next row.
   */
  public void nextRow() {
    rowCounter++;
    columnCounter = columnStart;
  }

  private void setBorder(XSSFCellStyle style) {
    // Create the border
    style.setBorderBottom(CELL_BORDER_TYPE_BOTTOM);
    style.setBorderTop(CELL_BORDER_TYPE_TOP);

    // Set color border
    style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(Color.decode(CELL_BORDER_COLOR_HEX)));
    style.setBorderColor(BorderSide.TOP, new XSSFColor(Color.decode(CELL_BORDER_COLOR_HEX)));

  }

  /**
   * This method writes the headers into the given sheet.
   * 
   * @param sheet is the sheet where you want to write the header.
   * @param headers is the array of headers to write.
   */
  public void writeHeaders(Sheet sheet, String[] headers) {
    if (usingTemplate) {
      // Row
      Row row = sheet.createRow(rowStart - 1);
      row.setHeightInPoints(HEADER_ROW_HEIGHT);

      // Writing headers.
      Cell cell;
      int counter;
      for (counter = 1; counter <= headers.length; counter++) {
        cell = row.createCell(counter);
        cell.setCellStyle(styleHeader);
        cell.setCellValue(headers[counter - 1]);
        sheet.autoSizeColumn(counter);
        sheet.setColumnWidth(counter, 6000);
      }
      columnEnd = counter - 1;
    } else {
      // TODO To develop the same algorithm but without style starting in the first row of the sheet.
    }
  }

  /**
   * This method writes the title box into the given sheet.
   * 
   * @param sheet is the sheet where you want to write the title box.
   * @param text is the title of the report.
   */
  public void writeTitleBox(Sheet sheet, String text) {


    XSSFDrawing draw = (XSSFDrawing) sheet.createDrawingPatriarch();
    XSSFTextBox textbox = draw.createTextbox(new XSSFClientAnchor(0, 0, 1, 1, 1, 1, 4, 6));

    // textbox.setTextAutofit(value);
    // textbox.setText(text);
    textbox.setFillColor(255, 204, 41);
    textbox.setVerticalAlignment(VerticalAlignment.CENTER);

    XSSFRichTextString stringX = new XSSFRichTextString();

    Font font = workbook.createFont();
    font.setFontHeightInPoints((short) 24);
    font.setFontName("Courier New");
    font.setItalic(true);
    font.setStrikeout(true);
    // TODO
    // font.setColor(arg0);

    stringX.applyFont(font);
    stringX.append(text);
    textbox.setText(stringX);

  }

  /**
   * This method writes any value into a specific cell.
   * 
   * @param sheet is the sheet where you want to add information into.
   * @param value is the specific information to be written.
   */
  public void writeValue(Sheet sheet, Object value) {

    Row row = sheet.getRow(rowCounter);
    // if there is no row index, it should create it
    if (row == null) {
      row = sheet.createRow(rowCounter);
    }
    row.setHeightInPoints((4 * sheet.getDefaultRowHeightInPoints()));
    Cell cell = row.createCell(columnCounter);

    if (value instanceof Integer) {
      cell.setCellType(Cell.CELL_TYPE_NUMERIC);
      cell.setCellValue((int) value);
      sheet.autoSizeColumn(columnCounter);
      // cell.setCellStyle(styleInteger);
    } else if (value instanceof Date) {
      cell.setCellValue((Date) value);
      // cell.setCellStyle(styleDate);
    } else if (value instanceof Boolean) {
      if ((boolean) value == true) {
        cell.setCellValue(CELL_TRUE_BOOLEAN);
      } else {
        cell.setCellValue(CELL_FALSE_BOOLEAN);
      }
      // cell.setCellStyle(styleBoolean);
    } else if (value instanceof String) {
      // cell.setCellStyle(styleLongString);
      if (value.toString().length() > 30) {
        sheet.setColumnWidth(columnCounter, 12000);
      }
      // sheet.autoSizeColumn(columnCounter);
      cell.setCellValue((String) value);
    } else if (value instanceof Double) {
      // cell.setCellStyle(this.styleDecimal);
      cell.setCellValue((double) value);
      sheet.autoSizeColumn(columnCounter);
    } else if (value == null) {
      cell.setCellValue("");
      // cell.setCellStyle(styleLongString);
    } else {
      cell.setCellValue(String.valueOf(value));
      // cell.setCellStyle(styleLongString);
    }

    if (columnCounter == this.columnStart) {
      XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
      style.cloneStyleFrom(cell.getCellStyle());
      style.setBorderLeft(CELL_BORDER_TYPE_LEFT);
      style.setBorderColor(BorderSide.LEFT, new XSSFColor(Color.decode(CELL_BORDER_COLOR_HEX)));
      cell.setCellStyle(style);
    } else if (columnCounter == this.columnEnd) {
      XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
      style.cloneStyleFrom(cell.getCellStyle());
      style.setBorderRight(CELL_BORDER_TYPE_RIGHT);
      style.setBorderColor(BorderSide.RIGHT, new XSSFColor(Color.decode(CELL_BORDER_COLOR_HEX)));
      cell.setCellStyle(style);
    }
  }

  /**
   * This Method is used for to write the Workbook instance into the output stream
   * 
   * @throws IOException if an I/O error occurs.
   */
  public void writeWorkbook() throws IOException {
    workbook.write(outputStream);
  }


}
