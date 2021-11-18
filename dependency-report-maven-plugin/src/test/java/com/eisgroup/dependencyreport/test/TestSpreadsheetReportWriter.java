/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import com.eisgroup.dependencyreport.core.DependencyInfoData;
import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.core.SummarySheetColumns;
import com.eisgroup.dependencyreport.report.spreadsheet.SpreadsheetReportWriter;
import com.eisgroup.dependencyreport.utils.SpreadsheetUtils;
import com.eisgroup.tests.utils.TestUtils;

import junit.framework.TestCase;

public class TestSpreadsheetReportWriter extends TestCase {
	private Log log = new LogMock();
	private static final String DATA_FILE_NAME = "dependency-info-data.xml";
	private static final String REPORT_FILE_NAME = "dependency-report.xml";
	private static final String INPUT_SHEET_FILE_NAME = "input_sheet.xlsx";
	
	private List<DependencyInfoData> testData;
	
	private Path dataFilePath;
	private Path inputSheetPath;
	private Path reportOutputPath;
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Before
	public void setUp() throws URISyntaxException, IOException, EncryptedDocumentException, InvalidFormatException, ReportGenerationException {
		dataFilePath = Paths.get(getClass().getClassLoader().getResource(DATA_FILE_NAME).toURI());
		testData = TestUtils.getListOfDependencies(dataFilePath);
		inputSheetPath = Paths.get(getClass().getClassLoader().getResource(INPUT_SHEET_FILE_NAME).toURI());
		
		folder.create();
		reportOutputPath = folder.getRoot().toPath().resolve(REPORT_FILE_NAME);
	}
	
	public void testReportGeneration() throws ReportGenerationException, EncryptedDocumentException, InvalidFormatException, IOException {
		SpreadsheetReportWriter writer = new SpreadsheetReportWriter(testData, log);
		writer.writeReport(reportOutputPath);
		
		assertEquals(TestUtils.getUniqueGroupIds(testData).size(), TestUtils.getNumberOfFilledDataRowsInSheet(reportOutputPath, SheetNames.SUMMARY));
		
		// Test if data is in the spreadsheet's detail sheet
		List<String> detailedSheetData = TestUtils.getGroupIdAndDescriptionConcatPairs(reportOutputPath, SheetNames.DETAILED);
		List<String> actualData = TestUtils.getGroupIdAndDescriptionConcatPairs(testData, SheetNames.DETAILED);
		actualData.removeAll(detailedSheetData);
		assertTrue(actualData.isEmpty());
		
		// Test if data is in the spreadsheet's summary sheet
		List<String> summarySheetData = TestUtils.getGroupIdAndDescriptionConcatPairs(reportOutputPath, SheetNames.SUMMARY);
		actualData = TestUtils.getGroupIdAndDescriptionConcatPairs(testData, SheetNames.SUMMARY);
		actualData.removeAll(summarySheetData);
		assertTrue(actualData.isEmpty());
		
		// Check if data contains empty lines. Generated report must not contain any empty lines.
		int totalNumberOfLines = TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.DETAILED);
		assertEquals(detailedSheetData.size(), totalNumberOfLines);
	}
	
	public void testReportGenerationWithDataOverride() throws Exception {
		Map<String, Row> rows = SpreadsheetUtils.getSheetRows(inputSheetPath, SummarySheetColumns.FRAMEWORK.getColumnIndex(), SheetNames.SUMMARY, log);
		assertEquals(2, rows.size() - 1); // -1, because don't count header row
		
		for (DependencyInfoData data : testData) {
			if ("e".equals(data.getGroupId())) {
				assertNull(data.getUrlOverride());
				assertNull(data.getLicenseNameOverride());
				assertNull(data.getDescriptionOverride());
			}
		}
		
		SpreadsheetUtils.updateWithDataFromInputSpreadsheet(inputSheetPath, testData, log);
		
		for (DependencyInfoData data : testData) {
			if ("e".equals(data.getGroupId())) {
				assertEquals("www.test.org", data.getUrlOverride());
				assertEquals("LICENSE_FOR_A", data.getLicenseNameOverride());
				assertNull("DESCRIPTION_FOR_A", data.getDescriptionOverride());
			}
		}
	}
}
