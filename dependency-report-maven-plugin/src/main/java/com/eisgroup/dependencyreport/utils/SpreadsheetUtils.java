/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.eisgroup.dependencyreport.core.DependencyInfoData;
import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.DiffLicenseSheetColumns;
import com.eisgroup.dependencyreport.core.DiffSheetColumns;
import com.eisgroup.dependencyreport.core.DiffVersionSheetColumns;
import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.core.SummarySheetColumns;

public class SpreadsheetUtils {

	public static final String VALUE_YES = "Yes";
	public static final String VALUE_EMPTY = "";

	public static boolean isCellEmpty(Cell cell) {
		if (cell == null) {
			return true;
		}

		String cellStringValue = cell.getStringCellValue();
		if (cellStringValue == null || cellStringValue.trim().length() == 0) {
			return true;
		}

		return false;
	}

	public static boolean isUrlCellEmpty(Cell cell) {
		if (cell == null) {
			return true;
		}

		String cellStringValue = cell.getStringCellValue();
		if (cellStringValue == null || cellStringValue.trim().length() == 0) {
			if (cell.getHyperlink() == null || cell.getHyperlink().getAddress() == null) {
				return true;
			}
		}

		return false;
	}

	public static Map<String, Row> getSheetRows(Path spreadsheetPath, int idColumnIndex, SheetNames sheetId, Log log) throws Exception {

		Map<String, Row> result = new HashMap<>();
		if (!Files.exists(spreadsheetPath)) {
			log.info("Cannot find spreadsheet file. Location of the spreadsheet file: " + spreadsheetPath);
			return result;
		}

		try (InputStream inputStream = Files.newInputStream(spreadsheetPath); Workbook workbook = WorkbookFactory.create(inputStream)) {
			Sheet sheet = workbook.getSheet(sheetId.getName());
			if (sheet == null) {
				throw new ReportGenerationException("Sheet with name '" + sheetId.getName() + "' was not found. Check information in the spreadsheet: " + spreadsheetPath);
			}
			Iterator<Row> inputSummarySheetRowIterator = sheet.iterator();

			while (inputSummarySheetRowIterator.hasNext()) {
				Row row = inputSummarySheetRowIterator.next();
				if (SpreadsheetUtils.isRowEmpty(row, sheetId)) {
					// Skip empty rows
					continue;
				}
				Cell idCell = row.getCell(idColumnIndex);
				if (isCellEmpty(idCell)) {
					log.error("Skipping row, because found row with it column empty.");
					continue;
				}
				result.put(idCell.getStringCellValue(), row);
			}
		} catch (Exception e) {
			log.error("Some errors occured while processing spreadsheet file. ", e);
			throw e;
		}

		return result;
	}

	public static void updateDiffRowWithArtifactData(DependencyInfoData dependencyInfoData, Row row) {
		row.createCell(DiffSheetColumns.PARENTPROJECT.getColumnIndex()).setCellValue(dependencyInfoData.getRootProjectName());
		row.createCell(DiffSheetColumns.GROUPID.getColumnIndex()).setCellValue(dependencyInfoData.getGroupId());
		row.createCell(DiffSheetColumns.ARTIFACTID.getColumnIndex()).setCellValue(dependencyInfoData.getArtifactId());
		row.createCell(DiffSheetColumns.VERSION.getColumnIndex()).setCellValue(dependencyInfoData.getVersion());
		row.createCell(DiffSheetColumns.LICENSE.getColumnIndex()).setCellValue(dependencyInfoData.getLicenseName());
		row.createCell(DiffSheetColumns.LICENSE_FILE_NAME.getColumnIndex()).setCellValue(dependencyInfoData.getLicenseFileName());
		row.createCell(DiffSheetColumns.DEPENDENCY_PATH.getColumnIndex()).setCellValue(dependencyInfoData.getDependencyPath());
	}

	public static void updateDiffRowWithPreviousVersionData(DependencyInfoData dependencyInfoData, String version1, Row row) {
		row.createCell(DiffVersionSheetColumns.PARENTPROJECT.getColumnIndex()).setCellValue(dependencyInfoData.getRootProjectName());
		row.createCell(DiffVersionSheetColumns.GROUPID.getColumnIndex()).setCellValue(dependencyInfoData.getGroupId());
		row.createCell(DiffVersionSheetColumns.ARTIFACTID.getColumnIndex()).setCellValue(dependencyInfoData.getArtifactId());
		row.createCell(DiffVersionSheetColumns.VERSION1.getColumnIndex()).setCellValue(version1);
		row.createCell(DiffVersionSheetColumns.VERSION2.getColumnIndex()).setCellValue(dependencyInfoData.getVersion());
		row.createCell(DiffVersionSheetColumns.LICENSE.getColumnIndex()).setCellValue(dependencyInfoData.getLicenseName());
		row.createCell(DiffVersionSheetColumns.LICENSE_FILE_NAME.getColumnIndex()).setCellValue(dependencyInfoData.getLicenseFileName());
		row.createCell(DiffVersionSheetColumns.DEPENDENCY_PATH.getColumnIndex()).setCellValue(dependencyInfoData.getDependencyPath());
	}

	public static void updateDiffRowWithPreviousLicenseData(DependencyInfoData dependencyInfoData, String licenseName1, String licenseFileName1, Row row) {
		row.createCell(DiffLicenseSheetColumns.PARENTPROJECT.getColumnIndex()).setCellValue(dependencyInfoData.getRootProjectName());
		row.createCell(DiffLicenseSheetColumns.GROUPID.getColumnIndex()).setCellValue(dependencyInfoData.getGroupId());
		row.createCell(DiffLicenseSheetColumns.ARTIFACTID.getColumnIndex()).setCellValue(dependencyInfoData.getArtifactId());
		row.createCell(DiffLicenseSheetColumns.VERSION.getColumnIndex()).setCellValue(dependencyInfoData.getVersion());
		row.createCell(DiffLicenseSheetColumns.LICENSE1.getColumnIndex()).setCellValue(licenseName1);
		row.createCell(DiffLicenseSheetColumns.LICENSE_FILE_NAME1.getColumnIndex()).setCellValue(licenseFileName1);
		row.createCell(DiffLicenseSheetColumns.LICENSE2.getColumnIndex()).setCellValue(dependencyInfoData.getLicenseName());
		row.createCell(DiffLicenseSheetColumns.LICENSE_FILE_NAME2.getColumnIndex()).setCellValue(dependencyInfoData.getLicenseFileName());
		row.createCell(DiffLicenseSheetColumns.DEPENDENCY_PATH.getColumnIndex()).setCellValue(dependencyInfoData.getDependencyPath());
	}

	public static void updateData(DependencyInfoData dependencyInfoData, Row row) {
		Cell descriptionCell = row.getCell(SummarySheetColumns.DESCRIPTION.getColumnIndex());
		if (!SpreadsheetUtils.isCellEmpty(descriptionCell)) {
			dependencyInfoData.setDescriptionOverride(descriptionCell.getStringCellValue());
		}

		Cell websiteCell = row.getCell(SummarySheetColumns.WEBSITE.getColumnIndex());
		if (!SpreadsheetUtils.isCellEmpty(websiteCell)) {
			dependencyInfoData.setUrlOverride(websiteCell.getStringCellValue());
		}

		Cell licenseCell = row.getCell(SummarySheetColumns.LICENSE.getColumnIndex());
		if (!SpreadsheetUtils.isUrlCellEmpty(licenseCell)) {
			String licenseCellValue = licenseCell.getStringCellValue();
			if (licenseCellValue != null && licenseCellValue.trim().length() != 0) {
				dependencyInfoData.setLicenseNameOverride(licenseCellValue);
			}
			Hyperlink hyperlink = licenseCell.getHyperlink();
			if (hyperlink != null && hyperlink.getAddress() != null) {
				dependencyInfoData.setLicenseUrlOverride(hyperlink.getAddress());
			}
		}

		Cell versionsCell = row.getCell(SummarySheetColumns.VERSIONS.getColumnIndex());
		if (!SpreadsheetUtils.isCellEmpty(versionsCell)) {
			dependencyInfoData.setUrlOverride(versionsCell.getStringCellValue());
		}
	}

	public static String getCellTextValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			return null;
		}

		return cell.getStringCellValue();
	}

	public static String getCellTextValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		return cell.getStringCellValue();
	}

	public static void writeWorkbookToFile(Workbook workbook, Path reportPath, Log log) throws ReportGenerationException {
		try (OutputStream outputStream = Files.newOutputStream(reportPath)) {
			workbook.write(outputStream);
		} catch (IOException e) {
			log.error("Error occured while writing to spreadsheet file.", e); //TODO: add field of type Path to Exception
			throw new ReportGenerationException(e);
		}
	}

	public static int getFirstEmptyRowIndex(Workbook wb, SheetNames sheet) {
		int index = 0;
		if (sheet == SheetNames.DETAILED) {
			Sheet detailedSheet = wb.getSheet(sheet.getName());
			Iterator<Row> rowIterator = detailedSheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (isRowEmpty(row, SheetNames.DETAILED)) {
					break;
				}
				index++;
			}
		} else {
			Sheet summarySheet = wb.getSheet(sheet.getName());
			Iterator<Row> rowIterator = summarySheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (isRowEmpty(row, SheetNames.SUMMARY)) {
					break;
				}
				index++;
			}
		}

		return index;
	}

	public static boolean isRowEmpty(Row row, SheetNames sheetId) {
		if (SheetNames.DETAILED == sheetId) {
			for (DetailedSheetColumns column : DetailedSheetColumns.values()) {
				Cell cell = row.getCell(column.getColumnIndex());
				if (!SpreadsheetUtils.isCellEmpty(cell)) {
					return false;
				}
			}
		} else {
			for (SummarySheetColumns column : SummarySheetColumns.values()) {
				Cell cell = row.getCell(column.getColumnIndex());
				if (!SpreadsheetUtils.isCellEmpty(cell)) {
					return false;
				}
			}
		}

		return true;
	}

	public static void updateWithDataFromInputSpreadsheet(Path inputReportPath, List<DependencyInfoData> dependencyInfoDataList, Log log) throws Exception {
		Map<String, Row> rowMap = SpreadsheetUtils.getSheetRows(inputReportPath, SummarySheetColumns.FRAMEWORK.getColumnIndex(), SheetNames.SUMMARY, log);
		log.info("Found " + rowMap.size() + " rows of data in input sheet.");
		for (DependencyInfoData dependencyInfoData : dependencyInfoDataList) {
			if (rowMap.containsKey(dependencyInfoData.getGroupId())) {
				Row row = rowMap.get(dependencyInfoData.getGroupId());
				SpreadsheetUtils.updateData(dependencyInfoData, row);
			}
		}
	}

	/**
	 * Versions column in summary report should contain list of used version for groupId
	 * Method adds new version (or versions separated by comma) value to Version column separated by comma.
	 * @param row
	 * @param versions
	 */
	public static void updateVersionListInSummaryReport(Row row, String versions) {
		Cell cell = row.getCell(SummarySheetColumns.VERSIONS.getColumnIndex());
		if (cell == null)
		{
			cell = row.createCell(SummarySheetColumns.VERSIONS.getColumnIndex());
		}

		if (StringUtils.isEmpty(cell.getStringCellValue())) {
			cell.setCellValue(versions);
			return;
		}

		Set<String> versionsSet = new TreeSet<>();
		for (String versionString : cell.getStringCellValue().split(",")) {
			versionsSet.add(versionString.trim());
		}
		if (!StringUtils.isEmpty(versions)) {
			for (String v : versions.split(",")) {
				if (!StringUtils.isEmpty(v.trim())) {
					versionsSet.add(v.trim());
				}
			}
		}
		cell.setCellValue(versionsSet.stream().collect(Collectors.joining(", ")));
	}
}
