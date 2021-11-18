/* Copyright © 2018 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws. 
CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent. */
package com.eisgroup.eis.maven.plugin;

import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.source.SourceJarNoForkMojo;
import org.apache.maven.project.MavenProject;

/**
 * Maven plugin Mojo to generate sources from specific directory.
 * 
 * @author anorkus
 */
@Mojo( name = "external-dir-jar", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true )
@Execute( phase = LifecyclePhase.GENERATE_SOURCES )
public class SourceDirJarMojo extends SourceJarNoForkMojo {

    @Parameter(property = "sourceRoot")
    protected String sourceRoot;

    @Override
    protected List<String> getSources(MavenProject p) {
        return Arrays.asList(sourceRoot);
    }

}
