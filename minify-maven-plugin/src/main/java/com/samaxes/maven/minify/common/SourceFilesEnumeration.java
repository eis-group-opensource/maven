/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.samaxes.maven.minify.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.maven.plugin.logging.Log;

/**
 * Used to initialize a {@code SequenceInputStream} with a {@code Enumeration<? extends InputStream>}. The input streams
 * that are produced by the enumeration will be read, in order, to provide the bytes to be read from the
 * {@code SequenceInputStream}.
 */
public class SourceFilesEnumeration implements Enumeration<InputStream> {

    private List<File> files;

    private int current = 0;

    /**
     * Enumeration public constructor.
     *
     * @param log Maven plugin log
     * @param files list of files
     * @param debug show source file paths in log output
     */
    public SourceFilesEnumeration(Log log, List<File> files, boolean debug) {
        this.files = files;

        for (File file : files) {
            log.info("Processing source file [" + ((debug) ? file.getPath() : file.getName()) + "].");
        }
    }

    /**
     * Tests if this enumeration contains more elements.
     *
     * @return {@code true} if and only if this enumeration object contains at least one more element to provide;
     *         {@code false} otherwise.
     */
    @Override
    public boolean hasMoreElements() {
        return (current < files.size()) ? true : false;
    }

    /**
     * Returns the next element of this enumeration if this enumeration object has at least one more element to provide.
     *
     * @return the next element of this enumeration.
     * @exception NoSuchElementException if no more elements exist.
     */
    @Override
    public InputStream nextElement() {
        InputStream is = null;

        if (!hasMoreElements()) {
            throw new NoSuchElementException("No more files!");
        } else {
            File nextElement = files.get(current);
            current++;

            try {
                is = new FileInputStream(nextElement);
            } catch (FileNotFoundException e) {
                throw new NoSuchElementException("The path [" + nextElement.getPath() + "] cannot be found.");
            }
        }

        return is;
    }
}
