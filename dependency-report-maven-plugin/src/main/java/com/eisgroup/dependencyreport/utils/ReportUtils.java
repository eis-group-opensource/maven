/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;

import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.ReportGenerationException;

public class ReportUtils {
	private static final String DEPENDENCY_REPORT_FILE_NAME = "dependency-report";
	private static final String DEPENDENCY_REPORT_FILE_NAME_EXTENSION = ".xlsx";
	private static final String DEFAULT_REPORT_OUTPUT_FOLDER_NAME = "dependency-report";
	private static final String DEFAULT_REPORT_INPUT_FOLDER_PATH = "src/site/resources/dependency-report";
	
	private static final String DEFAULT_REPORT_OUTPUT_FOLDER_PATH = "site/" + DEFAULT_REPORT_OUTPUT_FOLDER_NAME;
	private static final String DEFAULT_REPORT_OUTPUT_FOLDER_PATH_WITH_TARGET = "target/site/" + DEFAULT_REPORT_OUTPUT_FOLDER_NAME;
	
	/**
	 * Does the maven magic and loads the MavenProject object which contains information from artifact's pom file.
	 *  
	 * @param artifact artifact object which represents the project being searched.
	 * @param projectBuilder
	 * @param mavenSession
	 * @return project found, or null otherwise
	 */
	public static MavenProject getMavenProjectFromArtifact(Artifact artifact, ProjectBuilder projectBuilder, MavenSession mavenSession, Log log) {
	    ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest(mavenSession.getProjectBuildingRequest());
	    try {
	        buildingRequest.setProject(null);
	        // By default the strict validation is selected, which throws a lot of exceptions and results
	        // in loss of information because of failed validations. 
	        // Only metadata of the project is needed, no need for advanced validation.
	        buildingRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
	        MavenProject mavenProject = projectBuilder.build(artifact, buildingRequest).getProject();
	             
	        return mavenProject;
	    } catch (ProjectBuildingException e) {
			log.error(artifact.getId());
	    	log.error(e.getMessage());
	    }
	    return null;
	}
	
	/**
	 * Calculates report file path and if needed creates all the necessary directories.
	 * 
	 * @param mavenProject the project, where report will be generated
	 * @param log maven's logger
	 * @return path to the project report file
	 * @throws IOException if something happened while creating directory structure
	 */
	public static Path resolveReportOutputFilePath(MavenProject mavenProject, Log log) throws IOException {
		// find destination directory and pre-create directories if needed
		Path reportDestinationDirectoryPath = resolveReportDestinationDirectoryPath(mavenProject, log);
		
		// calculate file name
		String fileNameWithExtension = DEPENDENCY_REPORT_FILE_NAME + DEPENDENCY_REPORT_FILE_NAME_EXTENSION;
		
		// add file name to directory path
		return reportDestinationDirectoryPath.resolve(fileNameWithExtension);
	}
	
	/**
	 * Calculates path to report contained in the module project.
	 *  
	 * @param moduleProjectPath 
	 * @param log
	 * @return
	 * @throws IOException
	 */
	public static Path calculateModuleReportFilePath(Path moduleProjectPath, Log log) throws IOException {
		// calculate file name
		String fileNameWithExtension = DEPENDENCY_REPORT_FILE_NAME + DEPENDENCY_REPORT_FILE_NAME_EXTENSION;
		
		Path resultingPath = moduleProjectPath.resolve(DEFAULT_REPORT_OUTPUT_FOLDER_PATH_WITH_TARGET).resolve(fileNameWithExtension);

		return resultingPath;
	}
	
	/**
	 * Calculates path to report input file.
	 * 
	 * @param mavenProject the project containing report input file
	 * @param log maven logger
	 * @return path to the report input file
	 */
	public static Path resolveReportInputFilePath(MavenProject mavenProject, Log log) {
		// find input directory and pre-create directories if needed
		Path reportDestinationDirectoryPath = resolveReportInputDirectoryPath(mavenProject, log);
		
		if (reportDestinationDirectoryPath == null) {
			return null;
		}
		
		// calculate file name
		String fileNameWithExtension = DEPENDENCY_REPORT_FILE_NAME + DEPENDENCY_REPORT_FILE_NAME_EXTENSION;
		
		// add file name to directory path
		return reportDestinationDirectoryPath.resolve(fileNameWithExtension);
	}
	
	/**
	 * Finds ancestor project or self using project id.
	 * Example:
	 * Hierarchical path g0:a0 -> g1:a1 -> g2:a2 -> g3:a3 -> g4:a4 . g4:a4 is the last ascendant and it has no parent. 
	 * g1:a1 is the project represented by the mavenProject parameter.
	 * If project id is g3:a3, then the project which's groupId='g3' and artifactId='a3' will be returned.
	 * Descendants of mavenProject are not searched, so g0:a0 is not analyzed.
	 *  
	 * @param mavenProject project from which search will be executed. 
	 * @param reportDestinationProjectId groupId:artifactId project identifier
	 * @return project found
	 * @throws ReportGenerationException if no ancestor defined by property reportDestinationProjectId is found or property is in incorrect format
	 */
	public static MavenProject findMavenAncestorOrSelfProjectById(MavenProject mavenProject, String reportDestinationProjectId) throws ReportGenerationException {
		MavenProject destinationProject = null;
		String[] reportDestinationProjectIdSplitted = reportDestinationProjectId.split(":");
		if (reportDestinationProjectId == null || reportDestinationProjectIdSplitted.length != 2) {
			throw new ReportGenerationException("Check 'reportDestinationProjectId' property. Property value must be formatted as follows <groupId>:<artifactId>, got '" + reportDestinationProjectId + "'");
		}
		
		destinationProject = mavenProject;
		while (destinationProject != null) {
			if (destinationProject.getGroupId().equals(reportDestinationProjectIdSplitted[0]) 
					&& destinationProject.getArtifactId().equals(reportDestinationProjectIdSplitted[1])) {
				// this is THE project
				break;
			}
			destinationProject = destinationProject.getParent();
		}
		
		if (destinationProject == null) {
			throw new ReportGenerationException("Check 'reportDestinationProjectId' property. Unable to locate ancestor project defined by this property. Property value: " + reportDestinationProjectId);
		}
		
		return destinationProject;
	}
	
	private static Path resolveReportInputDirectoryPath(MavenProject destinationProject, Log log) {
		if (destinationProject.getBasedir() == null) {
			return null;
		}
		
		Path parentsBuildDirectory = Paths.get(destinationProject.getBasedir().getAbsolutePath());
		
		Path reportInputPath = parentsBuildDirectory.resolve(DEFAULT_REPORT_INPUT_FOLDER_PATH);
		
		return reportInputPath;
	}
	
	private static Path resolveReportDestinationDirectoryPath(MavenProject destinationProject, Log log) throws IOException {
		Path parentsBuildDirectory = Paths.get(destinationProject.getBuild().getDirectory());
		Path reportDestinationPath = parentsBuildDirectory.resolve(DEFAULT_REPORT_OUTPUT_FOLDER_PATH);
		
		if (Files.notExists(reportDestinationPath)) {
			try {
				Files.createDirectories(reportDestinationPath);
			} catch (IOException e) {
				log.error(e);
				throw e;
			}
		}
		
		return reportDestinationPath;
	}


	public static Set<DetailedSheetColumns> getExcludeColumnsFromInputAttribute(String excludeColumns) {
		Set<DetailedSheetColumns> columns = new HashSet<>();
		if (excludeColumns==null) {
			return columns;
		}
		for (String pattern : Arrays.asList(excludeColumns.split(","))) {
			if (DetailedSheetColumns.fromColumnName(pattern.trim()).isPresent())
			columns.add(DetailedSheetColumns.fromColumnName(pattern.trim()).get());
		}
		return columns;
	}
}
