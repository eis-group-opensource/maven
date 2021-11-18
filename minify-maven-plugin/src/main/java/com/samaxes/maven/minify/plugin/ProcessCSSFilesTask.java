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
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.samaxes.maven.minify.common.LocationReplaceMapping;
import com.samaxes.maven.minify.common.YuiConfig;
import com.samaxes.maven.minify.plugin.MinifyMojo.Engine;
import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * Task for merging and compressing CSS files.
 */
public class ProcessCSSFilesTask extends ProcessFilesTask {

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
     * @param sourceLastFiles list of source files to add at the end of minified file
     * @param outputDir directory to write the final file
     * @param outputFilename the output file name
     * @param engine minify processor engine selected
     * @param yuiConfig YUI Compressor configuration
     */
    public ProcessCSSFilesTask(Log log, boolean verbose, Integer bufferSize, String charset, String suffix,
            boolean nosuffix, boolean skipMerge, boolean skipMinify, String webappSourceDir, String webappTargetDir, String reportTargetDir, String reportFilename,
            List<LocationReplaceMapping> reportLocationReplaceMappings, String inputDir, List<String> sourceFiles, List<String> sourceIncludes, List<String> sourceExcludes,
            List<String> sourceLastFiles, String outputDir, String outputFilename, boolean addSourceMap, List<LocationReplaceMapping> sourceMapLocationMappings, Engine engine, YuiConfig yuiConfig) {
        
        super(log, verbose, bufferSize, charset, suffix, nosuffix, skipMerge, skipMinify, webappSourceDir,
                webappTargetDir, reportTargetDir, reportFilename, reportLocationReplaceMappings, inputDir, sourceFiles, sourceIncludes, sourceExcludes, sourceLastFiles, outputDir, outputFilename,
                addSourceMap, sourceMapLocationMappings, engine, yuiConfig);
    }

    /**
     * Minifies a CSS file.
     *
     * @param mergedFile input file resulting from the merged step
     * @param minifiedFile output file resulting from the minify step
     * @throws IOException when the minify step fails
     */
    @Override
    protected void minify(File mergedFile, File sourceMapFile, File minifiedFile) throws IOException {
        try {
		
			InputStream in = new FileInputStream(mergedFile);
            OutputStream out = new FileOutputStream(minifiedFile);
            InputStreamReader reader = new InputStreamReader(in, charset);
            OutputStreamWriter writer = new OutputStreamWriter(out, charset);
		
            log.info("Creating the minified file [" + ((verbose) ? minifiedFile.getPath() : minifiedFile.getName())
                    + "].");

            switch (engine) {
                case YUI:
                    log.debug("Using YUI Compressor engine.");

                    CssCompressor compressor = new CssCompressor(reader);
                    compressor.compress(writer, yuiConfig.getLinebreak());
                    break;
                default:
                    log.warn("CSS engine not supported.");
                    break;
            }
            
            writer.flush();
            writer.close();
            
        } catch (IOException e) {
            log.error("Failed to compress the CSS file [" + mergedFile.getName() + "].", e);
            throw e;
        }

        logCompressionGains(mergedFile, minifiedFile);
    }

}
