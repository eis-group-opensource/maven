/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.ipb.base.swagger.codegen.languages;

import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.swagger.codegen.CodegenOperation;

/**
 * Coverage for {@link EISSwaggerHtmlGenerator}
 * 
 * @author alralko
 * @since 2016SP11
 */
public class EISSwaggerHtmlGeneratorTest {

    
    @InjectMocks
	private EISSwaggerHtmlGenerator eisSwaggerHtmlGenerator;
	
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void nullExample() throws Exception {
		
		Map<String, Object> model = createModelWithNullExample();
		eisSwaggerHtmlGenerator.postProcessOperations(model);
		
		assertThat(model, hasExamples("empty-expected.json"));
	}
	
	@Test
	public void emptyExample() throws Exception {
		
		Map<String, Object> model = createModelWithExamples("empty.json");
		eisSwaggerHtmlGenerator.postProcessOperations(model);
		
		assertThat(model, hasExamples("empty-expected.json"));
	}
	
	@Test
	public void emptyArrayExample() throws Exception {
		
		Map<String, Object> model = createModelWithExamples("empty-array.json");
		eisSwaggerHtmlGenerator.postProcessOperations(model);
		
		assertThat(model, hasExamples("empty-array-expected.json"));
	}
	
	@Test
	public void singleArrayValueExample() throws Exception {
		
		Map<String, Object> model = createModelWithExamples("single-array-value.json");
		eisSwaggerHtmlGenerator.postProcessOperations(model);
		
		assertThat(model, hasExamples("single-array-value-expected.json"));
	}
	
	@Test
	public void fullExample() throws Exception {
		
		Map<String, Object> model = createModelWithExamples("full.json");
		eisSwaggerHtmlGenerator.postProcessOperations(model);
		
		assertThat(model, hasExamples("full-expected.json"));
	}
	
	@Test
	public void multipleExamples() throws Exception {
		
		Map<String, Object> model = createModelWithExamples(
			"full.json", "empty.json", "empty-array.json", "single-array-value.json"
		);
		eisSwaggerHtmlGenerator.postProcessOperations(model);
		
		assertThat(model, hasExamples(
			"full-expected.json", "empty-expected.json", "empty-array-expected.json", "single-array-value-expected.json"
		));
	}
	
	/**
	 * Create model with the list of provided examples (loaded from file)
	 * 
	 * @param exampleFileNames - example files names
	 * @return mode;
	 * @throws Exception
	 */
	private Map<String, Object> createModelWithExamples(String ... exampleFileNames) throws Exception {
		Map<String, Object> model = Maps.newHashMap();
		Map<String, Object> operations = Maps.newHashMap();
		List<CodegenOperation> operation = Lists.newArrayList();
		
		operation.add(createCodegenOperation(exampleFileNames));
				
		operations.put("operation", operation);
		model.put("operations", operations);
		
		return model;
	}
	
	/**
	 * Create model with the null example
	 * 
	 * @return mode;
	 * @throws Exception
	 */
	private Map<String, Object> createModelWithNullExample() throws Exception {
		Map<String, Object> model = Maps.newHashMap();
		Map<String, Object> operations = Maps.newHashMap();
		List<CodegenOperation> operation = Lists.newArrayList();
		
		CodegenOperation op = new CodegenOperation();
		op.baseName = "generator";
		op.path = "/swagger-rs/v1";
		op.examples = Lists.newArrayList();
		op.imports = Collections.emptySet();
		op.vendorExtensions = Maps.newHashMap();

		Map<String, String> example = Maps.newHashMap();
		example.put("example", null);
		
		op.examples.add(example);
		
		operation.add(op);
				
		operations.put("operation", operation);
		model.put("operations", operations);
		
		return model;
	}
	
	/**
	 * Matcher to check if updated model has expected exmaple (loaded from file)
	 * @param expectedExamples - expected example files names
	 * @return - matcher
	 */
	private Matcher<Map<String, Object>> hasExamples(final String... expectedExamples) {
		return new BaseMatcher<Map<String, Object>>() {

			@Override
			public void describeTo(Description desc) {
				desc.appendText("model should have examples ").appendValue(expectedExamples);
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean matches(Object item) {
				Map<String, Object> model = (Map<String, Object>) item;
				
				Map<String, Object> operations = (Map<String, Object>) model.get("operations");
		        List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");
		        for (CodegenOperation op : operationList) {
		        	if(op.examples != null && op.examples.size() > 0) {
		        		
		        		Iterator<Map<String, String>> it1 = op.examples.iterator();
		        		Iterator<String> it2 = Lists.newArrayList(expectedExamples).iterator();

		        		while (it1.hasNext() && it2.hasNext()) {
		        			try {
		        				String actual = Strings.nullToEmpty(it1.next().get("example"));
			        			String expected = loadExampleFromFile(it2.next());
			        			expected = expected.replace("__DATE__", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(Calendar.getInstance().getTime()));
			        			
			        			if(!actual.equals(expected)) {
			        				return false;
			        			}
			        			
							} catch (Exception e) {
								return false;
							}
		        		}
		        	}
		        }
		        
				return true;
			}
		};
	}
	
	/**
	 * Create {@link CodegenOperation} object based on example json content 
	 * @param exampleFileNames - exampleFileNames - example files names
	 * @return - {@link CodegenOperation}
	 * @throws Exception 
	 */
	private CodegenOperation createCodegenOperation(String ... exampleFileNames) throws Exception {
		CodegenOperation op = new CodegenOperation();
		op.baseName = "generator";
		op.path = "/swagger-rs/v1";
		op.examples = Lists.newArrayList();
		op.vendorExtensions = Maps.newHashMap();
		
		for(String exampleFileName : exampleFileNames) {
			op.examples.add(createExample(loadExampleFromFile(exampleFileName)));
		}
		
		return op;
	}
	
	/**
	 * Create example map from provided json string
	 * @param exampleJson - example json string
	 * @return - example map
	 */
	private Map<String, String> createExample(String exampleJson) {
		Map<String, String> example = Maps.newHashMap();
		example.put("example", exampleJson);
		
		return example;
	}
	
	/**
	 * Load file from resources based on file name
	 * @param name - file name
	 * @return - content of file
	 * @throws Exception
	 */
	private String loadExampleFromFile(String name) throws Exception {
		return IOUtils.toString(getClass().getResourceAsStream(name), "UTF-8");
	}
	
}
