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
import java.text.MessageFormat;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.LocationMapping;
import com.google.javascript.jscomp.WarningLevel;
import com.google.javascript.rhino.head.EvaluatorException;
import com.samaxes.maven.minify.common.ClosureConfig;
import com.samaxes.maven.minify.common.JavaScriptErrorReporter;
import com.samaxes.maven.minify.common.LocationReplaceMapping;
import com.samaxes.maven.minify.common.YuiConfig;
import com.samaxes.maven.minify.plugin.MinifyMojo.Engine;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * Task for merging and compressing JavaScript files.
 */
public class ProcessJSFilesTask extends ProcessFilesTask {

    private final ClosureConfig closureConfig;

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
     * @param closureConfig Google Closure Compiler configuration
     */
    public ProcessJSFilesTask(Log log, boolean verbose, Integer bufferSize, String charset, String suffix,
            boolean nosuffix, boolean skipMerge, boolean skipMinify, String webappSourceDir, String webappTargetDir, String reportTargetDir, String reportFilename,
            List<LocationReplaceMapping> reportLocationReplaceMappings, String inputDir, List<String> sourceFiles, List<String> sourceIncludes, List<String> sourceExcludes,
            List<String> sourceLastFiles, String outputDir, String outputFilename, boolean addSourceMap, List<LocationReplaceMapping> sourceMapLocationMappings, 
            Engine engine, YuiConfig yuiConfig, ClosureConfig closureConfig) {
        super(log, verbose, bufferSize, charset, suffix, nosuffix, skipMerge, skipMinify, webappSourceDir,
                webappTargetDir, reportTargetDir, reportFilename, reportLocationReplaceMappings, inputDir, sourceFiles, sourceIncludes, sourceExcludes, sourceLastFiles, 
                outputDir, outputFilename, addSourceMap, sourceMapLocationMappings, engine, yuiConfig);

        this.closureConfig = closureConfig;
    }

    /**
     * Minifies a JavaScript file.
     *
     * @param mergedFile input file resulting from the merged step
     * @param minifiedFile output file resulting from the minify step
     * @throws IOException when the minify step fails
     */
    @Override
    protected void minify(File mergedFile, File sourceMapFile, File minifiedFile) throws IOException {
        try {
			
            OutputStream out = new FileOutputStream(minifiedFile);
            OutputStreamWriter writer = new OutputStreamWriter(out, charset);
		
            log.info("Creating the minified file [" + ((verbose) ? minifiedFile.getPath() : minifiedFile.getName())
                    + "].");

            switch (engine) {
                case CLOSURE:
                    log.debug("Using Google Closure Compiler engine.");

                    CompilerOptions options = new CompilerOptions();
                    options.setOutputCharset(charset);
                    Format closureSourceMapFormat = closureConfig.getClosureSourceMapFormat();
                    OutputStreamWriter writerM = null;
                    if(sourceMapFile != null) {
                        if(closureSourceMapFormat != null) {
                            options.setSourceMapFormat(closureSourceMapFormat);
                        }
                        if(sourceMapLocationMappings != null) {
                            options.setSourceMapLocationMappings(Lists.newArrayList(Collections2.transform(sourceMapLocationMappings, new Function<LocationReplaceMapping, LocationMapping>() {
                                public LocationMapping apply(LocationReplaceMapping mapping) {
                                    String token = fixSlashes(mapping.getToken());
                                    String replace = fixSlashes(mapping.getReplace());
                                    if(verbose) {
                                        log.info(MessageFormat.format("Adding source map location mapping [{0}] -> [{1}]", token, replace));
                                    }
                                    return new LocationMapping(token, replace);
                                }

                                private String fixSlashes(String replace) {
                                    return replace.replace("/", FileUtils.FS).replace("\\", FileUtils.FS);
                                }
                                
                            })));
                        }
                        options.setSourceMapOutputPath(sourceMapFile.getPath());
                        OutputStream outM = new FileOutputStream(sourceMapFile);
                        writerM = new OutputStreamWriter(outM, charset);
                    }
                    closureConfig.getCompilationLevel().setOptionsForCompilationLevel(options);
                    options.setLanguageIn(closureConfig.getLanguage());
                    
                    List<SourceFile> externs = closureConfig.getExterns();
                    WarningLevel.QUIET.setOptionsForWarningLevel(options);
                    
                    Compiler compiler = new Compiler();
                    List<SourceFile> sources = null;
                    if(skipMerge) { 
                        sources = Lists.newArrayList(SourceFile.fromFile(mergedFile));
                    } else {
                        sources = Lists.newArrayList( Collections2.transform(files, new Function<File, SourceFile>() {
                            public SourceFile apply(File file) {
                                log.info("Processing source file [" + ((verbose) ? file.getPath() : file.getName()) + "].");
                                return SourceFile.fromFile(file);
                            }
                        }));
                    }
                    compiler.compile(externs, sources, options);

                    if (compiler.hasErrors()) {
                        throw new EvaluatorException(compiler.getErrors()[0].description);
                    }
                    
                    writer.append(compiler.toSource());
                    
                    if(writerM != null) {
                        writer.append("\n//# sourceMappingURL=" + sourceMapFile.getName());
                        compiler.getSourceMap().appendTo(writerM, minifiedFile.getName());
                        writerM.flush();
                        writerM.close();
                    }
                    break;
                case YUI:
                    
                    InputStream in = new FileInputStream(mergedFile);
                    InputStreamReader reader = new InputStreamReader(in, charset);
                    
                    log.debug("Using YUI Compressor engine.");

                    JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new JavaScriptErrorReporter(log,
                            mergedFile.getName()));
                    compressor.compress(writer, yuiConfig.getLinebreak(), yuiConfig.isMunge(), verbose,
                            yuiConfig.isPreserveAllSemiColons(), yuiConfig.isDisableOptimizations());
                    break;
                default:
                    log.warn("JavaScript engine not supported.");
                    break;
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("Failed to compress the JavaScript file [" + mergedFile.getName() + "].", e);
            throw e;
        }
        
        logCompressionGains(mergedFile, minifiedFile);
    }
    
    @Override
    protected void merge(File mergedFile) throws IOException {
        if(!Engine.CLOSURE.equals(engine)) {
            super.merge(mergedFile);
        }
    }
}
