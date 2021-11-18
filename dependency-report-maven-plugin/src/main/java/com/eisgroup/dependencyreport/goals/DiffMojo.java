package com.eisgroup.dependencyreport.goals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import com.eisgroup.dependencyreport.core.DependencyInfoData;
import com.eisgroup.dependencyreport.core.DetailedSheetColumns;
import com.eisgroup.dependencyreport.core.DiffData;
import com.eisgroup.dependencyreport.core.DiffLicenseSheetColumns;
import com.eisgroup.dependencyreport.core.DiffSheetColumns;
import com.eisgroup.dependencyreport.core.DiffVersionSheetColumns;
import com.eisgroup.dependencyreport.core.ReportGenerationException;
import com.eisgroup.dependencyreport.core.SheetNames;
import com.eisgroup.dependencyreport.report.spreadsheet.ReportSpreadsheetUtils;
import com.eisgroup.dependencyreport.report.spreadsheet.WorkbookReusableComponentCache;
import com.eisgroup.dependencyreport.utils.DiffUtils;
import com.eisgroup.dependencyreport.utils.SpreadsheetUtils;

@Mojo(name = "diff", defaultPhase = LifecyclePhase.SITE, aggregator = true, threadSafe = true, requiresProject = false)
public class DiffMojo extends AbstractMojo {

	@Parameter(property = "compare-from", required = true) private String reportFileName1;
	@Parameter(property = "compare-to", required = true) private String reportFileName2;

	@Parameter(property = "destination-report") private String destinationReportFileName;
	@Parameter(defaultValue = "false") private boolean skip;
	@Parameter(property = "report-format", defaultValue = "ms-excel") private String reportFormat;



	private static final String[] VALID_FORMATS = new String[] { "ms-excel" }; //  "html" not implemented

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (skip) {
			getLog().info("Plugin merge goal execution is set to skip. Skipping plugin execution.");
			return;
		}

		File reportFile1 = new File(reportFileName1);
		File reportFile2 = new File(reportFileName2);

		validateRequest(reportFile1, reportFile2);

		final List<DependencyInfoData> artifactList1 = readDependencyInfoData(reportFile1, getLog());
		getLog().info(reportFileName1 + " contains: " + artifactList1.size() + " artifacts");
		final List<DependencyInfoData> artifactList2 = readDependencyInfoData(reportFile2, getLog());
		getLog().info(reportFileName2 + " contains: " + artifactList2.size() + " artifacts");

		DiffData diffData = compareReports(artifactList1, artifactList2, getLog());

		logDiffData(diffData, getLog());

		try {
			String fileName = exportDiffReportToFile(generateDestinationReportName(), diffData, getLog());
			getLog().debug("Report generated to: " + fileName);
		} catch (IOException | ReportGenerationException e) {
			getLog().error("Report generation failed.");
			throw new MojoExecutionException(e.getMessage(), e);
		}

		getLog().info("Diff report output file name " + generateDestinationReportName());
	}

	/**
	 * Helper method. Not used during regular execution
	 *
	 * @param fileNamePath
	 * @throws Exception
	 */
	private void printCurrentColumnWidth(String fileNamePath) throws Exception {
		File file = new File(fileNamePath);
		Workbook workbook = new XSSFWorkbook(file);
		Sheet workbookSheet = workbook.getSheet(SheetNames.NEW_ITEMS.getName());
		for (DiffSheetColumns value : DiffSheetColumns.values()) {
			int columnWidth = workbookSheet.getColumnWidth(value.getColumnIndex());
			getLog().info(value.getColumnName() + " width=" + columnWidth);
		}
	}

	/**
	 * Compare two lists of DependencyInfoData and creates new DiffData with compare statistic
	 *
	 * @param entry1
	 * @param entry2
	 * @param log
	 * @return
	 */
	public static DiffData compareReports(final List<DependencyInfoData> entry1, final List<DependencyInfoData> entry2, Log log) {

		List<DependencyInfoData> equalGroupArtifactComponent = new ArrayList<>();
		List<DependencyInfoData> equalGroupArtifactComponentR = new ArrayList<>();
		List<DependencyInfoData> equalGroupArtifactComponentVersion = new ArrayList<>();
		List<DependencyInfoData> equalGroupArtifactComponentLicense = new ArrayList<>();

		for (DependencyInfoData dependencyInfoData1 : entry1) {
			for (DependencyInfoData dependencyInfoData2 : entry2) {
				if (DiffUtils.equalsGroupArtifactComponent(dependencyInfoData1, dependencyInfoData2)) {
					equalGroupArtifactComponent.add(dependencyInfoData2);
					equalGroupArtifactComponentR.add(dependencyInfoData1);
				}
				if (DiffUtils.equalsGroupArtifactComponentVersion(dependencyInfoData1, dependencyInfoData2)) {
					equalGroupArtifactComponentVersion.add(dependencyInfoData2);
				}
				if (DiffUtils.equalsGroupArtifactComponentLicense(dependencyInfoData1, dependencyInfoData2)) {
					equalGroupArtifactComponentLicense.add(dependencyInfoData2);
				}
			}
		}

		DiffData diffData = new DiffData();

		List<DependencyInfoData> newItems = entry2.stream().filter(i2 -> !equalGroupArtifactComponent.contains(i2)).collect(Collectors.toList());
		diffData.setNewItems(newItems);

		List<DependencyInfoData> newVersionItems = entry2.stream().filter(i2 -> !equalGroupArtifactComponentVersion.contains(i2)).collect(Collectors.toList());
		newVersionItems.removeAll(newItems);
		diffData.setNewVersionItems(newVersionItems);
		diffData.getNewVersionItems().sort(Comparator.comparing(DependencyInfoData::getRootProjectName).thenComparing(DependencyInfoData::getGroupId).thenComparing(DependencyInfoData::getArtifactId));

		List<DependencyInfoData> removedItems = entry1.stream().filter(i1 -> !equalGroupArtifactComponentR.contains(i1)).collect(Collectors.toList());
		diffData.setRemovedItems(removedItems);
		diffData.getRemovedItems().sort(Comparator.comparing(DependencyInfoData::getRootProjectName).thenComparing(DependencyInfoData::getGroupId).thenComparing(DependencyInfoData::getArtifactId));

		// removing new items from newVersionItems
		List<DependencyInfoData> newLicenseItems = entry2.stream().filter(i2 -> !equalGroupArtifactComponentLicense.contains(i2)).collect(Collectors.toList());
		newLicenseItems.removeAll(newItems);

		newLicenseItems.sort(Comparator.comparing(DependencyInfoData::getRootProjectName).thenComparing(DependencyInfoData::getGroupId).thenComparing(DependencyInfoData::getArtifactId));
		for (DependencyInfoData dependencyInfoData : newVersionItems) {
			Optional<String> previousVersion = DiffUtils.findPreviousVersion(dependencyInfoData, entry1);
			String previousVersionData = previousVersion.orElse(StringUtils.EMPTY);
			diffData.getNewVersionItemsMap().put(dependencyInfoData, previousVersionData);
		}

		newVersionItems.sort(Comparator.comparing(DependencyInfoData::getRootProjectName).thenComparing(DependencyInfoData::getGroupId).thenComparing(DependencyInfoData::getArtifactId));
		for (DependencyInfoData dependencyInfoData : newLicenseItems) {
			Map.Entry<String, String> previousLicense = DiffUtils.findPreviousLicense(dependencyInfoData, entry1);
			if (previousLicense != null) {
				diffData.getLicenseNameChanges().put(dependencyInfoData, previousLicense.getKey());
				diffData.getLicenseFileNameChanges().put(dependencyInfoData, previousLicense.getValue());
			}
		}
		return diffData;
	}

	/**
	 * Prints DiffData to output with INFO log level
	 *
	 * @param diffData
	 * @param log
	 */
	private static void logDiffData(DiffData diffData, Log log) {
		log.info(SheetNames.NEW_ITEMS.getName() + ": " + diffData.getNewItems().size());
		for (DependencyInfoData dependencyInfoData : diffData.getNewItems()) {
			log.info("\t" + dependencyInfoData.getRootProjectName() + "->" + dependencyInfoData.getGroupId() + ":" + dependencyInfoData.getArtifactId() + ":" + dependencyInfoData.getVersion());
		}
		log.info(SheetNames.REMOVED_ITEMS.getName() + ": " + diffData.getRemovedItems().size());
		for (DependencyInfoData dependencyInfoData : diffData.getRemovedItems()) {
			log.info("\t" + dependencyInfoData.getRootProjectName() + "->" + dependencyInfoData.getGroupId() + ":" + dependencyInfoData.getArtifactId() + ":" + dependencyInfoData.getVersion());
		}
		log.info(SheetNames.VERSION_CHANGES.getName() + ": " + diffData.getNewVersionItems().size());
		for (Map.Entry<DependencyInfoData, String> dependencyInfoDataStringEntry : diffData.getNewVersionItemsMap().entrySet()) {
			String previousVersionData = dependencyInfoDataStringEntry.getValue();
			log.info("\t" + dependencyInfoDataStringEntry.getKey().getRootProjectName() + "->" + dependencyInfoDataStringEntry.getKey().getGroupId() + ":" + dependencyInfoDataStringEntry.getKey().getArtifactId() + ":"
					+ previousVersionData + "->" + dependencyInfoDataStringEntry.getKey().getVersion());
		}
		log.info(SheetNames.LICENSE_CHANGES.getName() + ": " + diffData.getLicenseNameChanges().size());
		for (Map.Entry<DependencyInfoData, String> dependencyInfoDataStringEntry : diffData.getLicenseNameChanges().entrySet()) {

			String licName = "No License Name";
			String licFileName = "No License Filename";

			if (!StringUtils.isEmpty(dependencyInfoDataStringEntry.getKey().getLicenseName().trim())) {
				licName = dependencyInfoDataStringEntry.getKey().getLicenseName().trim();
			}
			if (!StringUtils.isEmpty(dependencyInfoDataStringEntry.getKey().getLicenseFileName().trim())) {
				licFileName = dependencyInfoDataStringEntry.getKey().getLicenseFileName().trim();
			}

			String previousLicenseData = dependencyInfoDataStringEntry.getValue() + ":" + diffData.getLicenseFileNameChanges().get(dependencyInfoDataStringEntry);
			String currentLicenseData = licName + ":" + licFileName;
			log.info("\t" + dependencyInfoDataStringEntry.getKey().getRootProjectName() + "->" + dependencyInfoDataStringEntry.getKey().getGroupId() + ":" + dependencyInfoDataStringEntry.getKey().getArtifactId() + ":"
					+ previousLicenseData + "->" + currentLicenseData);
		}
	}

	/**
	 * Creates new file and writes DiffData statistics there
	 *
	 * @param fileName
	 * @param diffData
	 * @param log
	 * @return
	 * @throws IOException
	 * @throws ReportGenerationException
	 */
	private static String exportDiffReportToFile(String fileName, DiffData diffData, Log log) throws IOException, ReportGenerationException {

		Workbook workbook = new XSSFWorkbook();
		WorkbookReusableComponentCache workbookComponentCache = new WorkbookReusableComponentCache(workbook);

		Sheet workbookSheetNewItems = workbook.createSheet(SheetNames.NEW_ITEMS.getName());
		addDiffSheetHeaderColumns(workbookSheetNewItems, workbookComponentCache);

		for (int i = 0; i < diffData.getNewItems().size(); i++) {
			Row row = workbookSheetNewItems.createRow(i+1);
			SpreadsheetUtils.updateDiffRowWithArtifactData(diffData.getNewItems().get(i), row);
		}


		Sheet workbookSheetRemovedItems = workbook.createSheet(SheetNames.REMOVED_ITEMS.getName());
		addDiffSheetHeaderColumns(workbookSheetRemovedItems, workbookComponentCache);

		for (int i = 0; i < diffData.getRemovedItems().size(); i++) {
			Row row = workbookSheetRemovedItems.createRow(i+1);
			SpreadsheetUtils.updateDiffRowWithArtifactData(diffData.getRemovedItems().get(i), row);
		}


		Sheet workbookSheetVersionChanges = workbook.createSheet(SheetNames.VERSION_CHANGES.getName());
		addDiffVersionSheetHeaderColumns(workbookSheetVersionChanges, workbookComponentCache);

		int currentRowNumber = 1;
		for (Map.Entry<DependencyInfoData, String> dependencyInfoDataStringEntry : diffData.getNewVersionItemsMap().entrySet()) {
			Row row = workbookSheetVersionChanges.createRow(currentRowNumber);
			SpreadsheetUtils.updateDiffRowWithPreviousVersionData(dependencyInfoDataStringEntry.getKey(), dependencyInfoDataStringEntry.getValue(), row);
			currentRowNumber++;
		}

		Sheet workbookSheetLicenseChanges = workbook.createSheet(SheetNames.LICENSE_CHANGES.getName());
		addDiffLicenseSheetHeaderColumns(workbookSheetLicenseChanges, workbookComponentCache);
		currentRowNumber = 1;

		for (Map.Entry<DependencyInfoData, String> dependencyInfoDataStringEntry : diffData.getLicenseNameChanges().entrySet()) {
			Row row = workbookSheetLicenseChanges.createRow(currentRowNumber);
			String prevLiFileName = diffData.getLicenseFileNameChanges().get(dependencyInfoDataStringEntry.getKey());
			SpreadsheetUtils.updateDiffRowWithPreviousLicenseData(dependencyInfoDataStringEntry.getKey(), dependencyInfoDataStringEntry.getValue(), prevLiFileName, row);
			currentRowNumber++;
		}

		ReportSpreadsheetUtils.applyFormattingDiffReport(workbook);

		File destinationReportFile = new File(fileName);
		SpreadsheetUtils.writeWorkbookToFile(workbook, new File(fileName).toPath(), log);

		return destinationReportFile.getAbsolutePath();
	}

	/**
	 * Reads inputReportFile1 file and returns a list of DependencyInfoData entries founded in file
	 *
	 * @param inputReportFile1
	 * @param log
	 * @return
	 * @throws MojoExecutionException
	 */
	public static List<DependencyInfoData> readDependencyInfoData(File inputReportFile1, Log log) throws MojoExecutionException {
		List<DependencyInfoData> result = new ArrayList<>();

		try (InputStream sourceInputStream = Files.newInputStream(inputReportFile1.toPath()); Workbook sourceWorkbook = XSSFWorkbookFactory.createWorkbook(sourceInputStream)) {
			Sheet reportsDetailedSheet = sourceWorkbook.getSheet(SheetNames.DETAILED.getName());
			if (reportsDetailedSheet == null) {
				throw new ReportGenerationException("Report contains no sheet named: " + SheetNames.DETAILED);
			}
			Iterator<Row> projectReportDetailsSheetRowIterator = reportsDetailedSheet.iterator();

			// skip first row, header information not needed
			if (projectReportDetailsSheetRowIterator.hasNext()) {
				projectReportDetailsSheetRowIterator.next();
			}

			while (projectReportDetailsSheetRowIterator.hasNext()) {
				Row projectReportDetailsSheetRow = projectReportDetailsSheetRowIterator.next();
				if (SpreadsheetUtils.isRowEmpty(projectReportDetailsSheetRow, SheetNames.DETAILED)) {
					// don't append empty rows
					continue;
				}
				DependencyInfoData dependencyInfoData = createDependencyInfoDataFromSheetRow(projectReportDetailsSheetRow);
				result.add(dependencyInfoData);
			}
		} catch (Exception e) {
			log.error("reportsDetailedSheet read failed. " + e.getMessage(), e);
			throw new MojoExecutionException(e.getMessage(), e);
		}

		return result;

	}

	/**
	 * Creates DependencyInfoData item from Dependency report file row
	 *
	 * @param row
	 * @return
	 */
	private static DependencyInfoData createDependencyInfoDataFromSheetRow(Row row) {
		DependencyInfoData dependencyInfoData = new DependencyInfoData();
		dependencyInfoData.setGroupId(SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.GROUPID.getColumnIndex())));
		dependencyInfoData.setArtifactId(SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.ARTIFACTID.getColumnIndex())));
		dependencyInfoData.setVersion(SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.VERSION.getColumnIndex())));
		dependencyInfoData.setRootProjectName(SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.EISCOMPONENT.getColumnIndex())));
		dependencyInfoData.setDependencyPath(SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.DEPENDENCY_PATH.getColumnIndex())));
		dependencyInfoData.setLicenseName(SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.LICENSE.getColumnIndex())));
		dependencyInfoData.setLicenseFileName(SpreadsheetUtils.getCellTextValue(row.getCell(DetailedSheetColumns.LICENSE_FILE_NAME.getColumnIndex())));
		return dependencyInfoData;
	}

	/**
	 * Adds header row to diff workbook
	 *
	 * @param detailedSheet
	 * @param workbookComponentCache
	 */
	private static void addDiffSheetHeaderColumns(Sheet detailedSheet, WorkbookReusableComponentCache workbookComponentCache) {
		Row row = detailedSheet.createRow(0);
		for (DiffSheetColumns column : DiffSheetColumns.values()) {
			Cell headerColumnCell = row.createCell(column.getColumnIndex());
			headerColumnCell.setCellStyle(workbookComponentCache.getCellStyle(WorkbookReusableComponentCache.CellStyles.CELL_STYLE_BOLD_WRAPPED));
			headerColumnCell.setCellValue(column.getColumnName());
		}
		detailedSheet.createFreezePane(DiffSheetColumns.values().length, 1);
	}


	/**
	 * Adds header row to diff workbook
	 *
	 * @param detailedSheet
	 * @param workbookComponentCache
	 */
	private static void addDiffLicenseSheetHeaderColumns(Sheet detailedSheet, WorkbookReusableComponentCache workbookComponentCache) {
		Row row = detailedSheet.createRow(0);
		for (DiffLicenseSheetColumns column : DiffLicenseSheetColumns.values()) {
			Cell headerColumnCell = row.createCell(column.getColumnIndex());
			headerColumnCell.setCellStyle(workbookComponentCache.getCellStyle(WorkbookReusableComponentCache.CellStyles.CELL_STYLE_BOLD_WRAPPED));
			headerColumnCell.setCellValue(column.getColumnName());
		}
		detailedSheet.createFreezePane(DiffLicenseSheetColumns.values().length, 1);
	}


	/**
	 * Adds header row to diff workbook
	 *
	 * @param detailedSheet
	 * @param workbookComponentCache
	 */
	private static void addDiffVersionSheetHeaderColumns(Sheet detailedSheet, WorkbookReusableComponentCache workbookComponentCache) {
		Row row = detailedSheet.createRow(0);
		for (DiffVersionSheetColumns column : DiffVersionSheetColumns.values()) {
			Cell headerColumnCell = row.createCell(column.getColumnIndex());
			headerColumnCell.setCellStyle(workbookComponentCache.getCellStyle(WorkbookReusableComponentCache.CellStyles.CELL_STYLE_BOLD_WRAPPED));
			headerColumnCell.setCellValue(column.getColumnName());
		}
		detailedSheet.createFreezePane(DiffVersionSheetColumns.values().length, 1);
	}

	/**
	 * Generates output report filename depending on local date time (in case report filename was not defined as input parameter)
	 *
	 * @return
	 */
	private String generateDestinationReportName() {
		if (destinationReportFileName != null && !StringUtils.isEmpty(destinationReportFileName)) {
			return destinationReportFileName;
		}
		return "dependency-report-diff-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd-HH_mm")) + ".xlsx";
	}

	/**
	 * Validate input parameters. Throws MojoExecutionException in case some attributes are invalid.
	 *
	 * @param reportFile1
	 * @param reportFile2
	 * @throws MojoExecutionException
	 */
	private void validateRequest(File reportFile1, File reportFile2) throws MojoExecutionException {
		if (!Arrays.asList(VALID_FORMATS).contains(reportFormat)) {
			throw new MojoExecutionException("'report-format' attribute is not valid. Valid attribute values: " + Stream.of(VALID_FORMATS).collect(Collectors.joining(",")));
		}

		if (!reportFile1.exists()) {
			throw new MojoExecutionException(reportFileName1 + " does not exist.");
		}
		if (!reportFile1.isFile()) {
			throw new MojoExecutionException(reportFileName1 + " is not a file.");
		}

		if (!reportFile2.exists()) {
			throw new MojoExecutionException(reportFileName2 + " does not exist.");
		}
		if (!reportFile2.isFile()) {
			throw new MojoExecutionException(reportFileName2 + " is not a file.");
		}
	}
}
