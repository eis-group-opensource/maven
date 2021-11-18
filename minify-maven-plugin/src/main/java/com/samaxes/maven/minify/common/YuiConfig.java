/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.samaxes.maven.minify.common;

/**
 * <a href="http://yui.github.io/yuicompressor/">YUI Compressor</a> configuration.
 */
public class YuiConfig {

    private final int linebreak;

    private final boolean munge;

    private final boolean preserveAllSemiColons;

    private final boolean disableOptimizations;

    /**
     * Init YuiConfig values.
     *
     * @param linebreak split long lines after a specific column
     * @param munge minify only
     * @param preserveAllSemiColons preserve unnecessary semicolons
     * @param disableOptimizations disable all the built-in micro optimizations
     */
    public YuiConfig(int linebreak, boolean munge, boolean preserveAllSemiColons, boolean disableOptimizations) {
        this.linebreak = linebreak;
        this.munge = munge;
        this.preserveAllSemiColons = preserveAllSemiColons;
        this.disableOptimizations = disableOptimizations;
    }

    /**
     * Gets the linebreak.
     *
     * @return the linebreak
     */
    public int getLinebreak() {
        return linebreak;
    }

    /**
     * Gets the munge.
     *
     * @return the munge
     */
    public boolean isMunge() {
        return munge;
    }

    /**
     * Gets the preserveAllSemiColons.
     *
     * @return the preserveAllSemiColons
     */
    public boolean isPreserveAllSemiColons() {
        return preserveAllSemiColons;
    }

    /**
     * Gets the disableOptimizations.
     *
     * @return the disableOptimizations
     */
    public boolean isDisableOptimizations() {
        return disableOptimizations;
    }
}
