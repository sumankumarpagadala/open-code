package com.winterwell.bob.tasks;

import org.junit.Test;

import com.winterwell.bob.TestTask;

public class ForkJVMTaskTest {

	@Test
	public void testDoTask() throws Exception {
		ForkJVMTask fork = new ForkJVMTask(TestTask.class);
		fork.setClasspath(new Classpath("bob-all.jar:bin.test"));
		fork.doTask();
	}

}
