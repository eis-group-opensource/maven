/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.samaxes.maven.minify.plugin;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.google.common.base.Strings;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceMap.Format;
import com.samaxes.maven.minify.common.ClosureConfig;
import com.samaxes.maven.minify.common.LocationReplaceMapping;
import com.samaxes.maven.minify.common.YuiConfig;

/**
 * Goal for combining and minifying CSS and JavaScript files.
 */
@Mojo(name = "minify", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, threadSafe = true)
public class MinifyMojo extends AbstractMojo {

    /**
     * Engine used for minification
     */
    public static enum Engine {
        /** YUI Compressor */
        YUI,
        /** Google Closure Compiler */
        CLOSURE;
    }

    /* ************** */
    /* Global Options */
    /* ************** */

    /**
     * Show source file paths in log output.
     *
     * @since 1.5.2
     * @deprecated Use {@link #verbose} instead.
     */
    @Deprecated
    @Parameter(property = "debug")
    private Boolean debug;

    /**
     * Display additional informational messages and warnings.
     */
    @Parameter(property = "verbose", defaultValue = "false")
    private boolean verbose;

    /**
     * Size of the buffer used to read source files.
     */
    @Parameter(property = "bufferSize", defaultValue = "4096")
    private int bufferSize;

    /**
     * If a supported character set is specified, it will be used to read the input file. Otherwise, it will assume that
     * the platform's default character set is being used. The output file is encoded using the same character set.<br/>
     * See the <a href="http://www.iana.org/assignments/character-sets">IANA Charset Registry</a> for a list of valid
     * encoding types.
     *
     * @since 1.3.2
     */
    @Parameter(property = "charset", defaultValue = "${project.build.sourceEncoding}")
    private String charset;

    /**
     * The output file name suffix.
     *
     * @since 1.3.2
     */
    @Parameter(property = "suffix", defaultValue = "min")
    private String suffix;

    /**
     * Do not append a suffix to the minified output file name, independently of the value in the {@code suffix}
     * parameter.
     *
     * @since 1.7
     */
    @Parameter(property = "nosuffix", defaultValue = "false")
    private boolean nosuffix;

    /**
     * Skip the merge step. Minification will be applied to each source file individually.
     *
     * @since 1.5.2
     */
    @Parameter(property = "skipMerge", defaultValue = "false")
    private boolean skipMerge;

    /**
     * Skip the minify step. Useful when merging files that are already minified.
     *
     * @since 1.5.2
     */
    @Parameter(property = "skipMinify", defaultValue = "false")
    private boolean skipMinify;

    /**
     * Webapp source directory.
     */
    @Parameter(property = "webappSourceDir", defaultValue = "${basedir}/src/main/webapp")
    private String webappSourceDir;

    /**
     * Webapp target directory.
     */
    @Parameter(property = "webappTargetDir", defaultValue = "${project.build.directory}/${project.build.finalName}")
    private String webappTargetDir;
    
    /**
     * Webapp classes directory.
     */
    @Parameter(defaultValue = "WEB-INF/classes")
    private String reportTargetDir;
    
    /**
     * Report resource location replace mapping
     */
    @Parameter
    private List<LocationReplaceMapping> reportLocationReplaceMappings;

    /* *********** */
    /* CSS Options */
    /* *********** */

    /**
     * CSS source directory.
     */
    @Parameter(property = "cssSourceDir", defaultValue = "css")
    private String cssSourceDir;

    /**
     * CSS source file names list.
     */
    @Parameter(property = "cssSourceFiles", alias = "cssFiles")
    private ArrayList<String> cssSourceFiles;

    /**
     * CSS files to include. Specified as fileset patterns which are relative to the CSS source directory.
     *
     * @since 1.2
     */
    @Parameter(property = "cssSourceIncludes", alias = "cssIncludes")
    private ArrayList<String> cssSourceIncludes;

    /**
     * CSS files to exclude. Specified as fileset patterns which are relative to the CSS source directory.
     *
     * @since 1.2
     */
    @Parameter(property = "cssSourceExcludes", alias = "cssExcludes")
    private ArrayList<String> cssSourceExcludes;
    
    /**
     * CSS files to add at the end of the minified file.
     * Specified as fileset patterns which are relative to the CSS source directory.
     * 
     * @since 1.7.3-e8
     */
    @Parameter(property = "cssSourceLastFiles")
    private ArrayList<String> cssSourceLastFiles;

    /**
     * CSS target directory. Takes the same value as {@code cssSourceDir} when empty.
     *
     * @since 1.3.2
     */
    @Parameter(property = "cssTargetDir")
    private String cssTargetDir;

    /**
     * CSS output file name.
     */
    @Parameter(property = "cssFinalFile", defaultValue = "style.css")
    private String cssFinalFile;
    
    /**
     * CSS output file name.
     */
    @Parameter(property = "addCssSourceMap", defaultValue = "false")
    private boolean addCssSourceMap;
    
    /**
     * Location replaces for css source map
     */
    @Parameter
    private List<LocationReplaceMapping> cssSourceMapLocationMappings;
    
    /**
     * Minified file report filename
     */
    @Parameter(defaultValue = "minified-css.properties")
    private String cssReportFinalFile;

    /**
     * Define the CSS compressor engine to use.<br/>
     * Possible values are:
     * <ul>
     * <li>{@code YUI}: <a href="http://yui.github.io/yuicompressor/">YUI Compressor</a></li>
     * </ul>
     *
     * @since 1.7.1
     */
    @Parameter(property = "cssEngine", defaultValue = "YUI")
    private Engine cssEngine;

    /* ****************** */
    /* JavaScript Options */
    /* ****************** */

    /**
     * JavaScript source directory.
     */
    @Parameter(property = "jsSourceDir", defaultValue = "js")
    private String jsSourceDir;

    /**
     * JavaScript source file names list.
     */
    @Parameter(property = "jsSourceFiles", alias = "jsFiles")
    private ArrayList<String> jsSourceFiles;

    /**
     * JavaScript files to include. Specified as fileset patterns which are relative to the JavaScript source directory.
     *
     * @since 1.2
     */
    @Parameter(property = "jsSourceIncludes", alias = "jsIncludes")
    private ArrayList<String> jsSourceIncludes;

    /**
     * JavaScript files to exclude. Specified as fileset patterns which are relative to the JavaScript source directory.
     *
     * @since 1.2
     */
    @Parameter(property = "jsSourceExcludes", alias = "jsExcludes")
    private ArrayList<String> jsSourceExcludes;

    /**
     * JavaScript files to add at the end of the minified file.
     * Specified as fileset patterns which are relative to the JavaScript source directory.
     * 
     * @since 1.7.3-e8
     */
    @Parameter(property = "jsSourceLastFiles")
    private ArrayList<String> jsSourceLastFiles;

    /**
     * JavaScript target directory. Takes the same value as {@code jsSourceDir} when empty.
     *
     * @since 1.3.2
     */
    @Parameter(property = "jsTargetDir")
    private String jsTargetDir;

    /**
     * JavaScript output file name.
     */
    @Parameter(property = "jsFinalFile", defaultValue = "script.js")
    private String jsFinalFile;
    
    /**
     * JavaScript output map file name.
     */
    @Parameter(property = "addJsSourceMap", defaultValue = "false")
    private boolean addJsSourceMap;
    
    /**
     * Location replaces for js source map
     */
    @Parameter
    private List<LocationReplaceMapping> jsSourceMapLocationMappings;

    /**
     * Define the JavaScript compressor engine to use.<br/>
     * Possible values are:
     * <ul>
     * <li>{@code YUI}: <a href="http://yui.github.io/yuicompressor/">YUI Compressor</a></li>
     * <li>{@code CLOSURE}: <a href="https://developers.google.com/closure/compiler/">Google Closure Compiler</a></li>
     * </ul>
     *
     * @since 1.6
     */
    @Parameter(property = "jsEngine", defaultValue = "YUI")
    private Engine jsEngine;
    
    /**
     * Minified file report filename
     */
    @Parameter(defaultValue = "minified-js.properties")
    private String jsReportFinalFile;

    /* *************************** */
    /* YUI Compressor Only Options */
    /* *************************** */

    /**
     * Some source control tools don't like files containing lines longer than, say 8000 characters. The linebreak
     * option is used in that case to split long lines after a specific column. It can also be used to make the code
     * more readable and easier to debug. Specify {@code 0} to get a line break after each semi-colon in JavaScript, and
     * after each rule in CSS. Specify {@code -1} to disallow line breaks.
     *
     * @deprecated Use {@link #yuiLinebreak} instead.
     */
    @Deprecated
    @Parameter(property = "linebreak")
    private Integer linebreak;

    /**
     * Some source control tools don't like files containing lines longer than, say 8000 characters. The linebreak
     * option is used in that case to split long lines after a specific column. It can also be used to make the code
     * more readable and easier to debug. Specify {@code 0} to get a line break after each semi-colon in JavaScript, and
     * after each rule in CSS. Specify {@code -1} to disallow line breaks.
     */
    @Parameter(property = "yuiLinebreak", defaultValue = "-1")
    private int yuiLinebreak;

    /**
     * Minify only. Do not obfuscate local symbols.
     *
     * @deprecated Use {@link #yuiMunge} instead.
     */
    @Deprecated
    @Parameter(property = "munge")
    private Boolean munge;

    /**
     * Minify only. Do not obfuscate local symbols.
     */
    @Parameter(property = "yuiMunge", defaultValue = "true")
    private boolean yuiMunge;

    /**
     * Preserve unnecessary semicolons (such as right before a '}'). This option is useful when compressed code has to
     * be run through JSLint.
     *
     * @deprecated Use {@link #yuiPreserveAllSemiColons} instead.
     */
    @Deprecated
    @Parameter(property = "preserveAllSemiColons")
    private Boolean preserveAllSemiColons;

    /**
     * Preserve unnecessary semicolons (such as right before a '}'). This option is useful when compressed code has to
     * be run through JSLint.
     */
    @Parameter(property = "yuiPreserveAllSemiColons", defaultValue = "false")
    private boolean yuiPreserveAllSemiColons;

    /**
     * Disable all the built-in micro-optimizations.
     *
     * @deprecated Use {@link #yuiDisableOptimizations} instead.
     */
    @Deprecated
    @Parameter(property = "disableOptimizations")
    private Boolean disableOptimizations;

    /**
     * Disable all the built-in micro-optimizations.
     */
    @Parameter(property = "yuiDisableOptimizations", defaultValue = "false")
    private boolean yuiDisableOptimizations;

    /* ************************************ */
    /* Google Closure Compiler Only Options */
    /* ************************************ */

    /**
     * Refers to which version of ECMAScript to assume when checking for errors in your code.<br/>
     * Possible values are:
     * <ul>
     * <li>{@code ECMASCRIPT3}: Checks code assuming ECMAScript 3 compliance, and gives errors for code using features
     * only present in ECMAScript 5.</li>
     * <li>{@code ECMASCRIPT5}: Checks code assuming ECMAScript 5 compliance, allowing new features not present in
     * ECMAScript 3.</li>
     * <li>{@code ECMASCRIPT5_STRICT}: Like {@code ECMASCRIPT5} but assumes compliance with strict mode ('use strict';).
     * </li>
     * </ul>
     *
     * @since 1.7.2
     */
    @Parameter(property = "closureLanguageIn", defaultValue = "ECMASCRIPT3")
    private LanguageMode closureLanguage;

    /**
     * The degree of compression and optimization to apply to your JavaScript.<br/>
     * There are three possible compilation levels:
     * <ul>
     * <li>{@code WHITESPACE_ONLY}: Just removes whitespace and comments from your JavaScript.</li>
     * <li>{@code SIMPLE_OPTIMIZATIONS}: Performs compression and optimization that does not interfere with the
     * interaction between the compiled JavaScript and other JavaScript. This level renames only local variables.</li>
     * <li>{@code ADVANCED_OPTIMIZATIONS}: Achieves the highest level of compression by renaming symbols in your
     * JavaScript. When using {@code ADVANCED_OPTIMIZATIONS} compilation you must perform extra steps to preserve
     * references to external symbols. See <a href="/closure/compiler/docs/api-tutorial3">Advanced Compilation and
     * Externs</a> for more information about {@code ADVANCED_OPTIMIZATIONS}.</li>
     * </ul>
     *
     * @since 1.7.2
     */
    @Parameter(property = "closureCompilationLevel", defaultValue = "SIMPLE_OPTIMIZATIONS")
    private CompilationLevel closureCompilationLevel;

    /**
     * List of JavaScript files containing code that declares function names or other symbols. Use
     * {@code closureExterns} to preserve symbols that are defined outside of the code you are compiling. The
     * {@code closureExterns} parameter only has an effect if you are using a {@code CompilationLevel} of
     * {@code ADVANCED_OPTIMIZATIONS}.<br/>
     * These file names are relative to {@link #webappSourceDir} directory.
     *
     * @since 1.7.2
     */
    @Parameter(property = "closureExterns")
    private ArrayList<String> closureExterns;
    
    /**
     * Closure source map format that is passed to closure compiler
     *
     * @since 1.7.3
     */
    @Parameter(property = "closureExterns")
    private Format closureSourceMapFormat;
    

    /**
     * Executed when the goal is invoked, it will first invoke a parallel lifecycle, ending at the given phase.
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        checkDeprecatedOptions();

        if (skipMerge && skipMinify) {
            return;
        }

        fillOptionalValues();
        YuiConfig yuiConfig = fillYuiConfig();
        ClosureConfig closureConfig = fillClosureConfig();

        Collection<ProcessFilesTask> processFilesTasks = new ArrayList<ProcessFilesTask>();
        processFilesTasks.add(new ProcessCSSFilesTask(getLog(), debug, bufferSize, charset, suffix, nosuffix, skipMerge, 
                skipMinify, webappSourceDir, webappTargetDir, reportTargetDir, cssReportFinalFile, reportLocationReplaceMappings, cssSourceDir, cssSourceFiles,
                cssSourceIncludes, cssSourceExcludes, cssSourceLastFiles, cssTargetDir, cssFinalFile, addCssSourceMap, cssSourceMapLocationMappings, cssEngine, yuiConfig));
        processFilesTasks.add(new ProcessJSFilesTask(getLog(), debug, bufferSize, charset, suffix, nosuffix, skipMerge,
                skipMinify, webappSourceDir, webappTargetDir, reportTargetDir, jsReportFinalFile, reportLocationReplaceMappings, jsSourceDir, jsSourceFiles, 
                jsSourceIncludes, jsSourceExcludes, jsSourceLastFiles, jsTargetDir, jsFinalFile, addJsSourceMap, jsSourceMapLocationMappings, jsEngine, yuiConfig, closureConfig));

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Future<Object>> futures = executor.invokeAll(processFilesTasks);
            for (Future<Object> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    throw new MojoFailureException(e.getMessage(), e);
                }
            }
            executor.shutdown();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            throw new MojoFailureException(e.getMessage(), e);
        }
    }

    private void checkDeprecatedOptions() {
        if (debug == null) {
            debug = verbose;
        } else {
            getLog().warn(
                    "The option 'debug' is deprecated and will be removed on the next version. Use 'verbose' instead.");
        }
        if (linebreak == null) {
            linebreak = yuiLinebreak;
        } else {
            getLog().warn(
                    "The option 'linebreak' is deprecated and will be removed on the next version. Use 'yuiLinebreak' instead.");
        }
        if (munge == null) {
            munge = yuiMunge;
        } else {
            getLog().warn(
                    "The option 'munge' is deprecated and will be removed on the next version. Use 'yuiMunge' instead.");
        }
        if (preserveAllSemiColons == null) {
            preserveAllSemiColons = yuiPreserveAllSemiColons;
        } else {
            getLog().warn(
                    "The option 'preserveAllSemiColons' is deprecated and will be removed on the next version. Use 'yuiPreserveAllSemiColons' instead.");
        }
        if (disableOptimizations == null) {
            disableOptimizations = yuiDisableOptimizations;
        } else {
            getLog().warn(
                    "The option 'disableOptimizations' is deprecated and will be removed on the next version. Use 'yuiDisableOptimizations' instead.");
        }
    }

    private void fillOptionalValues() {
        if (Strings.isNullOrEmpty(cssTargetDir)) {
            cssTargetDir = cssSourceDir;
        }
        if (Strings.isNullOrEmpty(jsTargetDir)) {
            jsTargetDir = jsSourceDir;
        }
    }

    private YuiConfig fillYuiConfig() {
        return new YuiConfig(linebreak, munge, preserveAllSemiColons, disableOptimizations);
    }

    private ClosureConfig fillClosureConfig() {
        List<SourceFile> externs = new ArrayList();
        for (String extern : closureExterns) {
            externs.add(SourceFile.fromFile(webappSourceDir + File.separator + extern, Charset.forName(charset)));
        }
        return new ClosureConfig(closureLanguage, closureCompilationLevel, externs, closureSourceMapFormat);
    }
}
