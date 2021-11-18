/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.ipb.base.swagger.codegen.languages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenType;
import io.swagger.codegen.languages.AbstractJavaJAXRSServerCodegen;

/**
 *	Custom implementation of Swagger static html generator
 *	
 *	Overrides method {@link #postProcessOperations(Map)} to adjust generated examples 
 *	and allows to re-define name of generated html file and template via environment variables 
 *
 *	@author alralko
 *	@since 2016SP11
 */
public class EISSwaggerHtmlGenerator extends AbstractJavaJAXRSServerCodegen {

	private static final String QUOTED_STRING_REGEX = ":([\\s\\[]*)\"[a-zA-Z]+\"";	
	private static final String QUOTED_STRING_REPLACEMENT = ":$1\"string\"";
	
	private static final String ARRAY_QUOTED_STRING_REGEX = "\\[(\\s*)\"[a-zA-Z]+\"";	
	private static final String ARRAY_QUOTED_STRING_REPLACEMENT = "[$1\"string\"";
	
	private static final String QUOTED_DATE_REGEX = ":([\\s\\[]*)\"\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}\"";
	private static final String QUOTED_DATE_REPLACEMENT = ":$1\"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(Calendar.getInstance().getTime()) + "\"";
	
	private static final String QUOTED_DECIMAL_REGEX = ":([\\s\\[]*)\\d+\\.\\d+";	
	private static final String QUOTED_DECIMAL_REPLACEMENT = ":$110000.25";
	
	public EISSwaggerHtmlGenerator() {
		super();
		
		// clear up AbstractJavaCodegen hard-coded template files
		modelTemplateFiles.clear();
		apiTemplateFiles.clear();
		apiTestTemplateFiles.clear();
		modelDocTemplateFiles.clear();
		apiDocTemplateFiles.clear();
		
		// false by default, could be overridden by USE_BEANVALIDATION CLI parameters.
		useBeanValidation = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CodegenType getTag() {
		return CodegenType.DOCUMENTATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "swaggerHtml";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHelp() {
		return "Generates Swagger static html file";
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
		Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");
        for (CodegenOperation op : operationList) {
        	if(op.examples != null && op.examples.size() > 0) {
        		for(Map<String, String> ex : op.examples) {
        			String example = ex.get("example");
        			if(!Strings.isNullOrEmpty(example)) {
		    			ex.put("example", example
		    					.replaceAll(QUOTED_STRING_REGEX, QUOTED_STRING_REPLACEMENT)
		    					.replaceAll(ARRAY_QUOTED_STRING_REGEX, ARRAY_QUOTED_STRING_REPLACEMENT)
		    					.replaceAll(QUOTED_DATE_REGEX, QUOTED_DATE_REPLACEMENT)
		    					.replaceAll(QUOTED_DECIMAL_REGEX, QUOTED_DECIMAL_REPLACEMENT)
		    			);
        			}
        		}
        	}
        }
        
        return super.postProcessOperations(objs);
    }

}
