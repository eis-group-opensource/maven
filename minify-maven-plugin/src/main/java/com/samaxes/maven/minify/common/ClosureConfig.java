/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.samaxes.maven.minify.common;

import java.util.List;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceMap.Format;

/**
 * <a href="https://developers.google.com/closure/compiler/">Google Closure Compiler</a> configuration.
 */
public class ClosureConfig {

    private final LanguageMode language;

    private final CompilationLevel compilationLevel;

    private final List<SourceFile> externs;
    
    private Format closureSourceMapFormat;

    /**
     * Init Closure Compiler values.
     *
     * @param language the version of ECMAScript used to report errors in the code
     * @param compilationLevel the degree of compression and optimization to apply to JavaScript
     * @param externs preserve symbols that are defined outside of the code you are compiling
     * @param closureSourceMapFormat 
     */
    public ClosureConfig(LanguageMode language, CompilationLevel compilationLevel, List<SourceFile> externs, Format closureSourceMapFormat) {
        this.language = language;
        this.compilationLevel = compilationLevel;
        this.externs = externs;
        this.closureSourceMapFormat = closureSourceMapFormat;
    }

    /**
     * Gets the language.
     *
     * @return the language
     */
    public LanguageMode getLanguage() {
        return language;
    }

    /**
     * Gets the compilationLevel.
     *
     * @return the compilationLevel
     */
    public CompilationLevel getCompilationLevel() {
        return compilationLevel;
    }

    /**
     * Gets the externs.
     *
     * @return the externs
     */
    public List<SourceFile> getExterns() {
        return externs;
    }

    /**
     * Gets source map format
     * 
     * @return the source map format
     */
    public Format getClosureSourceMapFormat() {
        return closureSourceMapFormat;
    }
}
