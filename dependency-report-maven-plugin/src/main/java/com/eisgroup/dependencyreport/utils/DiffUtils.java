package com.eisgroup.dependencyreport.utils;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.eisgroup.dependencyreport.core.DependencyInfoData;

public class DiffUtils {

	public static boolean equalsGroupArtifact(DependencyInfoData first, DependencyInfoData second) {
		if (first.getGroupId() == null) {
			if (second.getGroupId() != null) { return false; }
		} else if (!first.getGroupId().equals(second.getGroupId())) { return false; }

		if (first.getArtifactId() == null) {
			if (second.getArtifactId() != null) { return false; }
		} else if (!first.getArtifactId().equals(second.getArtifactId())) { return false; }
		return true;
	}

	public static Optional<String> findPreviousVersion(DependencyInfoData dependencyInfoData, List<DependencyInfoData> dependencyInfoDataList) {
		Optional<DependencyInfoData> first = dependencyInfoDataList.stream().filter(i -> equalsGroupArtifactComponent(dependencyInfoData, i)).findFirst();
		if (first.isPresent()) {
			return Optional.of(first.get().getVersion());
		}
		return Optional.empty();
	}

	public static Map.Entry<String, String> findPreviousLicense(DependencyInfoData dependencyInfoData, List<DependencyInfoData> dependencyInfoDataList) {
		Optional<DependencyInfoData> first = dependencyInfoDataList.stream().filter(i -> equalsGroupArtifactComponent(dependencyInfoData, i)).findFirst();
		if (!first.isPresent()) {
			return null;
		}
		String licName = StringUtils.EMPTY;
		String licFileName = StringUtils.EMPTY;
		if (!StringUtils.isEmpty(first.get().getLicenseName().trim())) {
			licName = first.get().getLicenseName().trim();
		}
		if (!StringUtils.isEmpty(first.get().getLicenseFileName().trim())) {
			licFileName = first.get().getLicenseFileName().trim();
		}
		return new AbstractMap.SimpleEntry<>(licName, licFileName);//Collections.singletonMap(licName,licFileName);
	}

	public static boolean equalsGroupArtifactComponent(DependencyInfoData first, DependencyInfoData second) {
		if (!equalsGroupArtifact(first, second)) {
			return false;
		}
		if (first.getRootProjectName() == null) {
			if (second.getRootProjectName() != null) { return false; }
		} else if (!first.getRootProjectName().equals(second.getRootProjectName())) { return false; }
		return true;
	}

	public static boolean equalsGroupArtifactComponentLicense(DependencyInfoData first, DependencyInfoData second) {
		if (!equalsGroupArtifactComponent(first, second)) {
			return false;
		}
		if (first.getLicenseName() == null) {
			if (second.getLicenseName() != null) { return false; }
		} else if (!first.getLicenseName().equals(second.getLicenseName())) { return false; }
		return true;
	}

	public static boolean equalsGroupArtifactComponentVersion(DependencyInfoData first, DependencyInfoData second) {
		if (!equalsGroupArtifactComponent(first, second)) {
			return false;
		}
		if (first.getVersion() == null) {
			if (second.getVersion() != null) { return false; }
		} else if (!first.getVersion().equals(second.getVersion())) { return false; }
		return true;
	}

}
