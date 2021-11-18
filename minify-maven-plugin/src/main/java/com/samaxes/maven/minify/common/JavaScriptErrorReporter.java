/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.samaxes.maven.minify.common;

import org.apache.maven.plugin.logging.Log;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 * A Rhino compatible error reporter.
 */
public class JavaScriptErrorReporter implements ErrorReporter {

    private Log log;

    private String filename;

    /**
     * Error reporter constructor.
     *
     * @param log Maven plugin log
     * @param filename JavaScript source file name
     */
    public JavaScriptErrorReporter(Log log, String filename) {
        this.log = log;
        this.filename = filename;
    }

    /**
     * Reports a warning.
     *
     * @param message a String describing the warning
     * @param sourceName a String describing the JavaScript source where the warning occurred; typically a file name or
     *        URL
     * @param line the line number associated with the warning
     * @param lineSource the text of the line (may be null)
     * @param lineOffset the offset into lineSource where problem was detected
     */
    @Override
    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        log.warn(constructMessage(message, sourceName, line, lineSource, lineOffset));
    }

    /**
     * Reports an error. If execution has not yet begun, the JavaScript engine is free to find additional errors rather
     * than terminating the translation. However, it will not execute a script that had errors.
     *
     * @param message a String describing the warning
     * @param sourceName a String describing the JavaScript source where the warning occurred; typically a file name or
     *        URL
     * @param line the line number associated with the warning
     * @param lineSource the text of the line (may be null)
     * @param lineOffset the offset into lineSource where problem was detected
     */
    @Override
    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        log.error(constructMessage(message, sourceName, line, lineSource, lineOffset));
    }

    /**
     * Creates an EvaluatorException that may be thrown. runtimeErrors, unlike errors, will always terminate the current
     * script.
     *
     * @param message a String describing the warning
     * @param sourceName a String describing the JavaScript source where the warning occurred; typically a file name or
     *        URL
     * @param line the line number associated with the warning
     * @param lineSource the text of the line (may be null)
     * @param lineOffset the offset into lineSource where problem was detected
     */
    @Override
    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
            int lineOffset) {
        log.error(message);

        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    private String constructMessage(String message, String sourceName, int line, String lineSource, int lineOffset) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(message).append(" at ");
        if (sourceName != null) {
            stringBuilder.append(sourceName);
        } else if (filename != null) {
            stringBuilder.append(filename);
        } else {
            stringBuilder.append("(unknown source)");
        }
        stringBuilder.append(" line ");
        if (line > 0) {
            stringBuilder.append(line);
        } else {
            stringBuilder.append("(unknown line)");
        }
        stringBuilder.append(":");
        if (lineOffset >= 0) {
            stringBuilder.append(lineOffset);
        } else {
            stringBuilder.append("(unknown column)");
        }
        if (lineSource != null) {
            stringBuilder.append('\n');
            stringBuilder.append(lineSource);
            if (lineOffset >= 0 && lineOffset <= lineSource.length()) {
                stringBuilder.append('\n');
                for (int i = 0; i < lineOffset; i++) {
                    char c = lineSource.charAt(i);
                    if (Character.isWhitespace(c)) {
                        stringBuilder.append(c);
                    } else {
                        stringBuilder.append(' ');
                    }
                }
                stringBuilder.append("^\n");
            }
        }

        return stringBuilder.toString();
    }
}
