/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.core;

public enum SummarySheetColumns {
	FRAMEWORK(0, "Framework", 7814),
	WEBSITE(1, "Web Site", 11020),
	LICENSE(2, "License", 9760),
	DESCRIPTION(3, "Description", 21153),
	VERSIONS(4, "Versions", 21153);

	private int columnIndex;
	private String columnName;
	// width in poi units
	private int columnWidth;
	
	private SummarySheetColumns(int columnIndex, String columnName, int columnWidth) {
		this.columnIndex = columnIndex;
		this.columnName = columnName;
		this.columnWidth = columnWidth;
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
}