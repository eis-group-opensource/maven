/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.tests.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.eisgroup.dependencyreport.core.DependencyInfoData;
import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.core.SummarySheetColumns;
import com.eisgroup.dependencyreport.utils.SpreadsheetUtils;

public class TestUtils {
	public static List<DependencyInfoData> getListOfDependencies(Path dataFilePath) {
		try {
			Document doc = Jsoup.parse(dataFilePath.toFile(), "UTF-8");
			Elements elems = doc.getElementsByTag("depinfo");
			List<DependencyInfoData> data = new ArrayList<>(elems.size());
			for (Element elem : elems) {
				String groupId = elem.getElementsByTag("groupId").get(0).text();
				String description = elem.getElementsByTag("description").get(0).text();
				DependencyInfoData d = new DependencyInfoData();
				d.setGroupId(groupId);
				d.setDescription(description);
				data.add(d);
			}
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static MavenProject mockMavenProjectParentHierarchy(String[] hierarchyIds) {
		String mavenProject = hierarchyIds[0];
		
		MavenProject lastDescendantProjectMock = mock(MavenProject.class);
		String[] idSplit = mavenProject.split(":");
		
		when(lastDescendantProjectMock.getGroupId()).thenReturn(idSplit[0]);
		when(lastDescendantProjectMock.getArtifactId()).thenReturn(idSplit[1]);
		
		if (hierarchyIds.length > 1) {
			MavenProject parentMock = null;
			MavenProject childMock = lastDescendantProjectMock;
			for (int i = 1; i < hierarchyIds.length; i++) {
				parentMock = mock(MavenProject.class);
				
				idSplit = hierarchyIds[i].split(":");
				when(parentMock.getGroupId()).thenReturn(idSplit[0]);
				when(parentMock.getArtifactId()).thenReturn(idSplit[1]);
				when(childMock.getParent()).thenReturn(parentMock);
				
				childMock = parentMock;
			}
		}
		
		return lastDescendantProjectMock;
	}
	
	public static Artifact mockArtifact(String groupId, String artifactId, String version, String scope, String type) {
		Artifact artifactMock = mock(Artifact.class);
		when(artifactMock.getGroupId()).thenReturn(groupId);
		when(artifactMock.getArtifactId()).thenReturn(artifactId);
		when(artifactMock.getVersion()).thenReturn(version);
		when(artifactMock.getScope()).thenReturn(scope);
		when(artifactMock.getType()).thenReturn(type);
		return artifactMock;
	}
	
	public static int getNumberOfFilledDataRowsInSheet(Path pathToReport, SheetNames sheetId) throws EncryptedDocumentException, InvalidFormatException, IOException {
		int rowCount = 0;

		try (InputStream is = Files.newInputStream(pathToReport);
				Workbook wb = WorkbookFactory.create(is)) {

			if (sheetId == SheetNames.DETAILED) {
				Sheet sheet = wb.getSheet(SheetNames.DETAILED.getName());
				Iterator<Row> rowIterator = sheet.iterator();
				
				rowIterator.next(); // skip first row, because it's a headers row
				
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (!SpreadsheetUtils.isCellEmpty(row.getCell(DetailedSheetColumns.GROUPID.getColumnIndex()))
							&& !SpreadsheetUtils.isCellEmpty(row.getCell(DetailedSheetColumns.DESCRIPTION.getColumnIndex()))) {
						rowCount++;
					}
				}
			} else {
				Sheet sheet = wb.getSheet(SheetNames.SUMMARY.getName());
				Iterator<Row> rowIterator = sheet.iterator();
				
				rowIterator.next(); // skip first row, because it's a headers row
				
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (!SpreadsheetUtils.isCellEmpty(row.getCell(SummarySheetColumns.FRAMEWORK.getColumnIndex()))
							&& !SpreadsheetUtils.isCellEmpty(row.getCell(SummarySheetColumns.DESCRIPTION.getColumnIndex()))) {
						rowCount++;
					}
				}
			}
		}
		
		return rowCount;
	}
	
	public static int getTotalNumberOfLinesInSheet(Path pathToReport, SheetNames sheetId) throws EncryptedDocumentException, InvalidFormatException, IOException {
		int rowCount = 0;

		try (InputStream is = Files.newInputStream(pathToReport);
				Workbook wb = WorkbookFactory.create(is)) {
			Sheet sheet = wb.getSheet(sheetId.getName());
			Iterator<Row> rowIterator = sheet.iterator();
			
			rowIterator.next(); // skip first row, because it's a headers row
			
			while (rowIterator.hasNext()) {
				rowIterator.next();
				rowCount++;
			}
		}
		
		return rowCount;
	}
	
	public static List<String> getGroupIdAndDescriptionConcatPairs(Path pathToReport, SheetNames sheetInfo) throws IOException, EncryptedDocumentException, InvalidFormatException {
		List<String> result = new ArrayList<>();
		try (InputStream is = Files.newInputStream(pathToReport);
				Workbook wb = WorkbookFactory.create(is)) {
			Sheet sheet = wb.getSheet(sheetInfo.getName());
			Iterator<Row> rows = sheet.iterator();
			
			rows.next(); // skip first row, because it's a headers row
			
			switch (sheetInfo) {
			case DETAILED:
				while (rows.hasNext()) {
					Row row = rows.next();
					if (!SpreadsheetUtils.isCellEmpty(row.getCell(DetailedSheetColumns.GROUPID.getColumnIndex()))
							&& !SpreadsheetUtils.isCellEmpty(row.getCell(DetailedSheetColumns.DESCRIPTION.getColumnIndex()))) {
						result.add(SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.GROUPID.getColumnIndex())) 
								+ SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.DESCRIPTION.getColumnIndex())));
					}
				}
				break;
			case SUMMARY:
				while (rows.hasNext()) {
					Row row = rows.next();
					if (!SpreadsheetUtils.isCellEmpty(row.getCell(SummarySheetColumns.FRAMEWORK.getColumnIndex()))
							&& !SpreadsheetUtils.isCellEmpty(row.getCell(SummarySheetColumns.DESCRIPTION.getColumnIndex()))) {
						result.add(SpreadsheetUtils.getCellTextValue(row.getCell(SummarySheetColumns.FRAMEWORK.getColumnIndex())) 
								+ SpreadsheetUtils.getCellTextValue(row.getCell(SummarySheetColumns.DESCRIPTION.getColumnIndex())));
					}
				}
				break;
			}
		}
		
		return result;
	}
	
	public static List<String> getGroupIdAndDescriptionConcatPairs(List<DependencyInfoData> dependencyData, SheetNames sheetInfo) {
		List<String> result = new ArrayList<>();
		switch (sheetInfo) {
		case DETAILED:
			for (DependencyInfoData data : dependencyData) {
				result.add(data.getGroupId() + data.getDescription());
			}
			break;
		case SUMMARY:
			for (DependencyInfoData data : dependencyData) {
				String concatResult = data.getGroupId() + data.getDescription();
				if (result.contains(concatResult)) {
					String r = result.remove(result.indexOf(concatResult));
					if (concatResult.compareTo(r) < 0) {
						result.add(concatResult);
					} else {
						result.add(r);
					}
				}
			}
			break;
		}
		
		return result;
	}
	
	public static Set<String> getUniqueGroupIds(List<DependencyInfoData> data) {
		Set<String> result = new HashSet<>();
		for (DependencyInfoData d : data) {
			result.add(d.getGroupId());
		}
		
		return result;
	}
}
