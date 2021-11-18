package org.liquibase.maven.plugins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Display help information on liquibase-maven-plugin.<br/> Call <pre>  mvn liquibase:help -Ddetail=true -Dgoal=&lt;goal-name&gt;</pre> to display parameter details.
 *
 * @version generated on Fri Feb 16 21:58:56 CST 2018
 * @author org.apache.maven.tools.plugin.generator.PluginHelpGenerator (version 2.6)
 * @goal help
 * @requiresProject false
 */
public class HelpMojo
    extends AbstractMojo
{
    /**
     * If <code>true</code>, display all settable properties for each goal.
     * 
     * @parameter expression="${detail}" default-value="false"
     */
    private boolean detail;

    /**
     * The name of the goal for which to show help. If unspecified, all goals will be displayed.
     * 
     * @parameter expression="${goal}"
     */
    private java.lang.String goal;

    /**
     * The maximum length of a display line, should be positive.
     * 
     * @parameter expression="${lineLength}" default-value="80"
     */
    private int lineLength;

    /**
     * The number of spaces per indentation level, should be positive.
     * 
     * @parameter expression="${indentSize}" default-value="2"
     */
    private int indentSize;


    /** {@inheritDoc} */
    public void execute()
        throws MojoExecutionException
    {
        if ( lineLength <= 0 )
        {
            getLog().warn( "The parameter 'lineLength' should be positive, using '80' as default." );
            lineLength = 80;
        }
        if ( indentSize <= 0 )
        {
            getLog().warn( "The parameter 'indentSize' should be positive, using '2' as default." );
            indentSize = 2;
        }

        StringBuffer sb = new StringBuffer();

        append( sb, "org.liquibase:liquibase-maven-plugin:3.5.5", 0 );
        append( sb, "", 0 );

        append( sb, "Liquibase Maven Plugin", 0 );
        append( sb, "A Maven plugin wraps up some of the functionality of Liquibase", 1 );
        append( sb, "", 0 );

        if ( goal == null || goal.length() <= 0 )
        {
            append( sb, "This plugin has 20 goals:", 0 );
            append( sb, "", 0 );
        }

        if ( goal == null || goal.length() <= 0 || "changelogSync".equals( goal ) )
        {
            append( sb, "liquibase:changelogSync", 0 );
            append( sb, "Marks all unapplied changes to the database as applied in the change log.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "changelogSyncSQL".equals( goal ) )
        {
            append( sb, "liquibase:changelogSyncSQL", 0 );
            append( sb, "Generates SQL that marks all unapplied changes as applied.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "migrationSqlOutputFile (Default: ${project.build.directory}/liquibase/migrate.sql)", 2 );
                append( sb, "The file to output the Migration SQL script to, if it exists it will be overwritten.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "clearCheckSums".equals( goal ) )
        {
            append( sb, "liquibase:clearCheckSums", 0 );
            append( sb, "Clears all checksums in the current changelog, so they will be recalculated next update.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "dbDoc".equals( goal ) )
        {
            append( sb, "liquibase:dbDoc", 0 );
            append( sb, "Generates dbDocs against the database.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDirectory (Default: ${project.build.directory}/liquibase/dbDoc)", 2 );
                append( sb, "(no description available)", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "diff".equals( goal ) )
        {
            append( sb, "liquibase:diff", 0 );
            append( sb, "Generates a diff between the specified database and the reference database. The output is either a report or a changelog depending on the value of the diffChangeLogFile parameter.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "diffChangeLogFile", 2 );
                append( sb, "If this parameter is set, the changelog needed to \'fix\' differences between the two databases is output. If the file exists, it is appended to. If this is null, a comparison report is output to stdout.", 3 );
                append( sb, "", 0 );

                append( sb, "diffExcludeObjects", 2 );
                append( sb, "Objects to be excluded from the changelog. Example filters: \'table_name\', \'table:main_.*\', \'column:*._lock, table:primary.*\'.", 3 );
                append( sb, "", 0 );

                append( sb, "diffIncludeCatalog", 2 );
                append( sb, "Include the catalog in the diff output? If this is null then the catalog will not be included", 3 );
                append( sb, "", 0 );

                append( sb, "diffIncludeObjects", 2 );
                append( sb, "Objects to be included in the changelog. Example filters: \'table_name\', \'table:main_.*\', \'column:*._lock, table:primary.*\'.", 3 );
                append( sb, "", 0 );

                append( sb, "diffIncludeSchema", 2 );
                append( sb, "Include the schema in the diff output? If this is null then the schema will not be included", 3 );
                append( sb, "", 0 );

                append( sb, "diffIncludeTablespace", 2 );
                append( sb, "Include the tablespace in the diff output? If this is null then the tablespace will not be included", 3 );
                append( sb, "", 0 );

                append( sb, "diffTypes", 2 );
                append( sb, "List of diff types to include in Change Log expressed as a comma separated list from: tables, views, columns, indexes, foreignkeys, primarykeys, uniqueconstraints, data. If this is null then the default types will be: tables, views, columns, indexes, foreignkeys, primarykeys, uniqueconstraints", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "referenceDefaultCatalogName", 2 );
                append( sb, "The reference database catalog.", 3 );
                append( sb, "", 0 );

                append( sb, "referenceDefaultSchemaName", 2 );
                append( sb, "The reference database schema.", 3 );
                append( sb, "", 0 );

                append( sb, "referenceDriver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the reference database. If this is not specified, then the driver will be used instead.", 3 );
                append( sb, "", 0 );

                append( sb, "referencePassword", 2 );
                append( sb, "The reference database password to use to connect to the specified database. If this is null then an empty password will be used.", 3 );
                append( sb, "", 0 );

                append( sb, "referenceServer", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "referenceUrl", 2 );
                append( sb, "The reference database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "referenceUsername", 2 );
                append( sb, "The reference database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "dropAll".equals( goal ) )
        {
            append( sb, "liquibase:dropAll", 0 );
            append( sb, "Drops all database objects in the configured schema(s). Note that functions, procedures and packages are not dropped.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "schemas", 2 );
                append( sb, "The schemas to be dropped. Comma separated list.", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "futureRollbackSQL".equals( goal ) )
        {
            append( sb, "liquibase:futureRollbackSQL", 0 );
            append( sb, "Generates the SQL that is required to rollback the database to current state after the next update.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFile (Default: ${project.build.directory}/liquibase/migrate.sql)", 2 );
                append( sb, "The file to output the Rollback SQL script to, if it exists it will be overwritten.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackCount (Default: -1)", 2 );
                append( sb, "The number of change sets to rollback.", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackDate", 2 );
                append( sb, "The date to rollback the database to. The format of the date must match either an ISO date format, or that of the DateFormat.getDateInstance() for the platform the plugin is executing on.", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackTag", 2 );
                append( sb, "The tag to roll the database back to.", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "generateChangeLog".equals( goal ) )
        {
            append( sb, "liquibase:generateChangeLog", 0 );
            append( sb, "Generates SQL that marks all unapplied changes as applied.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeSetAuthor", 2 );
                append( sb, "The author to be specified for Change Sets in the generated Change Log.", 3 );
                append( sb, "", 0 );

                append( sb, "changeSetContext", 2 );
                append( sb, "The execution context to be used for Change Sets in the generated Change Log, which can be \',\' separated if multiple contexts.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "dataDir", 2 );
                append( sb, "Directory where insert statement csv files will be kept.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "diffExcludeObjects", 2 );
                append( sb, "Objects to be excluded from the changelog. Example filters: \'table_name\', \'table:main_.*\', \'column:*._lock, table:primary.*\'.", 3 );
                append( sb, "", 0 );

                append( sb, "diffIncludeObjects", 2 );
                append( sb, "Objects to be included in the changelog. Example filters: \'table_name\', \'table:main_.*\', \'column:*._lock, table:primary.*\'.", 3 );
                append( sb, "", 0 );

                append( sb, "diffTypes", 2 );
                append( sb, "List of diff types to include in Change Log expressed as a comma separated list from: tables, views, columns, indexes, foreignkeys, primarykeys, uniqueconstraints, data. If this is null then the default types will be: tables, views, columns, indexes, foreignkeys, primarykeys, uniqueconstraints", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputChangeLogFile", 2 );
                append( sb, "The target change log file to output to. If this is null then the output will be to the screen.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "help".equals( goal ) )
        {
            append( sb, "liquibase:help", 0 );
            append( sb, "Display help information on liquibase-maven-plugin.\nCall\n\u00a0\u00a0mvn\u00a0liquibase:help\u00a0-Ddetail=true\u00a0-Dgoal=<goal-name>\nto display parameter details.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "detail (Default: false)", 2 );
                append( sb, "If true, display all settable properties for each goal.", 3 );
                append( sb, "", 0 );

                append( sb, "goal", 2 );
                append( sb, "The name of the goal for which to show help. If unspecified, all goals will be displayed.", 3 );
                append( sb, "", 0 );

                append( sb, "indentSize (Default: 2)", 2 );
                append( sb, "The number of spaces per indentation level, should be positive.", 3 );
                append( sb, "", 0 );

                append( sb, "lineLength (Default: 80)", 2 );
                append( sb, "The maximum length of a display line, should be positive.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "listLocks".equals( goal ) )
        {
            append( sb, "liquibase:listLocks", 0 );
            append( sb, "Lists all Liquibase updater locks on the current database.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "migrate".equals( goal ) )
        {
            append( sb, "liquibase:migrate", 0 );
            append( sb, "Deprecated. Use the LiquibaseUpdate class or Maven goal \'update\' instead.", 1 );
            if ( detail )
            {
                append( sb, "", 0 );
                append( sb, "Liquibase Migration Maven plugin. This plugin allows for DatabaseChangeLogs to be applied to a database as part of a Maven build process.", 1 );
            }
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changesToApply (Default: 0)", 2 );
                append( sb, "The number of changes to apply to the database. By default this value is 0, which will result in all changes (not already applied to the database) being applied.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "toTag", 2 );
                append( sb, "Update to the changeSet with the given tag command.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "migrateSQL".equals( goal ) )
        {
            append( sb, "liquibase:migrateSQL", 0 );
            append( sb, "Deprecated. Use LiquibaseUpdateSQL or Maven goal \'updateSQL\' instead.", 1 );
            if ( detail )
            {
                append( sb, "", 0 );
                append( sb, "Creates an SQL migration script using the provided DatabaseChangeLog(s) comparing what already exists in the database to what is defined in the DataBaseChangeLog(s).", 1 );
            }
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changesToApply (Default: 0)", 2 );
                append( sb, "The number of changes to apply to the database. By default this value is 0, which will result in all changes (not already applied to the database) being applied.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "migrationSqlOutputFile (Default: ${project.build.directory}/liquibase/migrate.sql)", 2 );
                append( sb, "The file to output the Migration SQL script to, if it exists it will be overwritten.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "toTag", 2 );
                append( sb, "Update to the changeSet with the given tag command.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "releaseLocks".equals( goal ) )
        {
            append( sb, "liquibase:releaseLocks", 0 );
            append( sb, "Removes any Liquibase updater locks from the current database.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "rollback".equals( goal ) )
        {
            append( sb, "liquibase:rollback", 0 );
            append( sb, "Invokes Liquibase rollbacks on a database.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackCount (Default: -1)", 2 );
                append( sb, "The number of change sets to rollback.", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackDate", 2 );
                append( sb, "The date to rollback the database to. The format of the date must match either an ISO date format, or that of the DateFormat.getDateInstance() for the platform the plugin is executing on.", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackTag", 2 );
                append( sb, "The tag to roll the database back to.", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "rollbackSQL".equals( goal ) )
        {
            append( sb, "liquibase:rollbackSQL", 0 );
            append( sb, "Generates the SQL that is required to rollback the database to the specified pointing attributes \'rollbackCount\', \'rollbackTag\'", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "migrationSqlOutputFile (Default: ${project.build.directory}/liquibase/migrate.sql)", 2 );
                append( sb, "The file to output the Rollback SQL script to, if it exists it will be overwritten.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackCount (Default: -1)", 2 );
                append( sb, "The number of change sets to rollback.", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackDate", 2 );
                append( sb, "The date to rollback the database to. The format of the date must match either an ISO date format, or that of the DateFormat.getDateInstance() for the platform the plugin is executing on.", 3 );
                append( sb, "", 0 );

                append( sb, "rollbackTag", 2 );
                append( sb, "The tag to roll the database back to.", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "status".equals( goal ) )
        {
            append( sb, "liquibase:status", 0 );
            append( sb, "Prints which changesets need to be applied to the database.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "tag".equals( goal ) )
        {
            append( sb, "liquibase:tag", 0 );
            append( sb, "Writes a Liquibase tag to the database.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "tag", 2 );
                append( sb, "(no description available)", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "update".equals( goal ) )
        {
            append( sb, "liquibase:update", 0 );
            append( sb, "Applies the DatabaseChangeLogs to the database. Useful as part of the build process.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changesToApply (Default: 0)", 2 );
                append( sb, "The number of changes to apply to the database. By default this value is 0, which will result in all changes (not already applied to the database) being applied.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "dropFirst (Default: false)", 2 );
                append( sb, "Whether or not to perform a drop on the database before executing the change.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "toTag", 2 );
                append( sb, "Update to the changeSet with the given tag command.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "updateSQL".equals( goal ) )
        {
            append( sb, "liquibase:updateSQL", 0 );
            append( sb, "Generates the SQL that is required to update the database to the current version as specified in the DatabaseChangeLogs.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changesToApply (Default: 0)", 2 );
                append( sb, "The number of changes to apply to the database. By default this value is 0, which will result in all changes (not already applied to the database) being applied.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "migrationSqlOutputFile (Default: ${project.build.directory}/liquibase/migrate.sql)", 2 );
                append( sb, "The file to output the Migration SQL script to, if it exists it will be overwritten.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "toTag", 2 );
                append( sb, "Update to the changeSet with the given tag command.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( goal == null || goal.length() <= 0 || "updateTestingRollback".equals( goal ) )
        {
            append( sb, "liquibase:updateTestingRollback", 0 );
            append( sb, "Applies the DatabaseChangeLogs to the database, testing rollback. This is done by updating the database, rolling it back then updating it again.", 1 );
            append( sb, "", 0 );
            if ( detail )
            {
                append( sb, "Available parameters:", 1 );
                append( sb, "", 0 );

                append( sb, "changelogCatalogName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changeLogFile", 2 );
                append( sb, "Specifies the change log file to use for Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "changelogSchemaName", 2 );
                append( sb, "Schema against which Liquibase changelog tables will be created.", 3 );
                append( sb, "", 0 );

                append( sb, "changesToApply (Default: 0)", 2 );
                append( sb, "The number of changes to apply to the database. By default this value is 0, which will result in all changes (not already applied to the database) being applied.", 3 );
                append( sb, "", 0 );

                append( sb, "clearCheckSums (Default: false)", 2 );
                append( sb, "Flag for forcing the checksums to be cleared from teh DatabaseChangeLog table.", 3 );
                append( sb, "", 0 );

                append( sb, "contexts", 2 );
                append( sb, "The Liquibase contexts to execute, which can be \',\' separated if multiple contexts are required. If no context is specified then ALL contexts will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogLockTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseChangeLogTableName", 2 );
                append( sb, "Table name to use for the databasechangelog.", 3 );
                append( sb, "", 0 );

                append( sb, "databaseClass", 2 );
                append( sb, "The class to use as the database object.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultCatalogName", 2 );
                append( sb, "The default catalog name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "defaultSchemaName", 2 );
                append( sb, "The default schema name to use the for database connection.", 3 );
                append( sb, "", 0 );

                append( sb, "driver", 2 );
                append( sb, "The fully qualified name of the driver class to use to connect to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "driverPropertiesFile", 2 );
                append( sb, "Location of a properties file containing JDBC connection properties for use by the driver.", 3 );
                append( sb, "", 0 );

                append( sb, "emptyPassword (Default: false)", 2 );
                append( sb, "Deprecated. Use an empty or null value for the password instead.", 3 );
                append( sb, "", 0 );
                append( sb, "Use an empty string as the password for the database connection. This should not be used along side the password setting.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVariables", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "expressionVars", 2 );
                append( sb, "Array to put a expression variable to maven plugin.", 3 );
                append( sb, "", 0 );

                append( sb, "includeArtifact (Default: true)", 2 );
                append( sb, "Allows for the maven project artifact to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "includeTestOutputDirectory (Default: true)", 2 );
                append( sb, "Allows for the maven test output directory to be included in the class loader for obtaining the Liquibase property and DatabaseChangeLog files.", 3 );
                append( sb, "", 0 );

                append( sb, "labels", 2 );
                append( sb, "The Liquibase labels to execute, which can be \',\' separated if multiple labels are required or a more complex expression. If no label is specified then ALL all will be executed.", 3 );
                append( sb, "", 0 );

                append( sb, "logging (Default: INFO)", 2 );
                append( sb, "Controls the level of logging from Liquibase when executing. The value can be \'debug\', \'info\', \'warning\', \'severe\', or \'off\'. The value is case insensitive.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultCatalog", 2 );
                append( sb, "Whether to ignore the catalog/database name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputDefaultSchema", 2 );
                append( sb, "Whether to ignore the schema name.", 3 );
                append( sb, "", 0 );

                append( sb, "outputFileEncoding", 2 );
                append( sb, "Flag to set the character encoding of the output file produced by Liquibase during the updateSQL phase.", 3 );
                append( sb, "", 0 );

                append( sb, "password", 2 );
                append( sb, "The database password to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "promptOnNonLocalDatabase (Default: true)", 2 );
                append( sb, "Controls the prompting of users as to whether or not they really want to run the changes on a database that is not local to the machine that the user is current executing the plugin on.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFile", 2 );
                append( sb, "The Liquibase properties file used to configure the Liquibase Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyFileWillOverride (Default: false)", 2 );
                append( sb, "Flag allowing for the Liquibase properties file to override any settings provided in the Maven plugin configuration. By default if a property is explicity specified it is not overridden if it also appears in the properties file.", 3 );
                append( sb, "", 0 );

                append( sb, "propertyProviderClass", 2 );
                append( sb, "The class to use as the property provider (must be a java.util.Properties implementation).", 3 );
                append( sb, "", 0 );

                append( sb, "server", 2 );
                append( sb, "The server id in settings.xml to use when authenticating with.", 3 );
                append( sb, "", 0 );

                append( sb, "skip", 2 );
                append( sb, "Set this to \'false\' to skip running liquibase. Its use is NOT RECOMMENDED, but quite convenient on occasion.", 3 );
                append( sb, "", 0 );

                append( sb, "systemProperties", 2 );
                append( sb, "List of system properties to pass to the database.", 3 );
                append( sb, "", 0 );

                append( sb, "toTag", 2 );
                append( sb, "Update to the changeSet with the given tag command.", 3 );
                append( sb, "", 0 );

                append( sb, "url", 2 );
                append( sb, "The Database URL to connect to for executing Liquibase.", 3 );
                append( sb, "", 0 );

                append( sb, "username", 2 );
                append( sb, "The database username to use to connect to the specified database.", 3 );
                append( sb, "", 0 );

                append( sb, "verbose (Default: false)", 2 );
                append( sb, "Controls the verbosity of the output from invoking the plugin.", 3 );
                append( sb, "", 0 );
            }
        }

        if ( getLog().isInfoEnabled() )
        {
            getLog().info( sb.toString() );
        }
    }

    /**
     * <p>Repeat a String <code>n</code> times to form a new string.</p>
     *
     * @param str String to repeat
     * @param repeat number of times to repeat str
     * @return String with repeated String
     * @throws NegativeArraySizeException if <code>repeat < 0</code>
     * @throws NullPointerException if str is <code>null</code>
     */
    private static String repeat( String str, int repeat )
    {
        StringBuffer buffer = new StringBuffer( repeat * str.length() );

        for ( int i = 0; i < repeat; i++ )
        {
            buffer.append( str );
        }

        return buffer.toString();
    }

    /** 
     * Append a description to the buffer by respecting the indentSize and lineLength parameters.
     * <b>Note</b>: The last character is always a new line.
     * 
     * @param sb The buffer to append the description, not <code>null</code>.
     * @param description The description, not <code>null</code>.
     * @param indent The base indentation level of each line, must not be negative.
     */
    private void append( StringBuffer sb, String description, int indent )
    {
        for ( Iterator it = toLines( description, indent, indentSize, lineLength ).iterator(); it.hasNext(); )
        {
            sb.append( it.next().toString() ).append( '\n' );
        }
    }

    /** 
     * Splits the specified text into lines of convenient display length.
     * 
     * @param text The text to split into lines, must not be <code>null</code>.
     * @param indent The base indentation level of each line, must not be negative.
     * @param indentSize The size of each indentation, must not be negative.
     * @param lineLength The length of the line, must not be negative.
     * @return The sequence of display lines, never <code>null</code>.
     * @throws NegativeArraySizeException if <code>indent < 0</code>
     */
    private static List toLines( String text, int indent, int indentSize, int lineLength )
    {
        List lines = new ArrayList();

        String ind = repeat( "\t", indent );
        String[] plainLines = text.split( "(\r\n)|(\r)|(\n)" );
        for ( int i = 0; i < plainLines.length; i++ )
        {
            toLines( lines, ind + plainLines[i], indentSize, lineLength );
        }

        return lines;
    }

    /** 
     * Adds the specified line to the output sequence, performing line wrapping if necessary.
     * 
     * @param lines The sequence of display lines, must not be <code>null</code>.
     * @param line The line to add, must not be <code>null</code>.
     * @param indentSize The size of each indentation, must not be negative.
     * @param lineLength The length of the line, must not be negative.
     */
    private static void toLines( List lines, String line, int indentSize, int lineLength )
    {
        int lineIndent = getIndentLevel( line );
        StringBuffer buf = new StringBuffer( 256 );
        String[] tokens = line.split( " +" );
        for ( int i = 0; i < tokens.length; i++ )
        {
            String token = tokens[i];
            if ( i > 0 )
            {
                if ( buf.length() + token.length() >= lineLength )
                {
                    lines.add( buf.toString() );
                    buf.setLength( 0 );
                    buf.append( repeat( " ", lineIndent * indentSize ) );
                }
                else
                {
                    buf.append( ' ' );
                }
            }
            for ( int j = 0; j < token.length(); j++ )
            {
                char c = token.charAt( j );
                if ( c == '\t' )
                {
                    buf.append( repeat( " ", indentSize - buf.length() % indentSize ) );
                }
                else if ( c == '\u00A0' )
                {
                    buf.append( ' ' );
                }
                else
                {
                    buf.append( c );
                }
            }
        }
        lines.add( buf.toString() );
    }

    /** 
     * Gets the indentation level of the specified line.
     * 
     * @param line The line whose indentation level should be retrieved, must not be <code>null</code>.
     * @return The indentation level of the line.
     */
    private static int getIndentLevel( String line )
    {
        int level = 0;
        for ( int i = 0; i < line.length() && line.charAt( i ) == '\t'; i++ )
        {
            level++;
        }
        for ( int i = level + 1; i <= level + 4 && i < line.length(); i++ )
        {
            if ( line.charAt( i ) == '\t' )
            {
                level++;
                break;
            }
        }
        return level;
    }
}
