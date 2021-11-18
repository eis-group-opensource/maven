package com.eisgroup.dependencyreport.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DiffData {
	private List<DependencyInfoData> newItems = new ArrayList<>();
	private List<DependencyInfoData> newVersionItems = new ArrayList<>();
	private Map<DependencyInfoData, String> newVersionItemsMap = new LinkedHashMap<>();
	private Map<DependencyInfoData, String> licenseNameChanges = new LinkedHashMap <>();
	private Map<DependencyInfoData, String> licenseFileNameChanges = new LinkedHashMap <>();
	private List<DependencyInfoData> removedItems = new ArrayList<>();

	public List<DependencyInfoData> getNewItems() {
		return newItems;
	}

	public void setNewItems(List<DependencyInfoData> newItems) {
		this.newItems = newItems;
	}

	public List<DependencyInfoData> getNewVersionItems() {
		return newVersionItems;
	}

	public void setNewVersionItems(List<DependencyInfoData> newVersionItems) {
		this.newVersionItems = newVersionItems;
	}

	public Map<DependencyInfoData, String> getNewVersionItemsMap() {
		return newVersionItemsMap;
	}

	public void setNewVersionItemsMap(Map<DependencyInfoData, String> newVersionItemsMap) {
		this.newVersionItemsMap = newVersionItemsMap;
	}

	public Map<DependencyInfoData, String> getLicenseNameChanges() {
		return licenseNameChanges;
	}

	public void setLicenseNameChanges(Map<DependencyInfoData, String> licenseNameChanges) {
		this.licenseNameChanges = licenseNameChanges;
	}

	public Map<DependencyInfoData, String> getLicenseFileNameChanges() {
		return licenseFileNameChanges;
	}

	public void setLicenseFileNameChanges(Map<DependencyInfoData, String> licenseFileNameChanges) {
		this.licenseFileNameChanges = licenseFileNameChanges;
	}

	public List<DependencyInfoData> getRemovedItems() {
		return removedItems;
	}

	public void setRemovedItems(List<DependencyInfoData> removedItems) {
		this.removedItems = removedItems;
	}
}
