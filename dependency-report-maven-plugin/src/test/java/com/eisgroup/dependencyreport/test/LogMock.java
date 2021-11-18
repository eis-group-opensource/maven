/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.test;

import java.util.logging.Logger;

import org.apache.maven.plugin.logging.Log;

public class LogMock implements Log {
	private Logger log = Logger.getLogger("LOGMOCK");
	
	@Override
	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void debug(CharSequence content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(CharSequence content, Throwable error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(Throwable error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInfoEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void info(CharSequence content) {
		log.info(content.toString());
	}

	@Override
	public void info(CharSequence content, Throwable error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(Throwable error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWarnEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void warn(CharSequence content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(CharSequence content, Throwable error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(Throwable error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isErrorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void error(CharSequence content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(CharSequence content, Throwable error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Throwable error) {
		// TODO Auto-generated method stub
		
	}
	
}