/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.eisgroup.dependencyreport.report.spreadsheet;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

/*
 * Cache for Workbook's components. 
 * Excel spreadsheets have limited style and font capacity. Creating logically same,
 * but internally new style or font for each cell could result in errors if those limits are exceeded.
 * Reuse as much as possible already created components.
 * 
 */
public class WorkbookReusableComponentCache {
	private Map<CellStyles, CellStyle> cellStyleCache = new HashMap<>();
	private Map<Fonts, Font> fontCache = new HashMap<>();
	private Map<String, Hyperlink> hyperlinkCache = new HashMap<>();
	
	private Workbook workbook;

	public WorkbookReusableComponentCache(Workbook workbook) {
		this.workbook = workbook;
	}

	public CellStyle getCellStyle(CellStyles style) {
		CellStyle cellStyle = cellStyleCache.get(style);
		if (cellStyle == null) {
			switch (style) {
			case CELL_STYLE_HYPERLINK:
				cellStyle = createCellHyperlinkStyle();
				break;
			case CELL_STYLE_SIMPLE_WRAPPED:
				cellStyle = createSimpleWrappedCellStyle();
				break;
			case CELL_STYLE_BOLD_WRAPPED:
				cellStyle = createBoldWrappedCellStyle();
				break;
			default:
				break;
			}
			
			cellStyleCache.put(style, cellStyle);
			
		}

		return cellStyle;
	}

	public Font getFont(Fonts fontDesc) {
		Font font = fontCache.get(fontDesc);
		if (font == null) {
			switch (fontDesc) {
			case FONT_HYPERLINK:
				font = createHyperlinkFont();
				break;
			case FONT_BOLD:
				font = createBoldFont();
				break;
			}

			fontCache.put(fontDesc, font);
		}
		
		
		return font;
	}

	private Font createBoldFont() {
		Font boldFont = workbook.createFont();
		
		boldFont.setBold(true);
		
		return boldFont;
	}

	public Hyperlink getHyperlink(String hyperlinkUrl) {
		Hyperlink hyperlink = hyperlinkCache.get(hyperlinkUrl);
		if (hyperlink == null) {
			CreationHelper helper = workbook.getCreationHelper();
			hyperlink = helper.createHyperlink(HyperlinkType.URL);
			hyperlink.setAddress(hyperlinkUrl);

			hyperlinkCache.put(hyperlinkUrl, hyperlink);
		}
		
		
		return hyperlink;
	}
	
	private Font createHyperlinkFont() {
		Font hyperlinkFont = workbook.createFont();
		
		hyperlinkFont.setUnderline(Font.U_SINGLE);
		hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
		
		return hyperlinkFont;
	}

	private CellStyle createCellHyperlinkStyle() {
		CellStyle hyperlinkCellStyle = workbook.createCellStyle();

		hyperlinkCellStyle.setFont(getFont(Fonts.FONT_HYPERLINK));
		hyperlinkCellStyle.setWrapText(true);
		
		return hyperlinkCellStyle;
	}
	
	private CellStyle createSimpleWrappedCellStyle() {
		CellStyle simpleWrappedCellStyle = workbook.createCellStyle();
		
		simpleWrappedCellStyle.setWrapText(true);
		
		return simpleWrappedCellStyle;
	}
	
	private CellStyle createBoldWrappedCellStyle() {
		CellStyle simpleWrappedCellStyle = workbook.createCellStyle();
		
		simpleWrappedCellStyle.setWrapText(true);
		simpleWrappedCellStyle.setFont(getFont(Fonts.FONT_BOLD));
		
		return simpleWrappedCellStyle;
	}
	
	public static enum CellStyles {
		CELL_STYLE_HYPERLINK, CELL_STYLE_SIMPLE_WRAPPED, CELL_STYLE_BOLD_WRAPPED
	}

	static enum Fonts {
		FONT_HYPERLINK, FONT_BOLD
	}
}