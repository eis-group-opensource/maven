package com.eisgroup.dependencyreport.utils;

import java.util.Set;

import junit.framework.TestCase;

import com.eisgroup.dependencyreport.core.DetailedSheetColumns;

public class ReportUtilsTest extends TestCase {

	public void testGetExcludeColumnsFromInputAttribute() {

		Set<DetailedSheetColumns> columnNames =
				ReportUtils.getExcludeColumnsFromInputAttribute("groupId,artifactId,webSite,license,licenseFilename,description,version,eisComponent,eisModified,dependencyPath");
		assertEquals(10, columnNames.size());


		columnNames =
				ReportUtils.getExcludeColumnsFromInputAttribute(" , groupId , artifactId , webSite ,");
		assertEquals(3, columnNames.size());

		columnNames =
				ReportUtils.getExcludeColumnsFromInputAttribute(" , groupId , groupId , groupId ,");
		assertEquals(1, columnNames.size());

		columnNames =
				ReportUtils.getExcludeColumnsFromInputAttribute(" , groupId1 , artifactId2 , webSite3 ,");
		assertEquals(0, columnNames.size());
	}
}