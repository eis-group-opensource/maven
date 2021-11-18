/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.report.spreadsheet;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.report.spreadsheet.WorkbookReusableComponentCache.CellStyles;
import com.eisgroup.dependencyreport.utils.SpreadsheetUtils;

/*
 * Builds row in Detailed sheet using data provided
 */
public class DetailedSheetLineBuilder {
	private WorkbookReusableComponentCache workbookComponentCache;
	private Sheet detailsSheet;
	private int rowIndex;
	
	private String groupId;
	private String artifactId;
	private String version;
	private String eisComponent;
	private String dependencyPath;
	private String licenseName;
	private String licenseUrl;
	private String licenseFilename;
	private String description;
	private String websiteUrl;
	private Boolean modified;
	
	public DetailedSheetLineBuilder(Sheet detailsSheet, int rowIndex, WorkbookReusableComponentCache workbookComponentCache) {
		this.detailsSheet = detailsSheet;
		this.workbookComponentCache = workbookComponentCache;
		this.rowIndex = rowIndex;
	}
	
	public DetailedSheetLineBuilder addGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}
	
	public DetailedSheetLineBuilder addArtifactId(String artifactId) {
		this.artifactId = artifactId;
		return this;
	}
	
	public DetailedSheetLineBuilder addVersion(String version) {
		this.version = version;
		return this;
	}
	
	public DetailedSheetLineBuilder addEisComponent(String eisComponent) {
		this.eisComponent = eisComponent;
		return this;
	}
	
	public DetailedSheetLineBuilder addDependencyPath(String dependencyPath) {
		this.dependencyPath = dependencyPath;
		return this;
	}
	
	public DetailedSheetLineBuilder addLicensingInfo(String licenseName, String licenseUrl) {
		this.licenseName = licenseName;
		this.licenseUrl = licenseUrl;
		return this;
	}

	public DetailedSheetLineBuilder addLicenseFilename(String licenseFilename) {
		this.licenseFilename = licenseFilename;
		return this;
	}

	public DetailedSheetLineBuilder addDescription(String description) {
		this.description = description;
		return this;
	}
	
	public DetailedSheetLineBuilder addWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
		return this;
	}

	public DetailedSheetLineBuilder addModified(Boolean modified) {
		this.modified = modified;
		return this;
	}

	public void build() {
		Row row = detailsSheet.createRow(rowIndex);
		
		Cell groupIdCell = row.createCell(DetailedSheetColumns.GROUPID.getColumnIndex());
		groupIdCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		groupIdCell.setCellValue(groupId);
		
		Cell artifactIdCell = row.createCell(DetailedSheetColumns.ARTIFACTID.getColumnIndex());
		artifactIdCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		artifactIdCell.setCellValue(artifactId);

		Cell versionCell = row.createCell(DetailedSheetColumns.VERSION.getColumnIndex());
		versionCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		versionCell.setCellValue(version);
		
		Cell eisComponentCell = row.createCell(DetailedSheetColumns.EISCOMPONENT.getColumnIndex());
		eisComponentCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		eisComponentCell.setCellValue(eisComponent);


		Cell modifiedCell = row.createCell(DetailedSheetColumns.MODIFIED.getColumnIndex());
		modifiedCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		modifiedCell.setCellValue(modified ? SpreadsheetUtils.VALUE_YES : SpreadsheetUtils.VALUE_EMPTY);

		Cell dependencyPathCell = row.createCell(DetailedSheetColumns.DEPENDENCY_PATH.getColumnIndex());
		dependencyPathCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		dependencyPathCell.setCellValue(dependencyPath);
		
		Cell licenseCell = row.createCell(DetailedSheetColumns.LICENSE.getColumnIndex());
		licenseCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_HYPERLINK));
		
		if (this.licenseName != null) {
			licenseCell.setCellValue(this.licenseName);
			if (this.licenseUrl != null) {
				try {
					CreationHelper helper = this.detailsSheet.getWorkbook().getCreationHelper();
					Hyperlink hyperlink = helper.createHyperlink(HyperlinkType.URL);
					hyperlink.setAddress(this.licenseUrl);
					licenseCell.setHyperlink(hyperlink);
				} catch (IllegalArgumentException e) {
					// file://${basedir}/LICENSE
					licenseCell.setCellValue(this.licenseName.concat(" (").concat(this.licenseUrl).concat(")"));
				}
			}
		}

		Cell licenseFileNameCell = row.createCell(DetailedSheetColumns.LICENSE_FILE_NAME.getColumnIndex());
		licenseFileNameCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		licenseFileNameCell.setCellValue(licenseFilename);


		Cell descriptionCell = row.createCell(DetailedSheetColumns.DESCRIPTION.getColumnIndex());
		descriptionCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		if (description != null) {
			descriptionCell.setCellValue(description);
		}
		
		Cell websiteCell = row.createCell(DetailedSheetColumns.WEBSITE.getColumnIndex());
		websiteCell.setCellStyle(workbookComponentCache.getCellStyle(CellStyles.CELL_STYLE_SIMPLE_WRAPPED));
		if (websiteUrl != null) {
			websiteCell.setCellValue(websiteUrl);
		}

	}
}
