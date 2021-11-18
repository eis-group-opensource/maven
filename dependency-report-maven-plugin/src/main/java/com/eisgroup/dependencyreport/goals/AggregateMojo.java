/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.goals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.report.spreadsheet.LicenseWriter;
import com.eisgroup.dependencyreport.report.spreadsheet.SpreadsheetReportAggregator;
import com.eisgroup.dependencyreport.utils.ReportUtils;

/*
 * Goal for multiple report aggregation into a single report.
 * This goal must be launched from aggregating project containing projects with reports as modules.
 */
@Mojo(name = "aggregate", defaultPhase = LifecyclePhase.SITE)
public class AggregateMojo extends AbstractMojo {

	@Component private MavenProject mavenProject;

	@Parameter(property = "skip-duplicates", defaultValue = "false") private boolean skipDuplicates;

	// Skip plugin execution
	@Parameter(defaultValue = "false") private boolean skip;

	@Parameter(property = "licenses-directory") private String licensesDirectory;

	@Parameter(property = "exclude-columns", defaultValue = "") private String excludeColumns;

	@Parameter(property = "root-component") private String rootComponent;

	/**
	 * Aggregate reports from project submodules (one level deep)
	 * @param reportAggregator
	 * @param aggregatorProjectPath
	 * @param aggregatedReportFilePath
	 * @param modules
	 * @throws IOException
	 * @throws ReportGenerationException
	 */
	private void processModules(SpreadsheetReportAggregator reportAggregator, Path aggregatorProjectPath, Path aggregatedReportFilePath, List<String> modules) throws IOException, ReportGenerationException {

		for (String module : modules) {

			Path modulePath = aggregatorProjectPath.resolve(module).normalize();
			getLog().info("Processing module: " + module);

			Path moduleReportFilePath = ReportUtils.calculateModuleReportFilePath(modulePath, getLog());
			if (!Files.exists(moduleReportFilePath)) {
				getLog().info("Report file was not found. Report path: " + moduleReportFilePath);
				continue;
			} else {
				getLog().info("Appending report: " + moduleReportFilePath);
			}
			// do all the processing
			getLog().info("Report file was not found. Report path: " + moduleReportFilePath);
			reportAggregator.appendReport(aggregatedReportFilePath, moduleReportFilePath);
		}
	}

	/**
	 * Aggregate reports from all project submodules (all levels in depth)
	 * @param reportAggregator
	 * @param aggregatedReportFilePath
	 * @param collectedProjects
	 * @throws IOException
	 * @throws ReportGenerationException
	 */
	private void processCollectedProjects(SpreadsheetReportAggregator reportAggregator, Path aggregatedReportFilePath, List<MavenProject> collectedProjects) throws IOException, ReportGenerationException {

		for (MavenProject collectedProject : collectedProjects) {

			Path modulePath = collectedProject.getBasedir().toPath().normalize();
			getLog().info("Processing module: " + collectedProject.getName());

			Path moduleReportFilePath = ReportUtils.calculateModuleReportFilePath(modulePath, getLog());
			if (!Files.exists(moduleReportFilePath)) {
				getLog().info("Report file was not found. Report path: " + moduleReportFilePath);
				continue;
			} else {
				getLog().info("Appending report: " + moduleReportFilePath);
			}
			// do all the processing
			reportAggregator.appendReport(aggregatedReportFilePath, moduleReportFilePath);

		}
	}

	public void execute() throws MojoExecutionException {
		if (skip) {
			getLog().info("Plugin execution is set to skip. Skipping plugin execution.");
			return;
		}

		SpreadsheetReportAggregator reportAggregator = new SpreadsheetReportAggregator(getLog(),
				this.skipDuplicates, ReportUtils.getExcludeColumnsFromInputAttribute(this.excludeColumns), this.rootComponent);

		Path aggregatorProjectPath = mavenProject.getFile().toPath().getParent();

		try {
			Path aggregatedReportFilePath = ReportUtils.resolveReportOutputFilePath(mavenProject, getLog());
			if (Files.exists(aggregatedReportFilePath)) {
				//Files.delete(aggregatedReportFilePath);
				getLog().info("Report file exist, start processing...");
			}

			List<String> modules = mavenProject.getModules();
			List<MavenProject> collectedProjects = mavenProject.getCollectedProjects();


			if (collectedProjects != null && collectedProjects.size() > 0) {
				getLog().info("Number of collected projects to process: " + ((collectedProjects == null) ? 0 : collectedProjects.size()));
				processCollectedProjects(reportAggregator, aggregatedReportFilePath, collectedProjects);
			} else {
				getLog().info("Number of project modules to process: " + ((modules == null) ? 0 : modules.size()));
				processModules(reportAggregator, aggregatorProjectPath, aggregatedReportFilePath, modules);
			}

			if (licensesDirectory != null) {
				try {
					Path licensesInputFolderPath = new File(licensesDirectory).toPath();
					Path projectOutputFolderPath = new File(mavenProject.getReporting().getOutputDirectory()).toPath();
					Path licensesInputOutputFolderPath = projectOutputFolderPath.resolve("licenses");
					LicenseWriter.moveLicencesToNamedFolders(aggregatedReportFilePath, licensesInputFolderPath, licensesInputOutputFolderPath, getLog());

				} catch (java.nio.file.InvalidPathException e) {
					throw new MojoExecutionException(e.getMessage(), e);
				}
			}

		} catch (ReportGenerationException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

}
