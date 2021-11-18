/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.report.spreadsheet;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.core.SummarySheetColumns;
import com.eisgroup.dependencyreport.utils.SpreadsheetUtils;

public class SpreadsheetReportAggregator {
	private Log log;
	private Boolean skipDuplicates;
	private String rootComponentName;

	public SpreadsheetReportAggregator(Log log,Boolean skipDuplicates) {
		this.log = log;
		this.skipDuplicates = skipDuplicates;
	}

	private Set<DetailedSheetColumns> excludeColumns;

	public SpreadsheetReportAggregator(Log log, Boolean skipDuplicates, Set<DetailedSheetColumns> excludeColumns, String rootComponentName) {
		this.log = log;
		this.skipDuplicates = skipDuplicates;
		this.excludeColumns = excludeColumns;
		this.rootComponentName = rootComponentName;
	}


	public void appendReport(Path destinationReportPath, Path sourceReportPath) throws ReportGenerationException {
		// If destination report does not exist, then it means, that it's the first project executing
		// report generation. In this case, just write project report as final report.
		if (!Files.exists(destinationReportPath)) {
			try (InputStream sourceInputStream = Files.newInputStream(sourceReportPath);
					Workbook sourceReportWorkbook = XSSFWorkbookFactory.createWorkbook(sourceInputStream)) {
				log.info("Cannot find final report file. It will be created.");
				SpreadsheetUtils.writeWorkbookToFile(sourceReportWorkbook, destinationReportPath, log);
				return;
			} catch (Exception e) {
				log.error("Problems occured when writing report to destination path.", e);
				throw new ReportGenerationException(e);
			}
		} else {
			log.info("Report file in existence. Appending.");
		}
		try (InputStream destinationInputStream = Files.newInputStream(destinationReportPath);
			 Workbook destinationWorkbook = XSSFWorkbookFactory.createWorkbook(destinationInputStream);
			 InputStream sourceInputStream = Files.newInputStream(sourceReportPath);
			 Workbook sourceWorkbook = XSSFWorkbookFactory.createWorkbook(sourceInputStream);) {

			WorkbookReusableComponentCache styleCache = new WorkbookReusableComponentCache(destinationWorkbook);

			appendReportToDetailedSheet(destinationWorkbook, sourceWorkbook, styleCache);
			appendReportToSummarySheet(destinationWorkbook, sourceWorkbook, styleCache);

			ReportSpreadsheetUtils.applyFormatting(destinationWorkbook);

			if (this.excludeColumns!=null) {
				for (DetailedSheetColumns excludeColumn : this.excludeColumns) {
					destinationWorkbook.getSheet(SheetNames.DETAILED.getName()).setColumnHidden(excludeColumn.getColumnIndex(), true);
				}
			}

			SpreadsheetUtils.writeWorkbookToFile(destinationWorkbook, destinationReportPath, log);
			log.info("Report has been successfully written: " + destinationReportPath);
		} catch (Exception e) {
			log.error("Report aggregation failed. " + e.getMessage(), e);
			throw new ReportGenerationException(e);
		}
	}
	
	private void appendReportToDetailedSheet(Workbook finalReportWorkbook, Workbook sourceReportWorkbook, WorkbookReusableComponentCache styleCache) throws ReportGenerationException {
		Sheet finalReportDetailsSheet = finalReportWorkbook.getSheet(SheetNames.DETAILED.getName());
		
		int rowIndex = SpreadsheetUtils.getFirstEmptyRowIndex(finalReportWorkbook, SheetNames.DETAILED);

		Sheet reportsDetailedSheet = sourceReportWorkbook.getSheet(SheetNames.DETAILED.getName());
		if (reportsDetailedSheet == null) {
			throw new ReportGenerationException("Report contains no sheet named: " + SheetNames.DETAILED);
		}
		Iterator<Row> projectReportDetailsSheetRowIterator = reportsDetailedSheet.iterator();

		// skip first row, header information not needed
		if (projectReportDetailsSheetRowIterator.hasNext()) {
			projectReportDetailsSheetRowIterator.next();
		}
		
		while (projectReportDetailsSheetRowIterator.hasNext()) {
			Row projectReportDetailsSheetRow = projectReportDetailsSheetRowIterator.next();

			if (this.skipDuplicates && ReportSpreadsheetUtils.isEqualRowInDetailedReportExists(projectReportDetailsSheetRow, finalReportDetailsSheet)) {
				// don't append equal rows in case 'skip-duplicates' was set to 'true'
				continue;
			}

			if (this.excludeColumns!=null
					&& this.excludeColumns.size()>0
					&& ReportSpreadsheetUtils.isEqualRowInDetailedReportExistsWithExcludes(projectReportDetailsSheetRow, finalReportDetailsSheet, this.excludeColumns)) {
				continue;
			}




			if (SpreadsheetUtils.isRowEmpty(projectReportDetailsSheetRow, SheetNames.DETAILED)) {
				// don't append empty rows
				continue;
			}

			DetailedSheetLineBuilder builder = new DetailedSheetLineBuilder(finalReportDetailsSheet, rowIndex, styleCache);
			builder.addArtifactId(SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.ARTIFACTID.getColumnIndex())));
			builder.addGroupId(SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.GROUPID.getColumnIndex())));
			builder.addVersion(SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.VERSION.getColumnIndex())));

			// Override eiscomponent with predefined value - for V20 summary report
			if (!StringUtils.isEmpty(rootComponentName)) {
				builder.addEisComponent(rootComponentName);
			} else {
				builder.addEisComponent(SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.EISCOMPONENT.getColumnIndex())));
			}

			builder.addDependencyPath(SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.DEPENDENCY_PATH.getColumnIndex())));
			builder.addWebsiteUrl(SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.WEBSITE.getColumnIndex())));
			builder.addDescription(SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.DESCRIPTION.getColumnIndex())));
			builder.addLicenseFilename(SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.LICENSE_FILE_NAME.getColumnIndex())));

			builder.addModified(
					SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.MODIFIED.getColumnIndex()))!=null &&
					SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.MODIFIED.getColumnIndex())).equals(SpreadsheetUtils.VALUE_YES));

			Cell licenseCell = projectReportDetailsSheetRow.getCell(DetailedSheetColumns.LICENSE.getColumnIndex());
			if (licenseCell != null) {
				String licenseName = licenseCell.getStringCellValue();
				String licenseUrl = null;
				if (licenseName != null) {
					Hyperlink hyperlink = licenseCell.getHyperlink();
					if (hyperlink != null) {
						licenseUrl = hyperlink.getAddress();
					}
				}
				builder.addLicensingInfo(licenseName, licenseUrl);
			}

			builder.build();
			
			rowIndex++;
		}
	}

	private void appendReportToSummarySheet(Workbook finalReportWorkbook, Workbook sourceReportWorkbook, WorkbookReusableComponentCache styleCache) throws ReportGenerationException {
		Sheet finalReportSummarySheet = finalReportWorkbook.getSheet(SheetNames.SUMMARY.getName());
		
		Map<String, Row> finalReportSummarySheetRows = ReportSpreadsheetUtils.getRowsByGroupId(finalReportSummarySheet);
		
		int rowIndex = SpreadsheetUtils.getFirstEmptyRowIndex(finalReportWorkbook, SheetNames.SUMMARY);
		if (finalReportSummarySheet == null) {
			throw new ReportGenerationException("Report contains no sheet named: " + SheetNames.SUMMARY);
		}
		
		Iterator<Row> sourceReportRowIterator = sourceReportWorkbook.getSheet(SheetNames.SUMMARY.getName()).iterator();
		// skip first row, header information not needed
		if (sourceReportRowIterator.hasNext()) {
			sourceReportRowIterator.next();
		}

		while (sourceReportRowIterator.hasNext()) {
			Row sourceReportRow = sourceReportRowIterator.next();
			
			if (SpreadsheetUtils.isRowEmpty(sourceReportRow, SheetNames.SUMMARY)) {
				// don't append empty rows
				continue;
			}
			
			Cell sourceReportRowFrameworkCell = sourceReportRow.getCell(SummarySheetColumns.FRAMEWORK.getColumnIndex());
			
			String sourceReportRowFrameworkCellValue = SpreadsheetUtils.getCellTextValue(sourceReportRowFrameworkCell);
			
			Row finalReportRow = finalReportSummarySheetRows.get(sourceReportRowFrameworkCellValue);
			// Final report already contains row with the same groupId. Need to decide, which row Description column value to use.
			if (finalReportRow != null) {
				String sourceDescriptionCellValue = ReportSpreadsheetUtils.getCellValue(sourceReportRow, SummarySheetColumns.DESCRIPTION.getColumnIndex());
				String destinationDescriptionCellValue = ReportSpreadsheetUtils.getCellValue(finalReportRow, SummarySheetColumns.DESCRIPTION.getColumnIndex());
				if (sourceDescriptionCellValue != null && destinationDescriptionCellValue != null) {
					// The one alphabetically less than other is the one to keep
					if (sourceDescriptionCellValue.compareTo(destinationDescriptionCellValue) < 0) {
						finalReportRow.getCell(SummarySheetColumns.DESCRIPTION.getColumnIndex()).setCellValue(sourceDescriptionCellValue);
					}
				}
				String sourceVersionsCellValue = ReportSpreadsheetUtils.getCellValue(sourceReportRow, SummarySheetColumns.VERSIONS.getColumnIndex());

				// There already exists record with the same Framework column
				// update with versions from source
				SpreadsheetUtils.updateVersionListInSummaryReport(finalReportRow, sourceVersionsCellValue);
				continue;
			}
			
			SummarySheetLineBuilder builder = new SummarySheetLineBuilder(finalReportSummarySheet, rowIndex, styleCache);
			builder.addGroupId(SpreadsheetUtils.getCellTextValue(sourceReportRow.getCell(SummarySheetColumns.FRAMEWORK.getColumnIndex())));
			builder.addDescription(SpreadsheetUtils.getCellTextValue(sourceReportRow.getCell(SummarySheetColumns.DESCRIPTION.getColumnIndex())));
			builder.addWebsiteUrl(SpreadsheetUtils.getCellTextValue(sourceReportRow.getCell(SummarySheetColumns.WEBSITE.getColumnIndex())));
			builder.addVersions(SpreadsheetUtils.getCellTextValue(sourceReportRow.getCell(SummarySheetColumns.VERSIONS.getColumnIndex())));
			Cell licenseCell = sourceReportRow.getCell(SummarySheetColumns.LICENSE.getColumnIndex());
			if (licenseCell != null) {
				String licenseName = licenseCell.getStringCellValue();
				String licenseUrl = null;
				if (licenseName != null) {
					Hyperlink hyperlink = licenseCell.getHyperlink();
					if (hyperlink != null) {
						licenseUrl = hyperlink.getAddress();
					}
				}
				builder.addLicensingInfo(licenseName, licenseUrl);
			}
			builder.build();
			rowIndex++;
		}
	}

}