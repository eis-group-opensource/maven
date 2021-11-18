/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.xhtmlgen;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * Files scanner class used for xhtml files documentation extraction.
 * 
 * @author dstulgis
 * @author astasauskas
 */
public class XhtmlScanner {
	
	private static final String PARAM_TAG = "ui:param";
	private static final String SEPARATOR = "src" + File.separatorChar + "main" + File.separatorChar + "resources";
	private static final String SEPARATOR2 = "src" + File.separatorChar + "main" + File.separatorChar + "webapp";
	private static XhtmlFileFilter filter = new XhtmlFileFilter();

	/**
	 * Scans all xhtml files recursively from given base directory and parses their documentation.
	 * 
	 * @param basedir initial directory for scanning.
	 * @param publicDoc is decision point if only public documents should be scanned.
	 * @param log for printing info/warning/error messages.
	 * @param skipDirs list of directories that should be excluded from scanning.
	 * @return map of PageInfo objects with xhtml files documentation matched by relative path,
	 * with separator characters replaced to underscore, as keys.
	 * @throws MojoFailureException when any IOException occurs.
	 */
	public static Map<String, PageInfo> scan(File basedir, Log log, List<String> skipDirs) throws MojoFailureException {
		Map<String, PageInfo> xhtmlMap = new HashMap<String, PageInfo>();
		
		for (File file : basedir.listFiles(filter)) {
			if (file.isDirectory() && !shouldBeExcluded(file.getAbsolutePath(), skipDirs)) {
				xhtmlMap.putAll(scan(file, log, skipDirs));
			} else {
				if (file.getName().endsWith((".xhtml")) && (file.getAbsolutePath().contains(SEPARATOR)
						|| file.getAbsolutePath().contains(SEPARATOR2))) {
					PageInfo pageInfo = extractInfo(file, log);
					/*if (pageInfo.isEmpty()) {
						continue;
					}*/
					String key = pageInfo.getRelativePath().replaceAll("[^a-zA-Z0-9.-]", "_");
					xhtmlMap.put(key, pageInfo);
				}
			}
		}
		
		return xhtmlMap;
	}
	
	private static boolean shouldBeExcluded(String dirPath, List<String> skipDirs) {
		for (String skipDir : skipDirs) {
			if (dirPath.contains(skipDir)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Scans xhtml file for documentation and page includes/inserts.
	 * @param xhtmlFile to be parsed.
	 * @return {@link PageInfo} object for given xhtml.
	 * @throws MojoFailureException when any IOException occures.
	 */
	private static PageInfo extractInfo(File xhtmlFile, Log log) throws MojoFailureException {
		PageInfo pageInfo = new PageInfo();
		boolean containsScopeAnnotation = false;
		
		try {
			pageInfo.setRelativePath(extractRelativePath(xhtmlFile.getCanonicalPath()));
		} catch (IOException e) {
			log.error("Failed parsing relative path.", e);
		}
		
		try {
			containsScopeAnnotation = parseXhtmlInfo(pageInfo, xhtmlFile, log);
		} catch (IOException e) {
			throw new MojoFailureException("Failed parsing xhtml info for file: " + xhtmlFile.getName(), e);
		}
		
		isPublicPath(pageInfo, pageInfo.getRelativePath(), containsScopeAnnotation);
		pageInfo.setPublicInfoHolder(containsPublicInfo(pageInfo));
		
		return pageInfo;
	}

	private static boolean parseXhtmlInfo(PageInfo pageInfo, File xhtmlFile, Log log) throws IOException, MojoFailureException {
		boolean containsScopeAnnotation = false;
		
		Document doc = Jsoup.parse(xhtmlFile, null);
		
		Elements compositionTags = doc.getElementsByTag("ui:composition");
		Elements htmlTags = doc.getElementsByTag("html");
		
		if (compositionTags.size() == 1) {
			containsScopeAnnotation = parseDescriptionAndParams(pageInfo, doc.childNode(0), false, log);
			pageInfo.setTemplate(compositionTags.get(0).attr("template"));
			pageInfo.setTagType(TagType.ui_composition);
		} else if (htmlTags.size() == 1) {
			containsScopeAnnotation = parseDescriptionAndParams(pageInfo, doc.childNode(0), false, log);
			pageInfo.setTagType(TagType.html);
		} else if (compositionTags.size() > 1) {
			throw new MojoFailureException("Page " + pageInfo.getRelativePath() + " is corrupt. It contains more than one <ui:composition/> tags.");
		}
		
		Elements uiInsertTags = doc.getElementsByTag("ui:insert");
		
		if (!uiInsertTags.isEmpty()) {
			parseUiInserts(pageInfo, uiInsertTags, log);
		}
		
		Elements uiIncludeTags = doc.getElementsByTag("ui:include");
		
		if (!uiIncludeTags.isEmpty()) {
			parseUiIncludes(pageInfo, uiIncludeTags, log);
		}
		
		Elements efIncludeTags = doc.getElementsByTag("ef:include");
		
		if (!efIncludeTags.isEmpty()) {
			parseEfIncludes(pageInfo, efIncludeTags, log);
		}
		
		Elements uiDecorateTags = doc.getElementsByTag("ui:decorate");
		
		if (!uiDecorateTags.isEmpty()) {
			parseUiDecorates(pageInfo, uiDecorateTags, log);
		}
		
		Elements uiDefineTags = doc.getElementsByTag("ui:define");
		
		if (!uiDefineTags.isEmpty()) {
			parseUiDefines(pageInfo, uiDefineTags, log);
		}
		
		return containsScopeAnnotation;
	}

	private static void parseUiInserts(PageInfo pageInfo, Elements uiInsertTags, Log log) {
		for (Element uiInsertTag : uiInsertTags) {
			pageInfo.getInserts().add(uiInsertTag.attr("name"));
		}
	}
	
	private static void parseUiIncludes(PageInfo pageInfo, Elements uiIncludeTags, Log log) throws MojoFailureException {
		for (Element uiIncludeTag : uiIncludeTags) {
			boolean containsScopeAnnotation = false;
			Node previousNode = uiIncludeTag.previousSibling();
			if (!(previousNode instanceof Comment)) {
				previousNode = previousNode.previousSibling();
			}
			PageInfo includeInfo = new PageInfo();
			includeInfo.setTagType(TagType.ui_include);
			includeInfo.setRelativePath(handleElExAndEmptySrc(uiIncludeTag.attr("src"), false));//uiIncludeTag.attr("src"));
			includeInfo.setParentRelativePath(pageInfo.getRelativePath());
			includeInfo.setTemplate(uiIncludeTag.attr("template"));
			if (previousNode != null) {
				containsScopeAnnotation = parseDescriptionAndParams(includeInfo, previousNode, true, log);
			}
			
			parseParameters(uiIncludeTag, includeInfo);
			
			isPublicPath(includeInfo, includeInfo.getRelativePath(), containsScopeAnnotation);
			includeInfo.setPublicInfoHolder(containsPublicInfo(includeInfo));
			
			pageInfo.getIncludedInPage().add(includeInfo);
		}
	}
	
	private static void parseEfIncludes(PageInfo pageInfo, Elements efIncludeTags, Log log) throws MojoFailureException {
		for (Element efIncludeTag : efIncludeTags) {
			boolean containsScopeAnnotation = false;
			Node previousNode = efIncludeTag.previousSibling();
			if (!(previousNode instanceof Comment)) {
				previousNode = previousNode.previousSibling();
			}
			PageInfo includeInfo = new PageInfo();
			includeInfo.setTagType(TagType.ef_include);
			includeInfo.setRelativePath(handleElExAndEmptySrc(efIncludeTag.attr("src"), false));	// Empty string when no source provided
			includeInfo.setParentRelativePath(pageInfo.getRelativePath());
			includeInfo.setTemplate(efIncludeTag.attr("template"));
			if (previousNode != null) {
				containsScopeAnnotation = parseDescriptionAndParams(includeInfo, previousNode, true, log);
			}
			
			parseParameters(efIncludeTag, includeInfo);
			
			isPublicPath(includeInfo, includeInfo.getRelativePath(), containsScopeAnnotation);
			includeInfo.setPublicInfoHolder(containsPublicInfo(includeInfo));
			
			pageInfo.getIncludedInPage().add(includeInfo);
		}
	}

	// ui:decorate page info doesn't have relative path.
	private static void parseUiDecorates(PageInfo pageInfo, Elements uiDecorateTags, Log log) throws MojoFailureException {
		for (Element uiDecorateTag : uiDecorateTags) {
			boolean containsScopeAnnotation = false;
			Node previousNode = uiDecorateTag.previousSibling();
			if (!(previousNode instanceof Comment)) {
				previousNode = previousNode.previousSibling();
			}
			PageInfo includeInfo = new PageInfo();
			includeInfo.setTagType(TagType.ui_decorate);
			includeInfo.setParentRelativePath(pageInfo.getRelativePath());
			includeInfo.setTemplate(handleElExAndEmptySrc(uiDecorateTag.attr("template"), false));//uiDecorateTag.attr("template"));
			if (previousNode != null) {
				containsScopeAnnotation = parseDescriptionAndParams(includeInfo, previousNode, true, log);
			}
			
			parseParameters(uiDecorateTag, includeInfo);
			
			isPublicPath(includeInfo, includeInfo.getTemplate(), containsScopeAnnotation);
			includeInfo.setPublicInfoHolder(containsPublicInfo(includeInfo));
			
			pageInfo.getIncludedInPage().add(includeInfo);
		}
	}
	
	private static void parseUiDefines(PageInfo pageInfo, Elements uiDefineTags, Log log) {
		for (Element uiDefineTag : uiDefineTags) {
			pageInfo.getDefines().add(uiDefineTag.attr("name"));
		}
	}

	private static boolean parseDescriptionAndParams(PageInfo pageInfo, Node node, Boolean innerPageInfo, Log log) throws MojoFailureException {
		boolean containsScopeAnnotation = false;
		if (node instanceof Comment) {
			String comment = ((Comment) node).getData().replaceAll("\r", "");
			StringBuilder description = new StringBuilder();
			Boolean isDescription = true;
			String paramDescription = "";
			String paramName = "";
			String descriptionSeparator = "";
			boolean firstLine = true;
			
			for (String part : comment.split("\n")) {
				String trimmedLine = part.trim();
				if (trimmedLine.isEmpty()) {
					continue;
				}
				if (firstLine && !innerPageInfo) {
					firstLine = false;
					Scope scope = findScopeAnnotation(trimmedLine);
					if (scope != null) {
						pageInfo.setScope(scope);
						pageInfo.setPublicPage(!scope.equals(Scope.internal));
						containsScopeAnnotation = true;
						continue;
					}
				}
				if (trimmedLine.contains("@param")) {
					if (!paramDescription.isEmpty()) {
						pageInfo.getParams().put(paramName, new Parameter(paramName, null, paramDescription));
					}
					String nameAndDescr = trimmedLine.substring(trimmedLine.indexOf(' ') + 1);
					paramName = nameAndDescr.substring(0, nameAndDescr.indexOf(' '));
					paramDescription = nameAndDescr.substring(nameAndDescr.indexOf(' ') + 1);
					isDescription = false;
				} else if (isDescription) {
					description.append(descriptionSeparator + trimmedLine);
					descriptionSeparator = "\n";
				} else {
					paramDescription += "\n" + trimmedLine;
				}
			}
			pageInfo.setDescription(description.toString());
			if (!paramDescription.isEmpty()) {
				pageInfo.getParams().put(paramName, new Parameter(paramName, null, paramDescription));
			}
		}
		return containsScopeAnnotation;
	}
	
	private static Scope findScopeAnnotation(String trimmedLine) {
		if ("@extendable".equalsIgnoreCase(trimmedLine)) {
			return Scope.extendable;
		}
		if ("@invokable".equalsIgnoreCase(trimmedLine)) {
			return Scope.invokable;
		}
		if ("@overridable".equalsIgnoreCase(trimmedLine)) {
			return Scope.overridable;
		}
		if ("@internal".equalsIgnoreCase(trimmedLine)) {
			return Scope.internal;
		}
		return null;
	}

	private static void parseParameters(Element uiIncludeTag, PageInfo includeInfo) throws MojoFailureException {
		Elements params = uiIncludeTag.getElementsByTag(PARAM_TAG);
		for (Element param : params) {
			String name = param.attr("name");
			String defaultValue = param.attr("value");
			Parameter p = includeInfo.getParams().get(name);
			if (p != null) {
				p.setDefaultValue(defaultValue);
			} else {
				p = new Parameter(name, defaultValue, null);
				includeInfo.getParams().put(name, p);
			}
		}
	}
	
	public static String handleElExAndEmptySrc(String value, boolean sanitize) {
		String resultValue = value;
		if (sanitize) {
			resultValue = resultValue.replaceAll("[^/a-zA-Z0-9.-]", "_");
		}
		if (resultValue.isEmpty()) {
			resultValue = "No source";
		}
		return resultValue;
	}
	
	private static String extractRelativePath(String canonicalPath) {
		if (canonicalPath.contains(SEPARATOR)) {
			return canonicalPath.substring(canonicalPath.lastIndexOf(SEPARATOR) + SEPARATOR.length()).replace("\\", "/");
		} else {
			return canonicalPath.substring(canonicalPath.lastIndexOf(SEPARATOR2) + SEPARATOR2.length()).replace("\\", "/");
		}
	}
	
	private static boolean isPublicPath(PageInfo includeInfo, String path, boolean containsScopeAnnotation) {
		boolean publicPage = false;
		Scope scope = Scope.internal;
		if (path.contains("overridable")) {
			publicPage = true;
			scope = Scope.overridable;
		} else if (path.contains("invokable")) {
			publicPage = true;
			scope = Scope.invokable;
		} else if (path.contains("extendable")) {
			publicPage = true;
			scope = Scope.extendable;
		} else {
			scope = Scope.internal;
		}
		
		if (!containsScopeAnnotation) {
			includeInfo.setScope(scope);
			includeInfo.setPublicPage(publicPage);
		}
		
		return  publicPage;
	}
	
	private static boolean containsPublicInfo(PageInfo pageInfo) {
		if (pageInfo.isPublicPage() || isPublicPath(pageInfo, pageInfo.getTemplate(), true)) {
			return true;
		} else {
			for (PageInfo innerPageInfo : pageInfo.getIncludedInPage()) {
				if (innerPageInfo.isPublicPage()) {
					return true;
				}
			}
		}
		return false;
	}

	private static class XhtmlFileFilter implements FilenameFilter {
		public boolean accept(File directory, String name) {
			if (".hg".equals(name) || "target".equals(name)) {
				return false;
			}
			return true;
		}
	}
}
