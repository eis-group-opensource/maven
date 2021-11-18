/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.core;
/*
 * Sheet names contained in the report
 */
public enum SheetNames {
	DETAILED("Detailed"), SUMMARY("Summary"),NEW_ITEMS("New items"),REMOVED_ITEMS("Removed items"),VERSION_CHANGES("Version changes"),LICENSE_CHANGES("License changes");
	
	private String sheetName;

	private SheetNames(String sheetName) {
		this.sheetName = sheetName;
	}
	
	public String getName() {
		return sheetName;
	}
}
