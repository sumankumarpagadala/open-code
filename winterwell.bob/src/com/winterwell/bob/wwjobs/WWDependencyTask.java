package com.winterwell.bob.wwjobs;

import java.io.File;

import com.winterwell.bob.tasks.ForkJVMTask;
import com.winterwell.utils.io.FileUtils;
import com.winterwell.utils.log.Log;
import com.winterwell.utils.time.TUnit;
import com.winterwell.web.FakeBrowser;

/**
 * Hack for BuildWWProject, which handles project-not-on-classpath
 * by downloading the jar 
 * 
 * FIXME this won't download the dependencies though :(
 * 
 * @author daniel
 * @testedby {@link WWDependencyTaskTest}
 */
public class WWDependencyTask extends BuildWinterwellProject {

	private String builderClass;

	/**
	 * 
	 * @param projectName
	 * @param builderClass fully-qualified class name. Can be null
	 */
	public WWDependencyTask(String projectName, String builderClass) {
		super(projectName);
		this.builderClass = builderClass;
		// dependencies shouldnt need rebuilding all the time
		setSkipGap(TUnit.DAY.dt);
	}

	@Override
	public void doTask() throws Exception {
		// Do NOT run the super
		
		// build?
		if (builderClass!=null) {			
			try {
				Class<?> clazz = Class.forName(builderClass);
				BuildWinterwellProject builder = (BuildWinterwellProject) clazz.newInstance();
				// copy settings: the ones that might get overridden by the caller.
				// Don't copy all -- 'cos BuildX may have done some setup in its constructor.
				builder.setCompile(isCompile());
				builder.setErrorHandler(errorHandler);
				builder.setIncSrc(incSrc);
				builder.setMaxTime(maxTime);
				builder.setScpToWW(scpToWW);
				builder.setSkipDependencies(isSkipDependencies());
				builder.setVerbosity(getVerbosity());
				
				// run!
				builder.run();
				setJar(builder.getJar());
				
				// success :)
				return;
			} catch(Throwable ex) {
				Log.w(LOGTAG, "Cannot run local BuildTask "+builderClass);
			}			
		}
		
		// download jar
		String url = "https://www.winterwell.com/software/downloads/"+projectName+".jar";
		Log.i(LOGTAG, "Downloading jar "+url);
		FakeBrowser fb = new FakeBrowser();
		File jar = fb.getFile(url);
//		String remoteJar = "/home/winterwell/public-software/"+projectName+".jar";
//		SCPTask scp = new SCPTask("winterwell@winterwell.com", remoteJar, getJar());
//		// this is online at: https://www.winterwell.com/software/downloads
//		scp.setMkdirTask(false);
//		scp.runInThread();
		
		getJar().getParentFile().mkdirs();
		FileUtils.move(jar, getJar());
		Log.i(LOGTAG, "Downloaded jar "+getJar());
		
		// run build for that project
		if (builderClass!=null) {			
			try {
				ForkJVMTask forked = new ForkJVMTask(builderClass);
				forked.setDir(projectDir);
				forked.getClasspath().add(jar);
				forked.run();
			} catch (Throwable ex) {
				Log.w(LOGTAG, "Recursive build of "+builderClass+" failed: "+ex);
				// NB: fork includes a finally: close block
			}
		}
	}

}
