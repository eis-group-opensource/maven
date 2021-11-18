package com.eisgroup.dependencyreport.test;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import junit.framework.TestCase;
import org.apache.maven.plugin.logging.Log;

import com.eisgroup.dependencyreport.report.spreadsheet.LicenseWriter;

public class TestLicenseWriter extends TestCase {

	private Log log = new LogMock();

	private static final String REPORT_NAME = "dependency-report-licenses.xlsx";
	private static final String INPUT_FOLDERNAME = "licenses-folder-1";
	private static final String OUTPUT_FOLDERNAME = "licenses-folder-2";

	public void testMoveLicencesToNamedFolders() throws URISyntaxException {
		Path reportFilePath = Paths.get(getClass().getClassLoader().getResource(REPORT_NAME).toURI());
		Path licensesInputFolderPath = Paths.get(getClass().getClassLoader().getResource(INPUT_FOLDERNAME).toURI());
		Path licensesInputOutputFolderPath = licensesInputFolderPath.getParent().resolve(OUTPUT_FOLDERNAME);
		LicenseWriter.moveLicencesToNamedFolders(reportFilePath, licensesInputFolderPath, licensesInputOutputFolderPath, log);
		assertTrue(Files.exists(licensesInputOutputFolderPath));
		assertTrue(Files.exists(licensesInputOutputFolderPath.resolve("a1.a2.2.7.6")));
		assertTrue(Files.exists(licensesInputOutputFolderPath.resolve("b1.b2.3.1")));
		assertTrue(Files.exists(licensesInputOutputFolderPath.resolve("d1.d2")));
		assertTrue(Files.exists(licensesInputOutputFolderPath.resolve("e1.e2.1.2.3")));
		assertTrue(Files.exists(licensesInputOutputFolderPath.resolve("e1.e2.1.2.3").resolve("license5.html")));
		assertTrue(Files.exists(licensesInputOutputFolderPath.resolve("e1.e2.1.2.3").resolve("license6.html")));
	}
}
