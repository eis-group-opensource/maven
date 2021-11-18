package com.eisgroup.dependencyreport.core;

public class XMLLicense {

	private String name;
	private String url;
	private String filename;

	public XMLLicense(String name, String url, String filename) {
		this.name = name;
		this.url = url;
		this.filename = filename;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}


