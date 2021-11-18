/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import junit.framework.TestCase;
import org.apache.maven.plugin.logging.Log;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.report.spreadsheet.SpreadsheetReportAggregator;
import com.eisgroup.tests.utils.TestUtils;

public class TestSpreadsheetReportAggregator extends TestCase {
	private Log log = new LogMock();
	
	private final String AGGREGATED_REPORT_FILE_NAME = "result_report.xlsx";
	private final String EMPTY_REPORT_NAME = "dependency-report_empty.xlsx";
	private Path emptyReportPath;
	private final String FILLED_REPORT_PATH_NAME_1 = "dependency-report_filled_1.xlsx";
	private final String[] REPORT_1_GROUPID_DESC_VALUES = new String[] {"bc", "ek", "mc", "nf"};
	private Path filledReportPath1;
	private final String FILLED_REPORT_PATH_NAME_2 = "dependency-report_filled_2.xlsx";
	private final String[] SORTED_VALUES = new String[] {"bc", "mc", "ec", "na"};
	private Path filledReportPath2;
	private final String FILLED_REPORT_PATH_NAME_3 = "dependency-report_filled_3.xlsx";
	private final String[] SORTED_VALUES_AFTER_2ND_SORT = new String[] {"ba", "ea", "va", "mc", "na", "zv"};
	private Path filledReportPath3;
	
	private Path reportOutputPath;
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	public void setUp() throws URISyntaxException, IOException, EncryptedDocumentException, InvalidFormatException, ReportGenerationException {
		emptyReportPath = Paths.get(getClass().getClassLoader().getResource(EMPTY_REPORT_NAME).toURI());
		filledReportPath1 = Paths.get(getClass().getClassLoader().getResource(FILLED_REPORT_PATH_NAME_1).toURI());
		filledReportPath2 = Paths.get(getClass().getClassLoader().getResource(FILLED_REPORT_PATH_NAME_2).toURI());
		filledReportPath3 = Paths.get(getClass().getClassLoader().getResource(FILLED_REPORT_PATH_NAME_3).toURI());
		folder.create();
		reportOutputPath = folder.getRoot().toPath().resolve(AGGREGATED_REPORT_FILE_NAME);
	}
	
	public void testAggregate() throws ReportGenerationException, EncryptedDocumentException, InvalidFormatException, IOException {
		SpreadsheetReportAggregator aggreagator = new SpreadsheetReportAggregator(log, false);

		// append empty report
		assertEquals(0, TestUtils.getNumberOfFilledDataRowsInSheet(emptyReportPath, SheetNames.DETAILED));
		assertEquals(0, TestUtils.getNumberOfFilledDataRowsInSheet(emptyReportPath, SheetNames.SUMMARY));
		
		aggreagator.appendReport(reportOutputPath, emptyReportPath);
		
		assertEquals(0, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.DETAILED));
		assertEquals(0, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.SUMMARY));
		
		// append report with 8 rows in detailed sheet and 4 rows in summary sheet
		assertEquals(8, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath1, SheetNames.DETAILED));
		assertEquals(4, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath1, SheetNames.SUMMARY));

		aggreagator.appendReport(reportOutputPath, filledReportPath1);
		
		assertEquals(8, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.DETAILED));
		assertEquals(4, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.SUMMARY));
		
		assertTrue(Arrays.asList(REPORT_1_GROUPID_DESC_VALUES).equals(TestUtils.getGroupIdAndDescriptionConcatPairs(reportOutputPath, SheetNames.SUMMARY)));
		
		// append 2nd report with 8 rows in detailed sheet and 4 rows in summary sheet
		assertEquals(8, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath2, SheetNames.DETAILED));
		assertEquals(4, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath2, SheetNames.SUMMARY));
		
		aggreagator.appendReport(reportOutputPath, filledReportPath2);

		assertEquals(16, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.DETAILED));
		assertEquals(4, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.SUMMARY));
		
		assertTrue(Arrays.asList(SORTED_VALUES).containsAll(TestUtils.getGroupIdAndDescriptionConcatPairs(reportOutputPath, SheetNames.SUMMARY)));
		
		// append 3rd report with 10 rows in detailed sheet and 6 rows in summary sheet
		assertEquals(10, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath3, SheetNames.DETAILED));
		assertEquals(6, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath3, SheetNames.SUMMARY));
		
		aggreagator.appendReport(reportOutputPath, filledReportPath3);

		assertEquals(26, TestUtils.getNumberOfFilledDataRowsInSheet(reportOutputPath, SheetNames.DETAILED));
		assertEquals(6, TestUtils.getNumberOfFilledDataRowsInSheet(reportOutputPath, SheetNames.SUMMARY));
		
		assertTrue(Arrays.asList(SORTED_VALUES_AFTER_2ND_SORT).containsAll(TestUtils.getGroupIdAndDescriptionConcatPairs(reportOutputPath, SheetNames.SUMMARY)));
	}


	public void testAggregateSkipDuplicates() throws ReportGenerationException, EncryptedDocumentException, InvalidFormatException, IOException {
		SpreadsheetReportAggregator aggreagator = new SpreadsheetReportAggregator(log, true);

		// append empty report
		assertEquals(0, TestUtils.getNumberOfFilledDataRowsInSheet(emptyReportPath, SheetNames.DETAILED));
		assertEquals(0, TestUtils.getNumberOfFilledDataRowsInSheet(emptyReportPath, SheetNames.SUMMARY));

		aggreagator.appendReport(reportOutputPath, emptyReportPath);

		assertEquals(0, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.DETAILED));
		assertEquals(0, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.SUMMARY));

		// append report with 8 rows in detailed sheet and 4 rows in summary sheet
		assertEquals(8, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath1, SheetNames.DETAILED));
		assertEquals(4, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath1, SheetNames.SUMMARY));

		aggreagator.appendReport(reportOutputPath, filledReportPath1);

		assertEquals(8, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.DETAILED));
		assertEquals(4, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.SUMMARY));

		assertTrue(Arrays.asList(REPORT_1_GROUPID_DESC_VALUES).equals(TestUtils.getGroupIdAndDescriptionConcatPairs(reportOutputPath, SheetNames.SUMMARY)));

		// append 2nd report with 8 rows in detailed sheet and 4 rows in summary sheet
		assertEquals(8, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath2, SheetNames.DETAILED));
		assertEquals(4, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath2, SheetNames.SUMMARY));

		aggreagator.appendReport(reportOutputPath, filledReportPath2);

		assertEquals(16, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.DETAILED));
		assertEquals(4, TestUtils.getTotalNumberOfLinesInSheet(reportOutputPath, SheetNames.SUMMARY));

		assertTrue(Arrays.asList(SORTED_VALUES).containsAll(TestUtils.getGroupIdAndDescriptionConcatPairs(reportOutputPath, SheetNames.SUMMARY)));

		// append 3rd report with 10 rows in detailed sheet and 6 rows in summary sheet
		assertEquals(10, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath3, SheetNames.DETAILED));
		assertEquals(6, TestUtils.getNumberOfFilledDataRowsInSheet(filledReportPath3, SheetNames.SUMMARY));

		aggreagator.appendReport(reportOutputPath, filledReportPath3);

		assertEquals(21, TestUtils.getNumberOfFilledDataRowsInSheet(reportOutputPath, SheetNames.DETAILED));
		assertEquals(6, TestUtils.getNumberOfFilledDataRowsInSheet(reportOutputPath, SheetNames.SUMMARY));

		assertTrue(Arrays.asList(SORTED_VALUES_AFTER_2ND_SORT).containsAll(TestUtils.getGroupIdAndDescriptionConcatPairs(reportOutputPath, SheetNames.SUMMARY)));
	}
}
