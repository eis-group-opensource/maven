/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.tests.scripts;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.core.SummarySheetColumns;
/*
 * Used to get precise column width sizes from workbook.
 */
public class GetColumnSizes {
	private static Path pathToReport = Paths.get("C:\\temp\\sheet\\data.xls");

	public static void main(String[] args) {
		try (InputStream is = Files.newInputStream(pathToReport);
				Workbook wb = WorkbookFactory.create(is);) {
			Sheet detailedSheet = wb.getSheet(SheetNames.DETAILED.getName());
			System.out.println("Detailed----------------------");
			for (DetailedSheetColumns column : DetailedSheetColumns.values()) {
				System.out.println(column.getColumnName() + ": " + detailedSheet.getColumnWidth(column.getColumnIndex()));
			}
			
			Sheet summarySheet = wb.getSheet(SheetNames.SUMMARY.getName());
			System.out.println("Summary----------------------");
			for (SummarySheetColumns column : SummarySheetColumns.values()) {
				System.out.println(column.getColumnName() + ": " + summarySheet.getColumnWidth(column.getColumnIndex()));
			}
		} catch (IOException | EncryptedDocumentException e) {
			e.printStackTrace();
		}
	}
}
