/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.xhtmlgen;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * This is maven plugin implementation used for documentation generation from xhtml files.
 * 
 * @param publicDoc set to {code}true{code} to generate documentation only for public pages, otherwise false.
 * @param skipDirs is list of directories to be excluded from scanning.
 * @author dstulgis
 * @author astasauskas
 */
@Mojo(inheritByDefault=false,
	name="aggregate",
	aggregator=false,
	defaultPhase=LifecyclePhase.GENERATE_RESOURCES)
public class XhtmlDocumentationMojo extends AbstractMojo {

	private static final String MENU_ITEM_NAME = "XhtmlDocs";
	private static final String LINK_PAGE_NAME = "links.html";
	private static final String MENU_PAGE_NAME = "xhtmlDocs.xhtml";
	private static final String MENU_PAGE_REF = "xhtmlDocs.html";
	
	@Parameter(property="publicDoc", defaultValue="true", required=true)
	private Boolean publicDoc;
	
	@Parameter(property="basedir", defaultValue="${basedir}")
    private String _basedir;
	
	@Parameter(property="skipDirs", required=false)
	private List<String> skipDirs;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting directory:" + _basedir);
		File siteXml;
		siteXml = new File(_basedir + "/src/site/site.xml");
		
		if (!siteXml.exists()) {
			return;
		}

		getLog().info("Generating documentation for " + (publicDoc ? "public" : "internal") + " xhtml files.");
		
		Map<String, PageInfo> scannedFiles = XhtmlScanner.scan(new File(_basedir), getLog(), skipDirs);
		
		String targetDir = _basedir + "/target/site-resources/xhtml";
		String targetDirForPages = _basedir + "/target/site";
		
		if (!publicDoc || (publicDoc && containsPublicPages(scannedFiles))) {
			scannedFiles = new TreeMap<String, PageInfo>(scannedFiles);
			updateIndexPage(siteXml);
			generateMenuPage(targetDir);
			generateLinksPage(scannedFiles, targetDirForPages, publicDoc);
			generateDocumentationPages(scannedFiles, targetDirForPages, publicDoc);
		}
	}

	private boolean containsPublicPages(Map<String, PageInfo> scannedFiles) {
		for (PageInfo pageInfo : scannedFiles.values()) {
			if (pageInfo.isPublicInfoHolder()) {
				return true;
			}
		}
		return false;
	}

	private void updateIndexPage(File siteXml) throws MojoFailureException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document doc = builder.parse(siteXml);
			
			NodeList menuTags = doc.getElementsByTagName("menu");
			Node lastMenuTag = menuTags.item(menuTags.getLength() - 1);
			
			//check if menu item already exists
			Node lastChild = lastMenuTag.getLastChild();
			if (lastChild instanceof Text) {
				lastChild = lastChild.getPreviousSibling();
			}
			if (lastChild != null && lastChild instanceof Element && MENU_ITEM_NAME.equals(((Element)lastChild).getAttribute("name"))) {
				return;
			}
			
			Element item = doc.createElement("item");
			item.setAttribute("name", MENU_ITEM_NAME);
			item.setAttribute("href", MENU_PAGE_REF);
			lastMenuTag.appendChild(item);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(siteXml);
			transformer.transform(source, result);
		} catch (IOException e) {
			throw new MojoFailureException("Failed updating /src/site/site.xml", e);
		} catch (SAXException e) {
			throw new MojoFailureException("Failed updating /src/site/site.xml", e);
		} catch (ParserConfigurationException e) {
			throw new MojoFailureException("Failed updating /src/site/site.xml", e);
		} catch (TransformerConfigurationException e) {
			throw new MojoFailureException("Failed updating /src/site/site.xml", e);
		} catch (TransformerException e) {
			throw new MojoFailureException("Failed updating /src/site/site.xml", e);
		}
	}

	private void generateMenuPage(String targetDir) throws MojoFailureException {
		File menuPageFile = new File(targetDir + File.separatorChar + MENU_PAGE_NAME);
		menuPageFile.getParentFile().mkdirs();
		
		try {
			menuPageFile.createNewFile();
		} catch (IOException e) {
			throw new MojoFailureException("Failed creating xhtml documentation site pages.", e);
		}
		
		String menuPageContents = PageCreationUtils.createFramesPage(LINK_PAGE_NAME);
		
		try {
			FileUtils.fileWrite(menuPageFile, menuPageContents);
		} catch (IOException e) {
			throw new MojoFailureException("Failed creating menu page.", e);
		}
	}
	
	private void generateLinksPage(Map<String, PageInfo> scannedFiles, String targetDir, Boolean isPublicDoc) throws MojoFailureException {
		File linksPageFile = new File(targetDir + File.separatorChar + LINK_PAGE_NAME);
		linksPageFile.getParentFile().mkdirs();
		
		try {
			linksPageFile.createNewFile();
		} catch (IOException e) {
			throw new MojoFailureException("Failed creating xhtml documentation site pages.", e);
		}
		Map<String, PageInfo> nonExistingPages = new HashMap<String, PageInfo>();
		for (PageInfo pageInfo : scannedFiles.values()) {
			for (PageInfo innerPageInfo : pageInfo.getIncludedInPage()) {
				String key;
				if (TagType.ui_decorate.equals(innerPageInfo.getTagType())) {
					key = PageCreationUtils.transformPathToKey(innerPageInfo.getTemplate());
				} else {
					key = PageCreationUtils.transformPathToKey(innerPageInfo.getRelativePath());
				}
				key = XhtmlScanner.handleElExAndEmptySrc(key, false);
				PageInfo existingPage = scannedFiles.get(key);
				if (existingPage == null) {
					innerPageInfo.setNonExistantPage(true);
					nonExistingPages.put(key, innerPageInfo);
				}
			}
		}
		scannedFiles.putAll(nonExistingPages);
		
		String linksPageContents = PageCreationUtils.createLinksPage(scannedFiles, isPublicDoc);
		
		try {
			FileUtils.fileWrite(linksPageFile, linksPageContents);
		} catch (IOException e) {
			throw new MojoFailureException("Failed creating links page.", e);
		}
	}
	
	private void generateDocumentationPages(Map<String, PageInfo> scannedFiles, String targetDir, Boolean isPublicDoc) throws MojoFailureException {
		PageCreationUtils.createPages(targetDir, scannedFiles, isPublicDoc);
	}

	public void setBasedir(String basedir) {
		this._basedir = basedir;
	}
}
