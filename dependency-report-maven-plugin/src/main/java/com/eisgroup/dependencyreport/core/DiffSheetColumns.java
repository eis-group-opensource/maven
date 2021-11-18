package com.eisgroup.dependencyreport.core;

public enum DiffSheetColumns {
	PARENTPROJECT(0, "Parent Project", 5000),
	GROUPID(1, "GroupId", 8000),
	ARTIFACTID(2, "ArtifactId", 8000),
	VERSION(3, "Version", 4500),
	LICENSE(4, "License name", 9000),
	LICENSE_FILE_NAME(5, "License filename", 12000),
	DEPENDENCY_PATH(6, "Dependency path", 33300);


	private int columnIndex;
	private String columnName;
	// width in poi units
	private int columnWidth;

	private DiffSheetColumns(int columnIndex, String columnName, int columnWidth) {
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
