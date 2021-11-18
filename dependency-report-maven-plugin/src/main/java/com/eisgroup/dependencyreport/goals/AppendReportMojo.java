package com.eisgroup.dependencyreport.goals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.report.spreadsheet.SpreadsheetReportAggregator;

/*
 * Goal for appending previously created report to another repost file.
 * In case destination report file is not exists - a new report file will be created.
 * This goal could be launched without Maven project (no pom.xml needed).
 * Example: "mvn com.eisgroup:dependency-report-maven-plugin:1.11-SNAPSHOT:append -Dsource-report=J:\central\input-dependency-report.xlsx  -Ddestination-report=J:\central\destination-dependency-report.xlsx
 */
@Mojo(name = "append", defaultPhase = LifecyclePhase.SITE, aggregator = true, threadSafe = true, requiresProject = false)
public class AppendReportMojo extends AbstractMojo {

	@Parameter(property = "source-report") private String sourceReportFilePath;
	@Parameter(property = "destination-report") private String destinationReportFilePath;
	@Parameter(defaultValue = "false") private boolean skip;
	@Parameter(property = "skip-duplicates", defaultValue = "true") private boolean skipDuplicates;


	private void validateInputParameters() throws MojoExecutionException {
		if (StringUtils.isEmpty(sourceReportFilePath)) {
			throw new MojoExecutionException("'source-report' attribute is mandatory.");
		}
		if (StringUtils.isEmpty(destinationReportFilePath)) {
			throw new MojoExecutionException("'destination-report' attribute is mandatory.");
		}
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (skip) {
			getLog().info("Plugin execution is set to skip. Skipping plugin execution.");
			return;
		}

		validateInputParameters();

		Path sourceReportPath = new File(sourceReportFilePath).toPath();
		Path destinationReportPath = new File(destinationReportFilePath).toPath();
		if (!Files.isReadable(sourceReportPath)) {
			getLog().error("Unable to read " + sourceReportFilePath + ". Skipping plugin execution.");
			return;
		}

		SpreadsheetReportAggregator reportAggregator = new SpreadsheetReportAggregator(getLog(), skipDuplicates);

		getLog().info("Appending " + sourceReportFilePath + " to " + destinationReportFilePath);
		try {
			reportAggregator.appendReport(destinationReportPath, sourceReportPath);
		} catch (ReportGenerationException e) {
			getLog().error("Unable to add " + sourceReportFilePath + " to " + destinationReportFilePath);
			getLog().error(e.getMessage());
		}

	}
}
