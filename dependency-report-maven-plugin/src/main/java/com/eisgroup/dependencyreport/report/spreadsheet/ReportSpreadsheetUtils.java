/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.report.spreadsheet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.DiffLicenseSheetColumns;
import com.eisgroup.dependencyreport.core.DiffSheetColumns;
import com.eisgroup.dependencyreport.core.DiffVersionSheetColumns;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.core.SummarySheetColumns;
import com.eisgroup.dependencyreport.utils.SpreadsheetUtils;

public class ReportSpreadsheetUtils {
	public static Map<String, Row> getRowsByGroupId(Sheet sheet) {
		Iterator<Row> rowIterator = sheet.iterator();
		Map<String, Row> sheetRows = new HashMap<>(); 
		
		rowIterator.next();
		
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			
			if (row.getCell(SummarySheetColumns.FRAMEWORK.getColumnIndex()) != null) {
				Cell cell = row.getCell(SummarySheetColumns.FRAMEWORK.getColumnIndex());
				if (!SpreadsheetUtils.isCellEmpty(cell)) {
					sheetRows.put(cell.getStringCellValue(), row);
				}
			} else {
				// 
			}
		}
		
		return sheetRows;
	}


	public static Boolean isEqualRowInDetailedReportExists(Row searchRow, Sheet sheet) {
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (equalsRows(row, searchRow)) {
				return true;
			}
		}
		return false;
	}

	public static Boolean isEqualRowInDetailedReportExistsWithExcludes(Row searchRow, Sheet sheet, Set<DetailedSheetColumns> excludeColumns) {
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();
		while (rowIterator.hasNext()) {
			Row currentRow = rowIterator.next();
			if (excludeColumns.size() > 0 && equalsRowsExcluding(currentRow, searchRow, excludeColumns)) {
				return true;
			} else if (equalsRows(currentRow, searchRow)) {
				return true;
			}
		}
		return false;
	}

	private static boolean equalsRowsExcluding(Row row1, Row row2, Set<DetailedSheetColumns> excludeColumns) {
		for (DetailedSheetColumns detailedSheetColumn : Arrays.stream(DetailedSheetColumns.values()).filter(i->! excludeColumns.contains(i)).collect(Collectors.toSet())) {
			if (!equalsCell(row1.getCell(detailedSheetColumn.getColumnIndex()), row2.getCell(detailedSheetColumn.getColumnIndex()))) {
				return false;
			}
		}
		return true;
	}

	private static boolean equalsCell(Cell cell1, Cell cell2) {
		if (cell1==null && cell2 ==null) {
			return true;
		}
		if (cell1 != null && String.valueOf(cell1).equals(SpreadsheetUtils.VALUE_EMPTY) && cell2 == null) {
			return true;
		}
		if (cell1 == null && cell2!=null && String.valueOf(cell2).equals(SpreadsheetUtils.VALUE_EMPTY)) {
			return true;
		}

		if (cell1!=null && cell2 !=null && String.valueOf(cell1).equals(String.valueOf(cell2))) {
			return true;
		}
		return false;
	}

	private static boolean equalsRows(Row row1, Row row2) {
		for (DetailedSheetColumns detailedSheetColumn : DetailedSheetColumns.values()) {
			if (!equalsCell(row1.getCell(detailedSheetColumn.getColumnIndex()), row2.getCell(detailedSheetColumn.getColumnIndex()))) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Sometimes description for some reason contains new line or multiple whitespace characters.
	 * Replace those with single space character to make it one-liner.
	 */
	public static String cleanDescriptionValue(String description) {
		if (description != null) {
			description = description.replace("\n", " ").replaceAll("\\s{2,}", " ");
		}
		
		return description;
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
	
	public static String getCellValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell != null) {
			String cellValue = cell.getStringCellValue();
			if (cellValue != null) {
				return cellValue;
			}
		}
		return SpreadsheetUtils.VALUE_EMPTY;
	}
	
	public static void applyFormatting(Workbook workbook) {
		Sheet detailedSheet = workbook.getSheet(SheetNames.DETAILED.getName());
		Sheet summarySheet = workbook.getSheet(SheetNames.SUMMARY.getName());

		if (detailedSheet != null) {
			for (DetailedSheetColumns column : DetailedSheetColumns.values()) {
				detailedSheet.setColumnWidth(column.getColumnIndex(), column.getColumnWidth());
			}
		}
		if (summarySheet != null) {
			for (SummarySheetColumns column : SummarySheetColumns.values()) {
				summarySheet.setColumnWidth(column.getColumnIndex(), column.getColumnWidth());
			}
		}
	}


	public static void applyFormattingDiffReport(Workbook workbook) {
		Sheet newItemsSheet = workbook.getSheet(SheetNames.NEW_ITEMS.getName());
		Sheet removedSheet = workbook.getSheet(SheetNames.REMOVED_ITEMS.getName());
		Sheet versionSheet = workbook.getSheet(SheetNames.VERSION_CHANGES.getName());
		Sheet licenseSheet = workbook.getSheet(SheetNames.LICENSE_CHANGES.getName());

		if (newItemsSheet != null) {
			for (DiffSheetColumns column : DiffSheetColumns.values()) {
				newItemsSheet.setColumnWidth(column.getColumnIndex(), column.getColumnWidth());
			}
		}

		if (removedSheet != null) {
			for (DiffSheetColumns column : DiffSheetColumns.values()) {
				removedSheet.setColumnWidth(column.getColumnIndex(), column.getColumnWidth());
			}
		}

		if (versionSheet != null) {
			for (DiffVersionSheetColumns column : DiffVersionSheetColumns.values()) {
				versionSheet.setColumnWidth(column.getColumnIndex(), column.getColumnWidth());
			}
		}

		if (licenseSheet != null) {
			for (DiffLicenseSheetColumns column : DiffLicenseSheetColumns.values()) {
				licenseSheet.setColumnWidth(column.getColumnIndex(), column.getColumnWidth());
			}
		}
	}
}