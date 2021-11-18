package com.eisgroup.dependencyreport.core;

public enum DiffLicenseSheetColumns {
	PARENTPROJECT(0, "Parent Project", 5000),
	GROUPID(1, "GroupId", 8000),
	ARTIFACTID(2, "ArtifactId", 8000),
	VERSION(3, "Version", 4500),
	LICENSE1(4, "License name #1", 9000),
	LICENSE_FILE_NAME1(5, "License filename #1", 12000),
	LICENSE2(6, "License name #2", 9000),
	LICENSE_FILE_NAME2(7, "License filename #2", 12000),
	DEPENDENCY_PATH(8, "Dependency path", 33300);


	private int columnIndex;
	private String columnName;
	// width in poi units
	private int columnWidth;

	private DiffLicenseSheetColumns(int columnIndex, String columnName, int columnWidth) {
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
