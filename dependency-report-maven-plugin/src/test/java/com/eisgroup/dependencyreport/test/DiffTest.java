package com.eisgroup.dependencyreport.test;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.eisgroup.dependencyreport.core.DependencyInfoData;
import com.eisgroup.dependencyreport.core.DiffData;
import com.eisgroup.dependencyreport.goals.DiffMojo;

public class DiffTest extends TestCase {

	private Log log = new LogMock();

	private static final String FILENAME_1 = "dependency-report-diff1.xlsx";
	private static final String FILENAME_2 = "dependency-report-diff2.xlsx";

	public void testDiffTwoReports() throws MojoExecutionException {

		File reportFile1 = new File(getClass().getClassLoader().getResource(FILENAME_1).getFile());
		File reportFile2 = new File(getClass().getClassLoader().getResource(FILENAME_2).getFile());

		final List<DependencyInfoData> artifactList1 = DiffMojo.readDependencyInfoData(reportFile1, log);
		final List<DependencyInfoData> artifactList2 = DiffMojo.readDependencyInfoData(reportFile2, log);

		DiffData diffData = DiffMojo.compareReports(artifactList1, artifactList2, log);

		assertEquals(3, diffData.getNewItems().size());
		assertEquals(3, diffData.getRemovedItems().size());
	}

}
