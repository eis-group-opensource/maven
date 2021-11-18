/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.xhtmlgen;

import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.StringUtils;

public class Parameter {

	private String name;
	
	private String defaultValue;
	
	private String description;
	
	public Parameter(String name, String defaultValue, String description) throws MojoFailureException {
		if (StringUtils.isEmpty(name)) {
			throw new MojoFailureException("");
		}
		this.name = name;
		this.defaultValue = defaultValue == null ? "" : defaultValue;
		this.description = description == null ? "" : description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder("Parameter [name=");
		string.append(name);
		string.append(", defaultValue=");
		string.append(defaultValue);
		string.append(", description=");
		string.append(description);
		string.append("]");
		return string.toString();
	}
}
