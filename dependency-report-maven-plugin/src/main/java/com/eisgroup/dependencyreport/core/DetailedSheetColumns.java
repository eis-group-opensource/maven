/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.core;

import java.util.Arrays;
import java.util.Optional;

public enum DetailedSheetColumns {
	GROUPID(0, "GroupId", 7212,"groupId"),
	ARTIFACTID(1, "ArtifactId", 7327,"artifactId"),
	WEBSITE(2, "Web Site", 10877,"webSite"),
	LICENSE(3, "License", 5667,"license"),
	LICENSE_FILE_NAME(4, "License filename", 5667,"licenseFilename"),
	DESCRIPTION(5, "Description", 14800,"description"),
	VERSION(6, "Version", 2048,"version"),
	EISCOMPONENT(7,"EIS Component", 4121,"eisComponent"),
	MODIFIED(8, "EIS modified", 3000,"eisModified"),
	DEPENDENCY_PATH(9, "Dependency path", 15086,"dependencyPath");

	private int columnIndex;
	private String columnName;
	// width in poi units
	private int columnWidth;

	private String columnShortName;

	private DetailedSheetColumns(int columnIndex, String columnName, int columnWidth, String columnShortName) {
		this.columnIndex = columnIndex;
		this.columnName = columnName;
		this.columnWidth = columnWidth;
		this.columnShortName = columnShortName;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public String getColumnShortName() {
		return columnShortName;
	}

	public static Optional<DetailedSheetColumns> fromColumnName(String columnName) {
		return Arrays.stream(DetailedSheetColumns.values()).filter(s->columnName.equalsIgnoreCase(s.getColumnShortName())).findFirst();
	}
}