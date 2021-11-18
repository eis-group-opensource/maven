/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.core;

/*
 * Unified representation of dependency information.
 * Information on dependency is obtained from different sources,
 * one is DependencyNode, other is Artifact and other is MavenProject. 
 * This data object is used in order to simplify dependency information collection and access to it. 
 */
public class DependencyInfoData {
	private String rootProjectName;
	private String groupId;
	private String artifactId;
	private String version;
	private String scope;
	private String description;
	private String descriptionOverride;
	private String url;
	private String urlOverride;
	private String licenseName;
	private String licenseNameOverride;
	private String licenseUrl;
	private String licenseUrlOverride;
	private String internalLicenseFileName;
	private String internalLicenseFileNameOverride;
	private String licenseFileName;
	private String licenseFileNameOverride;
	private String dependencyPath;
	private Boolean modified = false;
	private Boolean modifiedOverride = false;

	public String getRootProjectName() {
		return rootProjectName;
	}
	public void setRootProjectName(String rootProjectName) {
		this.rootProjectName = rootProjectName;
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
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLicenseName() {
		return licenseName;
	}
	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}
	public String getLicenseUrl() {
		return licenseUrl;
	}
	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}
	public String getDependencyPath() {
		return dependencyPath;
	}
	public void setDependencyPath(String dependencyPath) {
		this.dependencyPath = dependencyPath;
	}
	public String getDescriptionOverride() {
		return descriptionOverride;
	}
	public void setDescriptionOverride(String descriptionOverride) {
		this.descriptionOverride = descriptionOverride;
	}
	public String getUrlOverride() {
		return urlOverride;
	}
	public void setUrlOverride(String urlOverride) {
		this.urlOverride = urlOverride;
	}
	public String getLicenseNameOverride() {
		return licenseNameOverride;
	}
	public void setLicenseNameOverride(String licenseNameOverride) {
		this.licenseNameOverride = licenseNameOverride;
	}
	public String getLicenseUrlOverride() {
		return licenseUrlOverride;
	}
	public void setLicenseUrlOverride(String licenseUrlOverride) {
		this.licenseUrlOverride = licenseUrlOverride;
	}
	public Boolean getModified() {return modified;}
	public void setModified(Boolean modified) {this.modified = modified;}

	public String getLicenseFileName() {
		return licenseFileName;
	}

	public void setLicenseFileName(String licenseFileName) {
		this.licenseFileName = licenseFileName;
	}

	public String getInternalLicenseFileName() {
		return internalLicenseFileName;
	}

	public void setInternalLicenseFileName(String internalLicenseFileName) {
		this.internalLicenseFileName = internalLicenseFileName;
	}

	public String getLicenseFileNameOverride() {
		return licenseFileNameOverride;
	}

	public String getInternalLicenseFileNameOverride() {
		return internalLicenseFileNameOverride;
	}

	public void setInternalLicenseFileNameOverride(String internalLicenseFileNameOverride) {
		this.internalLicenseFileNameOverride = internalLicenseFileNameOverride;
	}

	public Boolean getModifiedOverride() {
		return modifiedOverride;
	}

	public void setModifiedOverride(Boolean modifiedOverride) {
		this.modifiedOverride = modifiedOverride;
	}

	public void setLicenseFileNameOverride(String licenseFileNameOverride) {
		this.licenseFileNameOverride = licenseFileNameOverride;
	}

	@Override
	public String toString() {
		return "DependencyInfoData [rootProjectName=" + rootProjectName + ", groupId=" + groupId + ", artifactId="
				+ artifactId + ", version=" + version + ", scope=" + scope + ", description=" + description + ", url="
				+ url + ", licenseName=" + licenseName + ", licenseUrl=" + licenseUrl + ", licenseFileName=" + licenseFileName+ ", modified=" + modified +"]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result + ((dependencyPath == null) ? 0 : dependencyPath.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((descriptionOverride == null) ? 0 : descriptionOverride.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((licenseName == null) ? 0 : licenseName.hashCode());
		result = prime * result + ((licenseNameOverride == null) ? 0 : licenseNameOverride.hashCode());
		result = prime * result + ((licenseUrl == null) ? 0 : licenseUrl.hashCode());
		result = prime * result + ((licenseUrlOverride == null) ? 0 : licenseUrlOverride.hashCode());
		result = prime * result + ((licenseFileName == null) ? 0 : licenseFileName.hashCode());
		result = prime * result + ((licenseFileNameOverride == null) ? 0 : licenseFileNameOverride.hashCode());
		result = prime * result + ((rootProjectName == null) ? 0 : rootProjectName.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((urlOverride == null) ? 0 : urlOverride.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((modified == null) ? 0 : modified.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DependencyInfoData other = (DependencyInfoData) obj;
		if (artifactId == null) {
			if (other.artifactId != null)
				return false;
		} else if (!artifactId.equals(other.artifactId))
			return false;
		if (dependencyPath == null) {
			if (other.dependencyPath != null)
				return false;
		} else if (!dependencyPath.equals(other.dependencyPath))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (descriptionOverride == null) {
			if (other.descriptionOverride != null)
				return false;
		} else if (!descriptionOverride.equals(other.descriptionOverride))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (licenseName == null) {
			if (other.licenseName != null)
				return false;
		} else if (!licenseName.equals(other.licenseName))
			return false;
		if (licenseNameOverride == null) {
			if (other.licenseNameOverride != null)
				return false;
		} else if (!licenseNameOverride.equals(other.licenseNameOverride))
			return false;
		if (licenseUrl == null) {
			if (other.licenseUrl != null)
				return false;
		} else if (!licenseUrl.equals(other.licenseUrl))
			return false;
		if (licenseUrlOverride == null) {
			if (other.licenseUrlOverride != null)
				return false;
		} else if (!licenseUrlOverride.equals(other.licenseUrlOverride))
			return false;
		if (licenseFileName == null) {
			if (other.licenseFileName != null)
				return false;
		} else if (!licenseFileName.equals(other.licenseFileName))
			return false;
		if (licenseFileNameOverride == null) {
			if (other.licenseFileNameOverride != null)
				return false;
		} else if (!licenseFileNameOverride.equals(other.licenseFileNameOverride))
			return false;
		if (rootProjectName == null) {
			if (other.rootProjectName != null)
				return false;
		} else if (!rootProjectName.equals(other.rootProjectName))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (urlOverride == null) {
			if (other.urlOverride != null)
				return false;
		} else if (!urlOverride.equals(other.urlOverride))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		return true;
	}
}
