/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.xhtmlgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Page information holder
 * 
 * @author astasauskas
 * @author dstulgis
 */
public class PageInfo {
	
	private Scope scope = Scope.internal;
	
	private String description = "";
	
	private Map<String, Parameter> params = new HashMap<String, Parameter>();
	
	// generated documentation title should be the same
	private String relativePath = "";
	
	private String parentRelativePath = "";
	
	private String template = "";
	
	private TagType tagType;
	
	// ui:insert tags in page
	private List<String> inserts = new ArrayList<String>();
	
	// ui:define tags in page
	private List<String> defines = new ArrayList<String>();
	
	// pages, where this page is included
	private List<PageInfo> includedInPage = new ArrayList<PageInfo>();
	
	private boolean publicPage = false;
	
	private boolean publicInfoHolder = false;

	private boolean nonExistantPage = false;

	public Scope getScope() {
		return scope;
	}
	
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Parameter> getParams() {
		return params;
	}

	public void setParams(Map<String, Parameter> params) {
		this.params = params;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getParentRelativePath() {
		return parentRelativePath;
	}

	public void setParentRelativePath(String parentRelativePath) {
		this.parentRelativePath = parentRelativePath;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public TagType getTagType() {
		return tagType;
	}

	public void setTagType(TagType tagType) {
		this.tagType = tagType;
	}

	public List<String> getInserts() {
		return inserts;
	}

	public void setInserts(List<String> inserts) {
		this.inserts = inserts;
	}

	public List<String> getDefines() {
		return defines;
	}

	public void setDefines(List<String> defines) {
		this.defines = defines;
	}

	public List<PageInfo> getIncludedInPage() {
		return includedInPage;
	}

	public void setIncludedInPage(List<PageInfo> includedInPage) {
		this.includedInPage = includedInPage;
	}
	
	public boolean isPublicPage() {
		return publicPage;
	}
	
	public void setPublicPage(boolean publicPage) {
		this.publicPage = publicPage;
	}
	
	public boolean isPublicInfoHolder() {
		return publicInfoHolder;
	}

	public void setPublicInfoHolder(boolean publicInfoHolder) {
		this.publicInfoHolder = publicInfoHolder;
	}

	@Override
	public String toString() {
		return "PageInfo [description=\"" + description + "\", params=" + params.keySet()
				+ ", relativePath=" + relativePath + ", template=" + template + ", parentRelativePath="
				+ parentRelativePath + ", tagType=" + tagType + ", inserts=" + inserts + ", defines="
				+ defines + ", includedInPage=" + includedInPage + "]";
	}

	public boolean isEmpty() {
		return relativePath.isEmpty() || (description.isEmpty() && params.isEmpty() && template.isEmpty()
				&& parentRelativePath.isEmpty() && inserts.isEmpty() && defines.isEmpty() && includedInPage.isEmpty());
	}

	public void setNonExistantPage(boolean nonExistantPage) {
		this.nonExistantPage = nonExistantPage;
	}

	public boolean isNonExistantPage() {
		return nonExistantPage;
	}
	
	

}
