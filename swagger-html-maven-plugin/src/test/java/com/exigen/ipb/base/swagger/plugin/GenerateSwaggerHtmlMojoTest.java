/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.ipb.base.swagger.plugin;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Coverage for {@link GenerateSwaggerHtmlMojo}
 * 
 * @author alralko
 * @since 2016SP11
 */
public class GenerateSwaggerHtmlMojoTest {

	private GenerateSwaggerHtmlMojo codeGenMojo;
	
	@Before
	public void setUp() throws Exception {
		codeGenMojo = new GenerateSwaggerHtmlMojo();
	}

	@Test
	public void mergeFiles() throws Exception {
		
		List<File> filesToMerge = Lists.newArrayList(
			loadFile("swagger.html.1"),
			loadFile("swagger.html.2"),
			loadFile("swagger.html.3")
		);
		
		Document document = codeGenMojo.mergeHtmlFiles(filesToMerge);
		
		assertThat(document, hasContentOf("swagger.html"));
	}
	
	/**
	 * Matcher compares content of provided file with expected one
	 * @param fileName - name of the file with expected content
	 * @return - matcher
	 */
	private Matcher<Document> hasContentOf(final String fileName) {
		return new BaseMatcher<Document>() {

			@Override
			public void describeTo(Description desc) {
				desc.appendText("element should have content same as ").appendValue(fileName);
			}

			@Override
			public boolean matches(Object obj) {
				try {
					final String actual = ((Document) obj).html().replaceAll("\\s+","");
					final String expected = loadDocumentFromFile(fileName).html().replaceAll("\\s+","");
										
					return actual.equals(expected);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		};
	}
	
	/**
	 * Load Jsoup document by file name
	 * @param name - file name
	 * @return - Jsop document
	 * @throws Exception
	 */
	private Document loadDocumentFromFile(String name) throws Exception {
		return Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream(name), "UTF-8"));
	}
	
	/**
	 * Load file by resouce name
	 * @param name - file name
	 * @return - File descriptor
	 * @throws Exception
	 */
	private File loadFile(String name) throws Exception {
		return new File(getClass().getResource(name).toURI());
	}
}
