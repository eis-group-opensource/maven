/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.tests.scripts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.eisgroup.dependencyreport.core.DetailedSheetColumns;

/*
 * Used to compare the output from org.apache.maven.plugins:maven-project-info-reports-plugin:dependencies
 * to the 
 */
public class CompareReportsTest {
	private static final String exclude = "com.exigen";
	private static final String HTML_REPORT_PATH = "C:\\temp\\ipb-app\\dependencies.html";
	private static final String XLS_REPORT_PATH = "C:\\temp\\ipb-app\\dependency-report.xlsx";
	
	public static void main(String[] args) throws IOException {
		List<Artifact> artifactsFromHtmlReport = processHtmlReport();
		List<Artifact> artifactsFromXlsReport = processXlsReport();
		System.out.println("Number of artifacts from html report: " + artifactsFromHtmlReport.size());
		System.out.println("Number of artifacts from xls report: " + artifactsFromXlsReport.size());
		artifactsFromXlsReport.removeAll(artifactsFromHtmlReport);
		System.out.println("Number of artifacts in html report which do not have match in xls report: " + artifactsFromXlsReport.size());
		System.out.println("Don't match: " );
		for (Artifact artifact : artifactsFromXlsReport) {
			System.out.println("\t" + artifact);
		}
	}
	
	private static List<Artifact> processXlsReport() {
		List<Artifact> artifactsFromXlsReport = new ArrayList<>();
		try (InputStream is = Files.newInputStream(Paths.get(XLS_REPORT_PATH));
				Workbook wb = WorkbookFactory.create(is);) {
			Sheet sheet = wb.getSheet("Detailed");
			for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
				Row row = sheet.getRow(i);
				String groupId = row.getCell(DetailedSheetColumns.GROUPID.getColumnIndex()).getStringCellValue();
				String artifactId = row.getCell(DetailedSheetColumns.ARTIFACTID.getColumnIndex()).getStringCellValue();
				String version = row.getCell(DetailedSheetColumns.VERSION.getColumnIndex()).getStringCellValue();
				Artifact artifact = new Artifact();
				artifact.setArtifactId(artifactId);
				artifact.setGroupId(groupId);
				artifact.setVersion(version);
				artifactsFromXlsReport.add(artifact);
			}
			
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}
		
		return artifactsFromXlsReport;
	}

	private static List<Artifact> processHtmlReport() throws IOException {
		Document document = Jsoup.parse(new File(HTML_REPORT_PATH), "UTF-8");
		String dependencyListSelector = "#contentBox > div:nth-child(12) > table:nth-child(4) > tbody > tr";
		Elements elements = document.select(dependencyListSelector);
		List<Artifact> artifactsFromHtmlReport = new ArrayList<>();
		for (int i = 0; i < elements.size(); i++) {
			if (i == 0) { // skip header
				continue;
			}
			if (i == (elements.size() - 2)) { // skip last statistics column
				continue;
			}
			if (i == (elements.size() - 1)) { // skip last statistics column
				continue;
			}
			
			Element element = elements.get(i);
			String details[] = element.text().split(":");
			String groupId = details[0];
			if (groupId.startsWith(exclude)) {
				continue;
			}
			artifactsFromHtmlReport.add(new Artifact(details));
		}
		
		return artifactsFromHtmlReport;
	}
	
	static class Artifact {
		String artifactId;
		String groupId;
		String packaging;
		String version;
		
		public Artifact() {}
		public Artifact(String[] details) {
			if (details == null) {
				throw new IllegalArgumentException("details must not be null");
			}
			if (details.length == 5) {
				this.groupId = details[0];
				this.artifactId = details[1];
				this.packaging = details[2];
				this.version = details[4];
			} else if (details.length == 4) {
				this.groupId = details[0];
				this.artifactId = details[1];
				this.packaging = details[2];
				this.version = details[3];
			} else {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < details.length; i++) {
					builder.append(i + "(" + details[i] + ") ");
				}
				throw new IllegalArgumentException("Number of elements must be 4. Current details element: " + builder);
			}
		}
		
		public String getArtifactId() {
			return artifactId;
		}
		public void setArtifactId(String artifactId) {
			this.artifactId = artifactId;
		}
		public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
		public String getPackaging() {
			return packaging;
		}
		public void setPackaging(String packaging) {
			this.packaging = packaging;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		@Override
		public String toString() {
			return groupId + ":" + artifactId + ":" + version + ":";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
			result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
			result = prime * result + ((version == null) ? 0 : version.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Artifact other = (Artifact) obj;
			if (artifactId == null) {
				if (other.artifactId != null)
					return false;
			} else if (!artifactId.equals(other.artifactId))
				return false;
			if (groupId == null) {
				if (other.groupId != null)
					return false;
			} else if (!groupId.equals(other.groupId))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}
		
	}
}
