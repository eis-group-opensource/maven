/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.test;

import org.apache.maven.project.MavenProject;

import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.utils.ReportUtils;
import com.eisgroup.tests.utils.TestUtils;

import junit.framework.TestCase;

public class SearchMavenProjectByIdTest extends TestCase {
	private static final String[] HIERARCHY_ID_PATH = new String[] {"group1:art1", "group2:art2", "group3:art3", "group4:art4"};
	private static final String LAST_DESCENDANT_ID = HIERARCHY_ID_PATH[0];
	private static final String LAST_ANCESTOR_ID = HIERARCHY_ID_PATH[HIERARCHY_ID_PATH.length - 1];
	private static final String NON_EXISTANT_ID = "non:existant";
	
	public void testFindLastAncestorSuccess() throws ReportGenerationException {
		MavenProject projectMock = TestUtils.mockMavenProjectParentHierarchy(HIERARCHY_ID_PATH);
		
		MavenProject projectFound = ReportUtils.findMavenAncestorOrSelfProjectById(projectMock, LAST_ANCESTOR_ID);
		assertNotNull(projectFound);
		assertEquals(projectFound.getGroupId(), "group4");
		assertEquals(projectFound.getArtifactId(), "art4");
	}
	
	public void testFindSelfSuccess() throws ReportGenerationException {
		MavenProject projectMock = TestUtils.mockMavenProjectParentHierarchy(HIERARCHY_ID_PATH);
	
		MavenProject projectFound = ReportUtils.findMavenAncestorOrSelfProjectById(projectMock, LAST_DESCENDANT_ID);
		assertNotNull(projectFound);
		assertEquals(projectFound.getGroupId(), "group1");
		assertEquals(projectFound.getArtifactId(), "art1");
	}
	
	public void testFindAncestorFail() throws ReportGenerationException {
		MavenProject projectMock = TestUtils.mockMavenProjectParentHierarchy(HIERARCHY_ID_PATH);
		boolean caught = false;
		try {
			ReportUtils.findMavenAncestorOrSelfProjectById(projectMock, NON_EXISTANT_ID);
		} catch (ReportGenerationException e) {
			caught = true;
		}
		
		assertTrue(caught);
	}
}
