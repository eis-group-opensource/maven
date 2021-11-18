/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.report.spreadsheet;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eisgroup.dependencyreport.core.DependencyInfoData;
import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.core.SummarySheetColumns;
import com.eisgroup.dependencyreport.report.spreadsheet.WorkbookReusableComponentCache.CellStyles;
import com.eisgroup.dependencyreport.utils.SpreadsheetUtils;

public class SpreadsheetReportWriter {
	private List<DependencyInfoData> dependencies;
	private Log log;
	
	public SpreadsheetReportWriter(List<DependencyInfoData> dependencies, Log log) {
		this.dependencies = new ArrayList<>(dependencies);
		this.log = log;
	}
	
	public void writeReport(Path projectReportPath) throws ReportGenerationException {
		// Sort first by groupId then by description.
		Collections.sort(dependencies, new Comparator<DependencyInfoData>() {
			@Override
			public int compare(DependencyInfoData o1, DependencyInfoData o2) {
				String id1 = o1.getGroupId() + ":" + o1.getDescription();
				String id2 = o2.getGroupId() + ":" + o2.getDescription();
				return id1.compareToIgnoreCase(id2);
			}
		});
		// generate workbook from data provided
		Workbook projectReportWorkbook = buildReport();
		
		SpreadsheetUtils.writeWorkbookToFile(projectReportWorkbook, projectReportPath, log);
	}

	private Workbook buildReport() {
		Workbook workbook = new XSSFWorkbook();
		WorkbookReusableComponentCache workbookComponentCache = new WorkbookReusableComponentCache(workbook);
		
		Sheet detailedSheet = workbook.createSheet(SheetNames.DETAILED.getName());
		addDetailedSheetHeaderColumns(detailedSheet, workbookComponentCache);
		
		Sheet summarySheet = workbook.createSheet(SheetNames.SUMMARY.getName());
		addSummarySheetHeaderColumns(summarySheet, workbookComponentCache);
		
		int detailedSheetRowIndex = 1;
		int summarySheetRowIndex = 1;
		// holds processed groupIds. Detailed sheet must contain data with unique groupId column

		HashMap<String,Integer> groupIdIndexMap = new HashMap<>();
		
		for (DependencyInfoData dependency : dependencies) {
			addRowToDetailedSheet(detailedSheet, workbookComponentCache, detailedSheetRowIndex, dependency);
			detailedSheetRowIndex++;
			if (!groupIdIndexMap.keySet().contains(dependency.getGroupId())) {
				addRowToSummarySheet(summarySheet, workbookComponentCache, summarySheetRowIndex, dependency);
				groupIdIndexMap.put(dependency.getGroupId(), summarySheetRowIndex);
				summarySheetRowIndex++;
			} else {
				Row row = summarySheet.getRow(groupIdIndexMap.get(dependency.getGroupId()));
				SpreadsheetUtils.updateVersionListInSummaryReport(row, dependency.getVersion());
			}
		}
		
		ReportSpreadsheetUtils.applyFormatting(workbook);
		
		return workbook;
	}

	private void addDetailedSheetHeaderColumns(Sheet detailedSheet, WorkbookReusableComponentCache workbookComponentCache) {
		Row row = detailedSheet.createRow(0);
		for (DetailedSheetColumns column : DetailedSheetColumns.values()) {
			Cell headerColumnCell = row.createCell(column.getColumnIndex());
			headerColumnCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_BOLD_WRAPPED));
			headerColumnCell.setCellValue(column.getColumnName());
		}
		detailedSheet.createFreezePane(DetailedSheetColumns.values().length, 1);
	}
	
	private void addSummarySheetHeaderColumns(Sheet summarySheet, WorkbookReusableComponentCache workbookComponentCache) {
		Row row = summarySheet.createRow(0);
		for (SummarySheetColumns column : SummarySheetColumns.values()) {
			Cell headerColumnCell = row.createCell(column.getColumnIndex());
			headerColumnCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_BOLD_WRAPPED));
			headerColumnCell.setCellValue(column.getColumnName());
		}
		summarySheet.createFreezePane(DetailedSheetColumns.values().length, 1);
	}
	
	private void addRowToDetailedSheet(Sheet detailedSheet, WorkbookReusableComponentCache workbookComponentCache, int rowIndex, DependencyInfoData dependency) {
		DetailedSheetLineBuilder builder = new DetailedSheetLineBuilder(detailedSheet, rowIndex, workbookComponentCache);
		builder.addGroupId(dependency.getGroupId());
		builder.addArtifactId(dependency.getArtifactId());
		builder.addVersion(dependency.getVersion());
		builder.addEisComponent(dependency.getRootProjectName());
		builder.addModified(dependency.getModified());
		builder.addDependencyPath(dependency.getDependencyPath());
		builder.addWebsiteUrl(dependency.getUrl());
		builder.addDescription(dependency.getDescription());
		builder.addLicensingInfo(dependency.getLicenseName(), dependency.getLicenseUrl());
		builder.addLicenseFilename(dependency.getLicenseFileName());
		builder.build();
	}
	
	private void addRowToSummarySheet(Sheet detailedSheet, WorkbookReusableComponentCache workbookComponentCache, int rowIndex, DependencyInfoData dependencyNode) {
		SummarySheetLineBuilder reportLineBuilder = new SummarySheetLineBuilder(detailedSheet, rowIndex, workbookComponentCache);
		reportLineBuilder.addGroupId(dependencyNode.getGroupId());
		String licenseNameOverride = dependencyNode.getLicenseNameOverride();
		String licenseUrlOverride = dependencyNode.getLicenseUrlOverride();
		if (licenseNameOverride != null || licenseUrlOverride != null) {
			reportLineBuilder.addLicensingInfo(licenseNameOverride, licenseUrlOverride);
		} else {
			reportLineBuilder.addLicensingInfo(dependencyNode.getLicenseName(), dependencyNode.getLicenseUrl());
		}
		if (dependencyNode.getDescriptionOverride() != null) {
			reportLineBuilder.addDescription(dependencyNode.getDescriptionOverride());
		} else { 
			reportLineBuilder.addDescription(dependencyNode.getDescription());
		}
		if (dependencyNode.getUrlOverride() != null) {
			reportLineBuilder.addWebsiteUrl(dependencyNode.getUrlOverride());
		} else {
			reportLineBuilder.addWebsiteUrl(dependencyNode.getUrl());
		}
		reportLineBuilder.addVersions(dependencyNode.getVersion());

		reportLineBuilder.build();
	}
}