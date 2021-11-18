/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.xhtmlgen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Creates html documentation pages for xhtml files
 * 
 * @author dstulgis
 * @author astasauskas
 *
 */
public class PageCreationUtils {
	
	private static final String TITLE = "All Pages";
	private static final String PAGE_STYLE = "border-radius: 4px 4px 4px 4px; border: 1px solid rgba(0, 0, 0, 0.05); box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.05) inset;";
	private static final String LINKS_PAGE_STYLE = PAGE_STYLE + "background-color: #F5F5F5;";
	private static final String MAIN_STYLE = "color: #333333; font-family: \"Helvetica Neue\",Helvetica,Arial,sans-serif; font-size: 13px; line-height: 18px;";
	private static final String BODY_STYLE = "body {" + MAIN_STYLE + "}";
	private static final String H3_STYLE = "h3 {font-size: 18px; line-height: 27px;}";
	private static final String H4_STYLE = "h4 {font-size: 14px; background: #95DDFF;}";
	private static final String TABLE_STYLE = "table {" + MAIN_STYLE + "}";
	private static final String TH_STYLE = "th {background: #A6DCED;}";
	private static final String TD_STYLE = ".parameters td {padding: 0 4px;}";
	private static final String FILTER_STYLE = "#filter {height: 26px;}";
	private static final String SCOPE_STYLE = ".Internal {background: #E0E0E0;}\n"
			+ ".Overridable {background: #FFCE85;}"
			+ ".Invokable {background: #FFFF98;}\n"
			+ ".Extendable {background: #ADDEAD;}\n";
	private static final String BREAK = "<hr class=\"divider\">";
	private static final String BR = "<br />";
	private static final String EMPTY_INSERT_MESSAGE = "Empty insertion point (Includes content from tag where this page is used).";
	private static final String JQUERY_URL = "./js/apache-maven-fluido.min.js";
	private static final String USAGES_LABEL = "In subsystem this page is used in:";
	private static final String INNER_USAGES_LABEL = "In subsystem this source page is also used in:";
	
	private static Map<String, String> linksMap = new HashMap<String, String>();
	private static Map<String, Set<String>> pageUsagesMap = new HashMap<String, Set<String>>();
	
	public static String createLinksPage(Map<String, PageInfo> pages, Boolean isPublicDoc) {
		Document doc = Jsoup.parse("");
		Element bodyElement = doc.getElementsByTag("body").get(0);
		
		appendFilteringScript(bodyElement);
		appendStyle(bodyElement);
		
		Element headElement = doc.getElementsByTag("head").get(0);
		
		appendHeadStyle(headElement);
		
		bodyElement.appendElement("h3").attr("id", "header").text(TITLE);
		
		appendFilter(bodyElement);
		
		resolveScopeForInnerElements(pages.values(), pages);
		
		Element list = bodyElement.appendElement("ul").attr("id", "pagesList");
		for (Entry<String, PageInfo> pageEntry : pages.entrySet()) {
			if (isApplicable(isPublicDoc, pageEntry.getValue().isPublicInfoHolder())) {
				Element listElement = list.appendElement("li");
				String pageLink = pageEntry.getKey().replace(".xhtml", ".html");
				linksMap.put(pageEntry.getValue().getRelativePath(), pageLink);
				extractPageUsages(pageEntry.getValue(), null);
				listElement.appendElement("a").attr("href", XhtmlScanner.handleElExAndEmptySrc(pageLink, true)).attr("target", "pageFrame").text(pageEntry.getValue().getRelativePath());
			}
		}
		
		return doc.html();
	}
	
	public static String transformPathToKey(String path) {
		return path.replaceAll("\\\\", "_").replaceAll("/", "_");
	}
	
	public static String createFramesPage(String linkPageName) {
		Document doc = Jsoup.parse("");
		Element bodyElement = doc.getElementsByTag("body").get(0);
		
		Element table = bodyElement.appendElement("table").attr("class", "noClass");
		table.appendElement("col").attr("width", "25%").text("");
		table.appendElement("col").attr("width", "75%").text("");
		Element tr = table.appendElement("tr");
		tr.appendElement("td").appendElement("iframe").attr("src", linkPageName).attr("name", "pagesListFrame").attr("scrolling", "auto")
				.attr("style", LINKS_PAGE_STYLE).attr("height", "800px").text("");
		tr.appendElement("td").appendElement("iframe").attr("src", "").attr("name", "pageFrame").attr("scrolling", "auto")
				.attr("style", PAGE_STYLE).attr("height", "800px").attr("width", "100%").text("");
		
		return doc.html();
	}

	public static void createPages(String targetDir, Map<String, PageInfo> pages, Boolean isPublicDoc) throws MojoFailureException {
		for (Entry<String, PageInfo> pageEntry : pages.entrySet()) {
			if (isApplicable(isPublicDoc, pageEntry.getValue().isPublicInfoHolder())) {
				createPage(targetDir, pageEntry, isPublicDoc);
			}
		}
	}
	
	private static void resolveScopeForInnerElements(Collection<PageInfo> pagesToUpdate, Map<String, PageInfo> pages) {
		for (PageInfo pageInfo : pagesToUpdate) {
			for (PageInfo innerPageInfo : pageInfo.getIncludedInPage()) {
				String key;
				if (TagType.ui_decorate.equals(innerPageInfo.getTagType())) {
					key = transformPathToKey(innerPageInfo.getTemplate());
				} else {
					key = transformPathToKey(innerPageInfo.getRelativePath());
				}
				PageInfo pageInfoInList = pages.get(key);
				if (pageInfoInList != null) {
					pageInfo.setPublicInfoHolder(pageInfo.isPublicInfoHolder() || pageInfoInList.isPublicPage()); //  || pageInfoInList.isPublicInfoHolder()
					innerPageInfo.setPublicPage(pageInfoInList.isPublicPage());
				}
				//resolveScopeForInnerElements(innerPageInfo.getIncludedInPage(), pages);
			}
		}
		
	}
	
	private static void extractPageUsages(PageInfo pageInfo, String firstPageRelativePath) {
		String templateName = pageInfo.getTemplate();
		String includeName = pageInfo.getRelativePath();
		if (!StringUtils.isEmpty(templateName)) { // decorate or composition
			appendPageUsagesToMap(firstPageRelativePath == null ? includeName : firstPageRelativePath, resolveFullRelativePath(templateName, pageInfo.getParentRelativePath()));
		}
		if (TagType.ef_include.equals(pageInfo.getTagType())) { // decorate or composition
			//appendPageUsagesToMap(firstPageRelativePath == null ? includeName : firstPageRelativePath, resolveFullRelativePath(includeName, pageInfo.getParentRelativePath()));
			appendPageUsagesToMap(firstPageRelativePath == null ? pageInfo.getParentRelativePath() : firstPageRelativePath, resolveFullRelativePath(includeName, pageInfo.getParentRelativePath()));
		}
		if (!StringUtils.isEmpty(includeName) && firstPageRelativePath != null) { // include
			appendPageUsagesToMap(firstPageRelativePath, resolveFullRelativePath(includeName, pageInfo.getParentRelativePath()));
		}
		for (PageInfo innerPageInfo : pageInfo.getIncludedInPage()) {
			extractPageUsages(innerPageInfo, includeName);
		}
	}
	
	private static String resolveFullRelativePath(String pagePath, String parentRelativePath) {
		String fullRelativePath = pagePath;
		if (!fullRelativePath.startsWith("/")) {
			File parentFile = new File(parentRelativePath);
			String parentRelativeDir = parentFile.getParent();
			if (parentRelativeDir != null) {
				parentRelativeDir = parentRelativeDir.replace("\\", "/");
			}
			fullRelativePath = parentRelativeDir + "/" + pagePath;
		}
		return fullRelativePath;
	}
	
	private static void appendPageUsagesToMap(String relativePath, String pageNameUsed) {
		Set<String> pageUsages = pageUsagesMap.get(pageNameUsed);
		if (pageUsages == null) {
			pageUsages = new HashSet<String>();
			pageUsagesMap.put(pageNameUsed, pageUsages);
		}
		pageUsages.add(relativePath);
	}
	
	private static void createPage(String targetDir, Entry<String, PageInfo> pageEntry, Boolean isPublicDoc) throws MojoFailureException {
		Document doc = Jsoup.parse("");
		Element bodyElement = doc.getElementsByTag("body").get(0);
		
		appendStyle(bodyElement);
		
		Element headElement = doc.getElementsByTag("head").get(0);
		
		appendHeadStyle(headElement);
		
		PageInfo pageInfo = pageEntry.getValue();
		/* This is checked before adding to map.
		 * if (pageInfo.isEmpty()) {
			return;
		}*/
		appendScope(bodyElement, pageInfo);
		
		bodyElement.appendElement("h3").attr("id", "header").text(pageInfo.getRelativePath());
		
		appendPageInfo(bodyElement, pageInfo, false, isPublicDoc);
		
		File page = new File(targetDir + File.separatorChar + XhtmlScanner.handleElExAndEmptySrc(pageEntry.getKey(), true).replace(".xhtml", ".html"));
		try {
			page.createNewFile();
			FileUtils.fileWrite(page, doc.html());
			System.out.println("Creating page: " + page.getAbsolutePath());
		} catch (IOException e) {
			throw new MojoFailureException("Failed creating page file for " + page.getName(), e);
		}
	}

	private static void appendScope(Element bodyElement, PageInfo pageInfo) {
		String scopeText = "";
		switch (pageInfo.getScope()) {
			case extendable:
				scopeText = "Extendable";
				break;
			case invokable:
				scopeText = "Invokable";
				break;
			case overridable:
				scopeText = "Overridable";
				break;
			default:
				scopeText = "Internal";
				break;
		}
		bodyElement.appendElement("p").attr("class", scopeText).appendText(scopeText);
	}

	private static void appendPageInfo(Element bodyElement, PageInfo pageInfo, boolean inner, Boolean isPublicDoc) {
		boolean applicable = isApplicable(isPublicDoc, pageInfo.isPublicPage());
		
		appendMainInfo(bodyElement, pageInfo, inner, applicable);
		
		appendInserts(bodyElement, pageInfo.getInserts(), inner, applicable);
		
		appendIncludesAndTemplateUsages(bodyElement, pageInfo.getIncludedInPage(), inner, isPublicDoc);
		
		appendDefines(bodyElement, pageInfo.getDefines(), inner, applicable);
	}

	private static void appendDefines(Element bodyElement, List<String> defines, boolean inner, Boolean applicable) {
		if (!defines.isEmpty() && applicable) {
			bodyElement.appendElement("p").appendElement("h4").text("Defines:");
			Element list = bodyElement.appendElement("ul");
			for (String definition : defines) {
				list.appendElement("li").text(definition);
			}
			
			bodyElement.append(inner ? BR : BREAK);
		}
	}

	private static void appendIncludesAndTemplateUsages(Element bodyElement, List<PageInfo> pageInfoList, boolean inner, Boolean isPublicDoc) {
		if (!pageInfoList.isEmpty()) {
			List<PageInfo> decorates = new ArrayList<PageInfo>();
			List<PageInfo> includes = new ArrayList<PageInfo>();
			for (PageInfo pInfo : pageInfoList) {
				if (!isApplicable(isPublicDoc, pInfo.isPublicPage())) {
					continue;
				}
				if (TagType.ui_decorate.equals(pInfo.getTagType())) {
					decorates.add(pInfo);
				} else {
					includes.add(pInfo);
				}
			}
			
			if (!includes.isEmpty()) {
				bodyElement.appendElement("p").appendElement("h4").text("Includes files:");
				
				for (PageInfo pInfo : includes) {
					appendPageName(bodyElement, pInfo);
					appendPageInfo(bodyElement, pInfo, true, isPublicDoc);
				}
				
				bodyElement.append(inner ? BR : BREAK);
			}
			
			if (!decorates.isEmpty()) {
				bodyElement.appendElement("p").appendElement("h4").text("Decorates templates:");
				
				for (PageInfo pInfo : decorates) {
					appendPageName(bodyElement, pInfo);
					appendPageInfo(bodyElement, pInfo, true, isPublicDoc);
				}
				
				bodyElement.append(inner ? BR : BREAK);
			}
		}
	}

	private static void appendInserts(Element bodyElement, List<String> inserts, boolean inner, Boolean applicable) {
		if (!inserts.isEmpty() && applicable) {
			bodyElement.appendElement("p").appendElement("h4").text("Inserts:");
			Element list = bodyElement.appendElement("ul");
			for (String insert : inserts) {
				list.appendElement("li").text(insert.isEmpty() ? EMPTY_INSERT_MESSAGE : insert);
			}
			
			bodyElement.append(inner ? BR : BREAK);
		}
	}

	/**
	 * Append main info section to page / tag infos
	 * 
	 * @param bodyElement
	 * @param pageInfo
	 * @param inner
	 * @param applicable Is it for public api
	 */
	private static void appendMainInfo(Element bodyElement, PageInfo pageInfo, Boolean inner, Boolean applicable) {
		boolean containsInfo = false;
		
		if (pageInfo.isNonExistantPage()) {
			bodyElement.appendElement("p").append("<i>Note: this file does not exist in subsystem</i>");
			
			containsInfo = true;
		}
		
		if (!StringUtils.isEmpty(pageInfo.getTemplate())) {
			bodyElement.appendElement("p").append("<b>Template:</b> " + appendAsLink(pageInfo.getTemplate()));
			
			containsInfo = true;
		}
		String usagesPath = pageInfo.getRelativePath();
		if (inner && pageInfo.getTagType().equals(TagType.ui_decorate)) {
			usagesPath = pageInfo.getTemplate();
		}
		Set<String> templateUsages = pageUsagesMap.get(resolveFullRelativePath(usagesPath, pageInfo.getParentRelativePath()));
		if (templateUsages != null && !templateUsages.isEmpty()) {
			appendPageUsages(bodyElement, templateUsages, inner, pageInfo.getParentRelativePath());
			containsInfo = true;
		}
		if (!StringUtils.isEmpty(pageInfo.getDescription())) {
			bodyElement.appendElement("p").append("<b>Description:</b> " + pageInfo.getDescription().replace("\n", BR));
			containsInfo = true;
		}
		/*if (!StringUtils.isEmpty(pageInfo.getParentRelativePath()) && applicable) {
			bodyElement.appendElement("p").append("<b>Parent page:</b> " + appendAsLink(pageInfo.getParentRelativePath()));
			containsInfo = true;
		}*/
		if (!pageInfo.getParams().isEmpty()) {
			appendParametersTable(bodyElement, pageInfo, inner);
			containsInfo = true;
		}
		
		if (containsInfo || inner) {
			bodyElement.append(inner ? BR : BREAK);
		}
	}

	private static void appendPageUsages(Element bodyElement, Set<String> templateUsages, Boolean inner, String nameToSkip) {
		StringBuilder usagesLinksHtml = new StringBuilder();
		String delimeter = " ";
		for (String templateUsage: templateUsages) {
			if (!nameToSkip.isEmpty() && inner && templateUsage.contains(nameToSkip)) {
				continue;
			}
			usagesLinksHtml.append(delimeter + appendAsLink(templateUsage));
			delimeter = ", ";
		}
		if (usagesLinksHtml.toString().isEmpty()) {
			return;
		}
		Element templateUsagesParagraph = bodyElement.appendElement("p").append(inner ? INNER_USAGES_LABEL : USAGES_LABEL);
		templateUsagesParagraph.append(usagesLinksHtml.toString());
	}

	@Deprecated
	private static String appendAsLink(String relativePath) {
		String pageLink = linksMap.get(relativePath);
		if (pageLink == null) {
			return relativePath;
		}
		return "<a href=\"" + XhtmlScanner.handleElExAndEmptySrc(pageLink, true) + "\" target=\"pageFrame\">" + relativePath + "</a>";
	}
	
	private static String appendAsLink(PageInfo pageInfo) {
		String relativePath = pageInfo.getRelativePath();
		String pageLink = linksMap.get(relativePath);
		
		if (!pageInfo.getRelativePath().startsWith("/") && pageLink == null) {
			File file = new File(pageInfo.getParentRelativePath());
			String parentRelativeDir = file.getParent().replace("\\", "/");
			String relativePathWithParentDir = parentRelativeDir + "/" + relativePath;
			pageLink = linksMap.get(relativePathWithParentDir);
		}
		
		if (pageLink == null) {
			return relativePath;
		}
		return "<a href=\"" + XhtmlScanner.handleElExAndEmptySrc(pageLink, true) + "\" target=\"pageFrame\">" + relativePath + "</a>";
	}

	private static void appendPageName(Element bodyElement, PageInfo pageInfo) {
		if (!StringUtils.isEmpty(pageInfo.getRelativePath())) {
			bodyElement.appendElement("p").append("<b>Source page:</b> " + appendAsLink(pageInfo));
		}
	}

	private static void appendParametersTable(Element bodyElement, PageInfo pageInfo, Boolean inner) {
		bodyElement.appendElement("p").append("<b>Parameters:</b>");
		Element paramsTable = bodyElement.appendElement("table").attr("frame", "box").attr("rules", "all").attr("class", "parameters");
		Element headerRow = paramsTable.appendElement("tr");
		headerRow.appendElement("th").text("Name");
		if (inner) {
			headerRow.appendElement("th").text("Value");
		}
		headerRow.appendElement("th").attr("width", "100%").text("Description");
		for (Parameter param : pageInfo.getParams().values()) {
			Element row = paramsTable.appendElement("tr");
			row.appendElement("td").text(getNotNull(param.getName()));
			if (inner) {
				row.appendElement("td").text(getNotNull(param.getDefaultValue()));
			}
			row.appendElement("td").append(getNotNull(param.getDescription().replace("\n", BR)));
		}
	}
	
	private static void appendStyle(Element bodyElement) {
		bodyElement.appendElement("style").text(BODY_STYLE + "\n" + H3_STYLE + "\n" + H4_STYLE + "\n" + TABLE_STYLE + "\n" + TH_STYLE + "\n" + TD_STYLE + "\n" + FILTER_STYLE+ "\n" + SCOPE_STYLE);
	}
	
	private static void appendHeadStyle(Element headElement) {
		headElement.appendElement("link").attr("rel", "stylesheet").attr("href", "./css/apache-maven-fluido.min.css");
		headElement.appendElement("link").attr("rel", "stylesheet").attr("href", "./css/site.css");
		headElement.appendElement("link").attr("rel", "stylesheet").attr("href", "./css/print.css").attr("media", "print");
	}
	
	private static void appendFilter(Element bodyElement) {
		bodyElement.appendElement("input").attr("id", "filter").attr("type", "text").attr("placeholder", "Filter pages...");
	}
	
	private static void appendFilteringScript(Element bodyElement) {
		bodyElement.appendElement("script").attr("src", JQUERY_URL);
		
		String javaScript = "$(function(){ $('#filter').keyup(function() { var searchText = $(this).val().toLowerCase(); $('#pagesList').children().each("
				+ "function() { var currentLiText = $(this).text().toLowerCase(), showCurrentLi = currentLiText.indexOf(searchText) !== -1;"
				+ " $(this).toggle(showCurrentLi); }); }); });";
		bodyElement.appendElement("script").attr("type", "text/javascript").text(javaScript);
	}
	
	private static boolean isApplicable(Boolean isPublicDoc, Boolean pagePublicCheck) {
		return !isPublicDoc || (isPublicDoc && pagePublicCheck);
	}

	private static String getNotNull(String description) {
		return description == null ? "" : description;
	}
}
