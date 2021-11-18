package com.eisgroup.dependencyreport.core;

import java.util.List;

public class LicenseInfoData {
	private String groupId;
	private String artifactId;
	private String version;
	private List<XMLLicense> licenses;

	public LicenseInfoData(String groupId, String artifactId, String version) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	public List<XMLLicense> getLicenses() {
		return licenses;
	}

	public void setLicenses(List<XMLLicense> licens) {
		this.licenses = licens;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
