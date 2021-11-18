package com.eisgroup.dependencyreport.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import junit.framework.TestCase;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.core.SummarySheetColumns;

public class SpreadsheetUtilsTest extends TestCase {

	private final String FILLED_REPORT_PATH_NAME_3 = "dependency-report_summary_version.xlsx";

	public void testAppendVersionToSummaryRow() throws URISyntaxException, IOException {

		Path path = Paths.get(getClass().getClassLoader().getResource(FILLED_REPORT_PATH_NAME_3).toURI());
		InputStream is = Files.newInputStream(path);
		Workbook wb = WorkbookFactory.create(is);
		Sheet sheet = wb.getSheet(SheetNames.SUMMARY.getName());
		Row row = sheet.getRow(1);
		SpreadsheetUtils.updateVersionListInSummaryReport( row, "2.0-beta");
		SpreadsheetUtils.updateVersionListInSummaryReport( row, "1.2.3");
		SpreadsheetUtils.updateVersionListInSummaryReport( row, "1");
		SpreadsheetUtils.updateVersionListInSummaryReport( row, "1");
		SpreadsheetUtils.updateVersionListInSummaryReport( row, "3,4");
		SpreadsheetUtils.updateVersionListInSummaryReport( row, " 5 , , 6 ");
		SpreadsheetUtils.updateVersionListInSummaryReport( row, "1.2.5");
		SpreadsheetUtils.updateVersionListInSummaryReport( row, "");
		SpreadsheetUtils.updateVersionListInSummaryReport( row, "11.12");
		Cell cell = row.getCell(SummarySheetColumns.VERSIONS.getColumnIndex());
		assertEquals("1, 1.2.3, 1.2.5, 11.12, 2.0-beta, 3, 4, 5, 6", cell.getStringCellValue());

	}
}