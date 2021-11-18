/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.samaxes.maven.minify.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.zip.GZIPOutputStream;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import com.samaxes.maven.minify.common.LocationReplaceMapping;
import com.samaxes.maven.minify.common.SourceFilesEnumeration;
import com.samaxes.maven.minify.common.YuiConfig;
import com.samaxes.maven.minify.plugin.MinifyMojo.Engine;

/**
 * Abstract class for merging and compressing a files list.
 */
public abstract class ProcessFilesTask implements Callable<Object> {

    public static final String TEMP_SUFFIX = ".tmp";

    protected final Log log;

    protected final boolean verbose;

    protected final Integer bufferSize;

    protected final String charset;

    protected final String suffix;

    protected final boolean nosuffix;

    protected final boolean skipMerge;

    protected final boolean skipMinify;

    protected final Engine engine;

    protected final YuiConfig yuiConfig;

    private final File sourceDir;

    private final File targetDir;

    private final String mergedFilename;
    
    protected final List<File> files = new ArrayList<File>();

    private final boolean sourceFilesEmpty;

    private final boolean sourceIncludesEmpty;

    private boolean addSourceMap;
    
    protected List<LocationReplaceMapping> sourceMapLocationMappings;

    private String reportFilename;

    private File reportDir;

    private List<LocationReplaceMapping> reportLocationReplaceMappings;

    /**
     * Task constructor.
     *
     * @param log Maven plugin log
     * @param verbose display additional info
     * @param bufferSize size of the buffer used to read source files
     * @param charset if a character set is specified, a byte-to-char variant allows the encoding to be selected.
     *        Otherwise, only byte-to-byte operations are used
     * @param suffix final file name suffix
     * @param nosuffix whether to use a suffix for the minified file name or not
     * @param skipMerge whether to skip the merge step or not
     * @param skipMinify whether to skip the minify step or not
     * @param webappSourceDir web resources source directory
     * @param webappTargetDir web resources target directory
     * @param reportLocationReplaceMappings 
     * @param inputDir directory containing source files
     * @param sourceFiles list of source files to include
     * @param sourceIncludes list of source files to include
     * @param sourceExcludes list of source files to exclude
     * @param sourceLastFiles list of source files to move/add at the end of the minified file
     * @param outputDir directory to write the final file
     * @param outputFilename the output file name
     * @param sourceMapLocationMappings 
     * @param engine minify processor engine selected
     * @param yuiConfig YUI Compressor configuration
     */
    public ProcessFilesTask(Log log, boolean verbose, Integer bufferSize, String charset, String suffix,
            boolean nosuffix, boolean skipMerge, boolean skipMinify, String webappSourceDir, String webappTargetDir, String reportTargetDir, String reportFilename,
            List<LocationReplaceMapping> reportLocationReplaceMappings, String inputDir, List<String> sourceFiles, List<String> sourceIncludes, List<String> sourceExcludes,
            List<String> sourceLastFiles, String outputDir, String outputFilename, boolean addSourceMap, List<LocationReplaceMapping> sourceMapLocationMappings, Engine engine, YuiConfig yuiConfig) {
        this.log = log;
        this.verbose = verbose;
        this.bufferSize = bufferSize;
        this.charset = charset;
        this.suffix = suffix + ".";
        this.nosuffix = nosuffix;
        this.skipMerge = skipMerge;
        this.skipMinify = skipMinify;
        this.engine = engine;
        this.yuiConfig = yuiConfig;

        this.sourceDir = new File(webappSourceDir + File.separator + inputDir);
        this.targetDir = new File(webappTargetDir + File.separator + outputDir);
        this.reportDir = new File(webappTargetDir + File.separator + reportTargetDir);
        this.mergedFilename = outputFilename;
        for (String sourceFilename : sourceFiles) {
            addNewSourceFile(mergedFilename, sourceFilename);
        }
        for (File sourceInclude : getFilesToInclude(sourceIncludes, sourceExcludes, sourceLastFiles)) {
            if (!files.contains(sourceInclude)) {
                addNewSourceFile(mergedFilename, sourceInclude);
            }
        }
        this.sourceFilesEmpty = sourceFiles.isEmpty();
        this.sourceIncludesEmpty = sourceIncludes.isEmpty();
        this.addSourceMap = addSourceMap;
        this.sourceMapLocationMappings = sourceMapLocationMappings;
        this.reportFilename = reportFilename;
        this.reportLocationReplaceMappings = reportLocationReplaceMappings;
    }

    /**
     * Method executed by the thread.
     *
     * @throws IOException when the merge or minify steps fail
     */
    @Override
    public Object call() throws IOException {
        synchronized (log) {
            String fileType = (this instanceof ProcessCSSFilesTask) ? "CSS" : "JavaScript";
            log.info("Starting " + fileType + " task:");
            
            if (!files.isEmpty() && (targetDir.exists() || targetDir.mkdirs())) {
                
                if(!reportDir.exists()) {
                    reportDir.mkdirs();
                }
                OutputStream out = new FileOutputStream(new File(reportDir, reportFilename));
                OutputStreamWriter outWriter = new OutputStreamWriter(out, charset);
                
                if (skipMerge) {
                    log.info("Skipping the merge step...");
                    String sourceBasePath = sourceDir.getAbsolutePath();

                    for (File mergedFile : files) {
                        // Create folders to preserve sub-directory structure when only minifying
                        String originalPath = mergedFile.getAbsolutePath();
                        String subPath = originalPath.substring(sourceBasePath.length(),
                                originalPath.lastIndexOf(File.separator));
                        File targetPath = new File(targetDir.getAbsolutePath() + subPath);
                        targetPath.mkdirs();

                        String fileName = (nosuffix) ? mergedFile.getName()
                                : FileUtils.basename(mergedFile.getName()) + suffix
                                        + FileUtils.getExtension(mergedFile.getName());
                        File minifiedFile = new File(targetPath, fileName);
                        File sourceMapFile = new File(targetPath, fileName + ".map");
                        printToReport(outWriter, mergedFile, minifiedFile);
                        minify(mergedFile, sourceMapFile, minifiedFile);
                    }
                } else if (skipMinify) {
                    File mergedFile = new File(targetDir, mergedFilename);
                    merge(mergedFile);
                    log.info("Skipping the minify step...");
                } else {
                    File mergedFile = new File(targetDir, (nosuffix) ? mergedFilename + TEMP_SUFFIX : mergedFilename);
                    merge(mergedFile);
                    String fileName = (nosuffix) ? mergedFilename
                            : FileUtils.basename(mergedFilename) + suffix + FileUtils.getExtension(mergedFilename);
                    File minifiedFile = new File(targetDir, fileName);
                    File sourceMapFile = null;
                    if(addSourceMap) {
                        sourceMapFile = new File(targetDir, fileName + ".map");
                    }
                    for(File sourceFile : files) {
                        printToReport(outWriter, sourceFile, minifiedFile);
                    }
                    minify(mergedFile, sourceMapFile, minifiedFile);
                    if (nosuffix) {
                        if (!mergedFile.delete()) {
                            mergedFile.deleteOnExit();
                        }
                    }
                }
                
                outWriter.flush();
                outWriter.close();
                
                log.info("");
            } else if (!sourceFilesEmpty || !sourceIncludesEmpty) {
                // 'files' list will be empty if source file paths or names added to the project's POM are invalid.
                log.error("No valid " + fileType + " source files found to process.");
            }
            
        }

        return null;
    }

    /**
     * Merges a list of source files.
     *
     * @param mergedFile output file resulting from the merged step
     * @throws IOException when the merge step fails
     */
    protected void merge(File mergedFile) throws IOException {
        try {
			InputStream sequence = new SequenceInputStream(new SourceFilesEnumeration(log, files, verbose));
            OutputStream out = new FileOutputStream(mergedFile);
            InputStreamReader sequenceReader = new InputStreamReader(sequence, charset);
            OutputStreamWriter outWriter = new OutputStreamWriter(out, charset);
		
            log.info("Creating the merged file [" + ((verbose) ? mergedFile.getPath() : mergedFile.getName()) + "].");

            IOUtil.copy(sequenceReader, outWriter, bufferSize);
        } catch (IOException e) {
            log.error("Failed to concatenate files.", e);
            throw e;
        }
    }

    /**
     * Minifies a source file.
     *
     * @param mergedFile input file resulting from the merged step
     * @param minifiedFile output file resulting from the minify step
     * @throws IOException when the minify step fails
     */
    abstract void minify(File mergedFile, File sourceMapFile, File minifiedFile) throws IOException;
    
    /**
     * Prints file mapping to report file
     * 
     * @param reportWriter - report file writer
     * @param sourceFile - filed that was minified
     * @param targetFile - minified file
     * @throws IOException - if failed to write to report
     */
    protected void printToReport(OutputStreamWriter reportWriter, File sourceFile, File targetFile) throws IOException {
        reportWriter.write(processReportLocation(sourceDir.getPath(), sourceFile.getPath()) + "="
                + processReportLocation(targetDir.getPath(), targetFile.getPath()) + "\n");
    }
    
    /**
     * Converts report resource location to properties file format
     * 
     * @param baseLocation - absolute path to resource directory
     * @param fileLocation - absolute location of resource file
     * @return normalized resource relative path
     */
    private String processReportLocation(String baseLocation, String fileLocation) {
        String result = fileLocation.replace(baseLocation + File.separator, "").replace('\\', '/');
        if (reportLocationReplaceMappings != null) {
            for (LocationReplaceMapping rep : reportLocationReplaceMappings) {
                result = result.replaceAll(rep.getToken(), rep.getReplace());
            }
        }
        return result;
    }

    /**
     * Logs compression gains.
     *
     * @param mergedFile input file resulting from the merged step
     * @param minifiedFile output file resulting from the minify step
     */
    void logCompressionGains(File mergedFile, File minifiedFile) {
        try {
            File temp = File.createTempFile(minifiedFile.getName(), ".gz");

            try {
				InputStream in = new FileInputStream(minifiedFile);
                OutputStream out = new FileOutputStream(temp);
                GZIPOutputStream outGZIP = new GZIPOutputStream(out);
                IOUtil.copy(in, outGZIP, bufferSize);
            } finally {}

            log.info("Uncompressed size: " + mergedFile.length() + " bytes.");
            log.info("Compressed size: " + minifiedFile.length() + " bytes minified (" + temp.length()
                    + " bytes gzipped).");

            temp.deleteOnExit();
        } catch (IOException e) {
            log.debug("Failed to calculate the gzipped file size.", e);
        }
    }

    /**
     * Logs an addition of a new source file.
     *
     * @param finalFilename the final file name
     * @param sourceFilename the source file name
     */
    private void addNewSourceFile(String finalFilename, String sourceFilename) {
        File sourceFile = new File(sourceDir, sourceFilename);

        addNewSourceFile(finalFilename, sourceFile);
    }

    /**
     * Logs an addition of a new source file.
     *
     * @param finalFilename the final file name
     * @param sourceFile the source file
     */
    private void addNewSourceFile(String finalFilename, File sourceFile) {
        if (sourceFile.exists()) {
            if (finalFilename.equalsIgnoreCase(sourceFile.getName())) {
                log.warn("The source file [" + ((verbose) ? sourceFile.getPath() : sourceFile.getName())
                        + "] has the same name as the final file.");
            }
            log.debug("Adding source file [" + ((verbose) ? sourceFile.getPath() : sourceFile.getName()) + "].");
            files.add(sourceFile);
        } else {
            log.warn("The source file [" + ((verbose) ? sourceFile.getPath() : sourceFile.getName())
                    + "] does not exist.");
        }
    }

    /**
     * Returns the files to copy. Default exclusions are used when the excludes list is empty.
     *
     * @param includes list of source files to include
     * @param excludes list of source files to exclude
     * @param lastFiles list of source files to add at the end
     * @return the files to copy
     */
    private List<File> getFilesToInclude(List<String> includes, List<String> excludes, List<String> lastFiles) {

        List<File> includedFiles = new ArrayList<File>();
        List<File> overrides = new ArrayList<File>();

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.addDefaultExcludes();
        scanner.setExcludes(excludes.toArray(new String[0]));
        scanner.setBasedir(sourceDir);

        if (includes != null && !includes.isEmpty()) {

            for (String include : includes) {
                scanner.setIncludes(new String[] { include });
                scanner.scan();

                for (String includedFilename : scanner.getIncludedFiles()) {
                    includedFiles.add(new File(sourceDir, includedFilename));
                }
            }

            for (String lastFile : lastFiles) {
                scanner.setIncludes(new String[] { lastFile });
                scanner.scan();

                for (String includedFilename : scanner.getIncludedFiles()) {
                    File aFile = new File(sourceDir, includedFilename);
                    overrides.remove(aFile);
                    overrides.add(aFile);
                }
            }
            includedFiles.removeAll(overrides);
            includedFiles.addAll(overrides);
        }

        return includedFiles;
    }
}
