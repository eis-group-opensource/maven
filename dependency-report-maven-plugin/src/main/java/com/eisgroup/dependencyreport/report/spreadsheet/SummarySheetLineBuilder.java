/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.report.spreadsheet;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.eisgroup.dependencyreport.core.SummarySheetColumns;
import com.eisgroup.dependencyreport.report.spreadsheet.WorkbookReusableComponentCache.CellStyles;

/*
 * Builds row in Summary sheet using data provided
 */
public class SummarySheetLineBuilder {
	private WorkbookReusableComponentCache workbookComponentCache;
	private Sheet summarySheet;
	private int rowIndex;
	
	private String groupId;
	private String description;
	private String licenseName;
	private String licenseUrl;
	private String websiteUrl;
	private String versions;
	
	public SummarySheetLineBuilder(Sheet sheet, int rowIndex, WorkbookReusableComponentCache workbookComponentCache) {
		this.summarySheet = sheet;
		this.workbookComponentCache = workbookComponentCache;
		this.rowIndex = rowIndex;
	}
	
	public SummarySheetLineBuilder addGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}
	
	public SummarySheetLineBuilder addDescription(String description) {
		this.description = description;
		return this;
	}
	
	public SummarySheetLineBuilder addLicensingInfo(String licenseName, String licenseUrl) {
		this.licenseName = licenseName;
		this.licenseUrl = licenseUrl;
		return this;
	}
	
	public SummarySheetLineBuilder addWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
		return this;
	}

	public SummarySheetLineBuilder addVersions(String versions) {
		this.versions = versions;
		return this;
	}
	
	public void build() {
		Row row = summarySheet.createRow(rowIndex);
		
		Cell frameworkCell = row.createCell(SummarySheetColumns.FRAMEWORK.getColumnIndex());
		frameworkCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		if (groupId != null) {
			frameworkCell.setCellValue(groupId);
		}
		
		Cell websiteCell = row.createCell(SummarySheetColumns.WEBSITE.getColumnIndex());
		websiteCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		if (websiteUrl != null) {
			websiteCell.setCellValue(websiteUrl);
		}
		
		Cell descriptionCell = row.createCell(SummarySheetColumns.DESCRIPTION.getColumnIndex());
		descriptionCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		if (description != null) {
			descriptionCell.setCellValue(description);
		}
		
		Cell licenseCell = row.createCell(SummarySheetColumns.LICENSE.getColumnIndex());
		licenseCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_HYPERLINK));
		
		if (licenseName != null) {
			licenseCell.setCellValue(licenseName);
			if (licenseUrl != null) {
				try {
					CreationHelper helper = this.summarySheet.getWorkbook().getCreationHelper();
					Hyperlink hyperlink = helper.createHyperlink(HyperlinkType.URL);
					hyperlink.setAddress(this.licenseUrl);
					licenseCell.setHyperlink(hyperlink);
				} catch (IllegalArgumentException e) {
					// file://${basedir}/LICENSE
					licenseCell.setCellValue(this.licenseName.concat(" (").concat(this.licenseUrl).concat(")"));
				}
			}
		}
		Cell versionsCell = row.createCell(SummarySheetColumns.VERSIONS.getColumnIndex());
		versionsCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		if (versions != null) {
			versionsCell.setCellValue(versions);
		}
	}
}
