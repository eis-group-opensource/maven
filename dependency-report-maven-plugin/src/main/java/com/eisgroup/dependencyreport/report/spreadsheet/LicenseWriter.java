package com.eisgroup.dependencyreport.report.spreadsheet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import org.apache.maven.plugin.logging.Log;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.utils.SpreadsheetUtils;

public class LicenseWriter {

	private static final String DOT = ".";

	/**
	 * Reads detailed report sheet
	 * For each artifact with non empty license file column
	 * Creates named folders (groupId.artifactId.version) and put license files to this named folder.
 	 */
	public static void moveLicencesToNamedFolders(Path reportFilePath, Path licensesInputDirectoryPath, Path licensesOutputDirectory, Log log) {

		if(!Files.isDirectory(licensesInputDirectoryPath)) {
			throw new IllegalArgumentException("Incorrect folder path");
		}

		if(!Files.exists(licensesInputDirectoryPath)) {
			throw new IllegalArgumentException("Folder path does not exist");
		}

		if(!Files.exists(licensesOutputDirectory)) {
			try {
				Files.createDirectories(licensesOutputDirectory);
			} catch (IOException e) {
				throw new IllegalArgumentException("Unable to create output directory for licences at "+ licensesOutputDirectory.toString());
			}
		}

		if (!Files.exists(reportFilePath)) {
			log.warn("File does not exist :" + reportFilePath.toString());
			return;
		}

		try ( InputStream inputStream = Files.newInputStream(reportFilePath);
				Workbook workbook = XSSFWorkbookFactory.createWorkbook(inputStream)) {

			Sheet reportsDetailedSheet = workbook.getSheet(SheetNames.DETAILED.getName());
			if (reportsDetailedSheet == null) {
				throw new ReportGenerationException("Report contains no sheet named: " + SheetNames.DETAILED);
			}
			Iterator<Row> projectReportDetailsSheetRowIterator = reportsDetailedSheet.iterator();

			// skip first row, header information not needed
			if (projectReportDetailsSheetRowIterator.hasNext()) {
				projectReportDetailsSheetRowIterator.next();
			}

			while (projectReportDetailsSheetRowIterator.hasNext()) {
				Row projectReportDetailsSheetRow = projectReportDetailsSheetRowIterator.next();

				if (SpreadsheetUtils.isRowEmpty(projectReportDetailsSheetRow, SheetNames.DETAILED)) {
					// don't append empty rows
					continue;
				}

				String groupId = SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.GROUPID.getColumnIndex()));
				String artifactId = SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.ARTIFACTID.getColumnIndex()));
				String version = SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.VERSION.getColumnIndex()));

				if (groupId==null || artifactId==null || version==null) {
					continue;
				}

				String folderNameForArtifactLicense = groupId + DOT + artifactId;

				if (version.length() > 0) {
					folderNameForArtifactLicense = folderNameForArtifactLicense + DOT + version;
				}

				String licenseFileName = SpreadsheetUtils.getCellTextValue(projectReportDetailsSheetRow.getCell(DetailedSheetColumns.LICENSE_FILE_NAME.getColumnIndex()));

				if (licenseFileName!=null && licenseFileName.trim().length() > 0) {
					if (licenseFileName.contains("\n")) {
						// in case one artifact contains several license files separated by new line delimiter.
						String[] split = licenseFileName.split("\n");
						for (String fileName : split) {
							copyLicenseToArtifactFolder(licensesInputDirectoryPath, fileName, folderNameForArtifactLicense, licensesOutputDirectory, log);
						}
					} else {
						copyLicenseToArtifactFolder(licensesInputDirectoryPath, licenseFileName, folderNameForArtifactLicense, licensesOutputDirectory, log);
					}
				}
			}

		} catch (Exception e) {
			log.error("Copy licenses to named folder failed. " + e.getMessage(), e);
		}
	}


	private static void copyLicenseToArtifactFolder(Path licensesInputDirectoryPath, String licenseFileName, String folderNameForArtifactLicense, Path licensesOutputDirectory, Log log) throws IOException {
		try {
			Path oldLicensePath = licensesInputDirectoryPath.resolve(licenseFileName);
			if (Files.exists(oldLicensePath)) {
				Path newFolder = licensesOutputDirectory.resolve(folderNameForArtifactLicense);
				if (!Files.exists(newFolder)) {
					Files.createDirectory(newFolder);
				}
				Path newLicensePath = newFolder.resolve(licenseFileName);
				Files.copy(oldLicensePath, newLicensePath, StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (InvalidPathException ipe){
			log.error(ipe.getMessage());
		}
	}

}
