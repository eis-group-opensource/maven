package com.eisgroup.dependencyreport.core;

public enum DiffVersionSheetColumns {
	PARENTPROJECT(0, "Parent Project", 5000),
	GROUPID(1, "GroupId", 8000),
	ARTIFACTID(2, "ArtifactId", 8000),
	VERSION1(3, "Version #1", 4500),
	VERSION2(4, "Version #2", 4500),
	LICENSE(5, "License name", 9000),
	LICENSE_FILE_NAME(6, "License filename", 12000),
	DEPENDENCY_PATH(7, "Dependency path", 33300);

	private int columnIndex;
	private String columnName;
	// width in poi units
	private int columnWidth;

	private DiffVersionSheetColumns(int columnIndex, String columnName, int columnWidth) {
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
