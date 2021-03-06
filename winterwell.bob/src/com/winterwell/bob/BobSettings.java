package com.winterwell.bob;

import java.io.File;

import com.winterwell.utils.Key;
import com.winterwell.utils.Printer;
import com.winterwell.utils.containers.Containers;
import com.winterwell.utils.io.Option;

/**
 * The settings for {@link Bob}. These can be set via command line arguments
 * passed to {@link Bob#main(String[])}, or by using
 * {@link Bob#setSettings(BobSettings)}.
 * 
 * @author Daniel
 */
public class BobSettings {

	@Option(tokens="-update", description="Download a fresh copy of Bob itself")
	public boolean update;
	

	@Option(description="TODO sniff the script .java file for Bob settings in javadoc, e.g. @Bob -classpath lib/foo.jar")
	public boolean sniff = true;
	
	@Override
	public String toString() {
		return "BobSettings"+Printer.toString(Containers.objectAsMap(this));
	}

	public static final Key<Boolean> VERBOSE = new Key<Boolean>("verbose");

	@Option(tokens="-cp,-classpath", description="Classpath used for dynamically compiling build scripts. Uses the file1:file2 format of Java")
	// NB: This is not the classpath used for CompileTasks which are part of a build script run.
	public String classpath;
	
	@Option(tokens = "-ignore", description = "Ignore all exceptions")
	public boolean ignoreAllExceptions;

	@Option(tokens = "-logdir", description = "Directory to write log files to")
	public File logDir = new File("boblog");

	@Option(tokens = "-nolog", description = "Switch off logging")
	public boolean loggingOff;

	@Option(tokens = "-q,-quiet")
	public boolean quiet;

	@Option(tokens = "-noskip,-clean", description = "Switch off smart dependency skipping (if on, avoids repeatedly running sub-tasks)")
	public boolean skippingOff;

	// @Option(tokens = "-p,-properties", description =
	// "Java properties file to load")
	// public File properties = new File("bob.properties");

	@Option(tokens = "-v,-verbose")
	public boolean verbose;

}
