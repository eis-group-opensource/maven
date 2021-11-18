/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.samaxes.maven.minify.common;

import java.io.File;
import java.util.Comparator;

/**
 * Custom file name comparator. Compares file name instead of file path.
 */
public class FilenameComparator implements Comparator<File> {

    /**
     * Compares two file names lexicographically, ignoring case differences. This method returns an integer whose sign is
     * that of calling compareTo with normalized versions of the strings where case differences have been eliminated by
     * calling Character.toLowerCase(Character.toUpperCase(character)) on each character.
     *
     * @param o1 The first file object to be compared
     * @param o2 The second file object to be compared
     * @return Zero if both the files have the same name, a value less than zero if the first file is lexicographically
     *         less than the second, or a value greater than zero if the first file is lexicographically greater than
     *         the second
     */
    @Override
    public int compare(File o1, File o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
