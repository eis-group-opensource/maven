/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.core;

/**
 * Used as a wrapper for any exception thrown during report generation
 *
 */
public class ReportGenerationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ReportGenerationException(Throwable exception) {
		super(exception);
	}
	
	public ReportGenerationException(String message) {
		super(message);
	}
	
	public ReportGenerationException(String message, Throwable cause) {
		super(message, cause);
	}
}
