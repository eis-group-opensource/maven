/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package com.exigen.ipb.base.swagger.plugin;

import static io.swagger.codegen.config.CodegenConfiguratorUtils.applyAdditionalPropertiesKvp;
import static io.swagger.codegen.config.CodegenConfiguratorUtils.applyImportMappingsKvp;
import static io.swagger.codegen.config.CodegenConfiguratorUtils.applyInstantiationTypesKvp;
import static io.swagger.codegen.config.CodegenConfiguratorUtils.applyLanguageSpecificPrimitivesCsv;
import static io.swagger.codegen.config.CodegenConfiguratorUtils.applyTypeMappingsKvp;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.swagger.codegen.ClientOptInput;
import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.DefaultGenerator;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.config.CodegenConfigurator;

/**
 * Goal which generates html page from a swagger json/yaml definition.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.NONE, aggregator = true)
public class GenerateSwaggerHtmlMojo extends AbstractMojo {

	private static final String FILE_ENCODING = "UTF-8";
	
    @Parameter(name="verbose", required = false, defaultValue = "false")
    private boolean verbose;

    /**
     * Client language to generate.
     */
    @Parameter(name = "language", 
    		defaultValue = "com.exigen.ipb.base.swagger.codegen.languages.EISSwaggerHtmlGenerator")
    private String language;

    /**
     * Location of the output directory.
     */
    @Parameter(name = "output",
            property = "com.exigen.ipb.base.swagger.plugin.output",
            defaultValue = "${project.build.directory}/generated-sources/swagger")
    private File output;

    /**
     * Location of the swagger spec (comma separated list of URLs), as URL or file.
     */
    @Parameter(name = "specs", required = true)
    private String specs;
    
    /**
     * Name of the output file.
     */
    @Parameter(name = "destinationFile", 
    		property = "com.exigen.ipb.base.swagger.plugin.destinationFile", 
    		defaultValue = "swagger.html")
    private String destinationFile;

    /**
     * Folder containing the template files.
     */
    @Parameter(name = "templateDirectory", property = "com.exigen.ipb.base.swagger.plugin.templateDirectory")
    private File templateDirectory;

    /**
     * Adds authorization headers when fetching the swagger definitions remotely.
     * Pass in a URL-encoded string of name:header with a comma separating multiple values
     */
    @Parameter(name="auth", property = "com.exigen.ipb.base.swagger.plugin.auth")
    private String auth;

    /**
     * Path to separate json configuration file.
     */
    @Parameter(name = "configurationFile", required = false)
    private String configurationFile;

    /**
     * Specifies if the existing files should be overwritten during the generation.
     */
    @Parameter(name="skipOverwrite", required = false)
    private Boolean skipOverwrite;

    /**
     * The package to use for generated api objects/classes
     */
    @Parameter(name = "apiPackage")
    private String apiPackage;

    /**
     * The package to use for generated model objects/classes
     */
    @Parameter(name = "modelPackage")
    private String modelPackage;

    /**
     * The package to use for the generated invoker objects
     */
    @Parameter(name = "invokerPackage")
    private String invokerPackage;

    /**
     * groupId in generated pom.xml
     */
    @Parameter(name = "groupId")
    private String groupId;

    /**
     * artifactId in generated pom.xml
     */
    @Parameter(name = "artifactId")
    private String artifactId;

    /**
     * artifact version in generated pom.xml
     */
    @Parameter(name = "artifactVersion")
    private String artifactVersion;

    /**
     * Sets the library
     */
    @Parameter(name = "library", required = false)
    private String library;

    /**
     * A map of language-specific parameters as passed with the -c option to the command line
     */
    @Parameter(name = "configOptions")
    private Map<?, ?> configOptions;

    /**
     * Add the output directory to the project as a source root, so that the
     * generated java types are compiled and included in the project artifact.
     */
    @Parameter(defaultValue = "true")
    private boolean addCompileSourceRoot = true;

    @Parameter
    protected Map<String, String> environmentVariables = new HashMap<String, String>();

    @Parameter
    private boolean configHelp = false;

    /**
     * The project being built.
     */
    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {

        //attempt to read from config file
        CodegenConfigurator configurator = CodegenConfigurator.fromFile(configurationFile);

        //if a config file wasn't specified or we were unable to read it
        if(configurator == null) {
            configurator = new CodegenConfigurator();
        }

        configurator.setVerbose(verbose);

        if(skipOverwrite != null) {
            configurator.setSkipOverwrite(skipOverwrite);
        }

        configurator.setLang(language);

        configurator.setOutputDir(output.getAbsolutePath());

        if(isNotEmpty(auth)) {
            configurator.setAuth(auth);
        }

        if(isNotEmpty(apiPackage)) {
            configurator.setApiPackage(apiPackage);
        }

        if(isNotEmpty(modelPackage)) {
            configurator.setModelPackage(modelPackage);
        }

        if(isNotEmpty(invokerPackage)) {
            configurator.setInvokerPackage(invokerPackage);
        }

        if(isNotEmpty(groupId)) {
            configurator.setGroupId(groupId);
        }

        if(isNotEmpty(artifactId)) {
            configurator.setArtifactId(artifactId);
        }

        if(isNotEmpty(artifactVersion)) {
            configurator.setArtifactVersion(artifactVersion);
        }

        if(isNotEmpty(library)) {
            configurator.setLibrary(library);
        }

        if (null != templateDirectory) {
            configurator.setTemplateDir(templateDirectory.getAbsolutePath());
        }

        if (configOptions != null) {

            if(configOptions.containsKey("instantiation-types")) {
                applyInstantiationTypesKvp(configOptions.get("instantiation-types").toString(), configurator);
            }

            if(configOptions.containsKey("import-mappings")) {
                applyImportMappingsKvp(configOptions.get("import-mappings").toString(), configurator);
            }

            if(configOptions.containsKey("type-mappings")) {
                applyTypeMappingsKvp(configOptions.get("type-mappings").toString(), configurator);
            }

            if(configOptions.containsKey("language-specific-primitives")) {
                applyLanguageSpecificPrimitivesCsv(configOptions.get("language-specific-primitives").toString(), configurator);
            }

            if(configOptions.containsKey("additional-properties")) {
                applyAdditionalPropertiesKvp(configOptions.get("additional-properties").toString(), configurator);
            }
        }

        if (environmentVariables != null) {

            for(String key : environmentVariables.keySet()) {
                String value = environmentVariables.get(key);
                if(value == null) {
                    // don't put null values
                    value = "";
                }
                System.setProperty(key, value);
                configurator.addSystemProperty(key, value);
            }
        }
        
        final List<File> htmlDocsToMerge = generateForSpecs(configurator, specs);
        
        try {
        	File outputFile = new File(output.getAbsolutePath() + File.separator + destinationFile);
        	
        	if(htmlDocsToMerge.size() == 1) {
        		FileUtils.copyFile(htmlDocsToMerge.get(0), outputFile);
        	} else {
        		Document doc = mergeHtmlFiles(htmlDocsToMerge);
    			writeToFile(outputFile, doc.html());
        	}
        	
		} catch (Exception e) {
			getLog().error(e); 
            throw new MojoExecutionException("Merging of generated files failed. See above for the full exception.");
		}
        
        if (addCompileSourceRoot) {
            project.addCompileSourceRoot(output.toString());
        }
    }
    
    /**
     * Generate files for provided configuration and set of swagger specs
     * @param configurator - configuration
     * @param specs - swagger specifications (comma separated list)
     * @return - list of generated documents except of additional files like LICENCE, etc.
     * @throws MojoExecutionException
     */
    protected List<File> generateForSpecs(CodegenConfigurator configurator, String specs) throws MojoExecutionException {
    	List<File> files = new ArrayList<File>();
        List<String> specList = Arrays.asList(specs.split("\\s*,\\s*"));

    	int i = 0;
        for(String spec : specList) {
        	
        	final String generatedFileName = destinationFile + "." + ++i;
        	
        	for(File f : generate(configurator, spec, generatedFileName)) {
        		if(f.getName().equals(generatedFileName)) {
        			files.add(f);
        		}
        	}
        }    	
        
    	return files;
    }
    
    /**
     * Generates artifacts for swagger spec
     * @param configurator - configuration
     * @param spec - swagger specification json/yaml
     * @param generatedFileName - name of the generated file
     * @return - list of generated artifacts
     * @throws MojoExecutionException
     */
	protected List<File> generate(CodegenConfigurator configurator, String spec, String generatedFileName) throws MojoExecutionException {
    	
    	final ClientOptInput input = configurator.setInputSpec(spec).toClientOptInput();
        final CodegenConfig config = input.getConfig();
        config.supportingFiles().add(
        		new SupportingFile(
        				System.getProperty("com.exigen.ipb.base.swagger.template.filename", "swagger.mustache"),
        				generatedFileName
        		));
        
        try {
            return new DefaultGenerator().opts(input).generate();
        } catch (Exception e) {
            // Maven logs exceptions thrown by plugins only if invoked with -e
            // I find it annoying to jump through hoops to get basic diagnostic information,
            // so let's log it in any case:
            getLog().error(e); 
            throw new MojoExecutionException("Code generation failed. See above for the full exception.");
        }
    }
	
	/**
	 * Merge provided list of html files to single file  
	 * @param filesToMerge - list of html file to merge
	 * @return - merged file content
	 * @throws IOException
	 */
	protected Document mergeHtmlFiles(List<File> filesToMerge) throws IOException {
		
		Document doc = Jsoup.parse("<div id=\"contentBox\">");
		
		for(File f : filesToMerge) {
			Document document = Jsoup.parse(f, FILE_ENCODING);
			
			Element table = document.select("table#content").first();
			if(table != null) {
				if(doc.select("table#content").isEmpty()) {
					doc.select("div").first().appendChild(table);
				} else {
					Elements trs = table.select("tr").first().siblingElements();
					for(Element tr : trs) {
						doc.select("table#content tr").last().after(tr);
					}
				}
			}
			
			Elements ops = document.select("div#operation");
			if(ops != null) {
				for(Element op : ops) {
					doc.select("div").first().children().last().after(op);
				}
			}
		}
		
		return doc;
	}
	
	/**
	 * Write to file with provided content
	 * @param output - output file
	 * @param contents - file content
	 * @throws IOException
	 */
	protected void writeToFile(File output, String contents) throws IOException {

        if (output.getParent() != null && !new File(output.getParent()).exists()) {
            File parent = new File(output.getParent());
            parent.mkdirs();
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(output), FILE_ENCODING));

        out.write(contents);
        out.close();
    }

}
