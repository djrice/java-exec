//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ProcessWatcherTest {

  @Test
  public void destroysProcessIfExecutionExceedsTimeout() throws Exception {
    String[] cmd = TestProgram.cmdFor(new String[]{"sleep", "20000"});
    Process process = Runtime.getRuntime().exec(cmd);
    ProcessWatcher watcher = new ProcessWatcher(process, 100L);
    watcher.start();
    int returnCode = process.waitFor();

    assertThat(returnCode, not(equalTo(0)));
    assertThat(watcher.timedOut(), equalTo(true));
  }

  @Test
  public void watcherCanBeCanceled() throws Exception {
    String[] cmd = TestProgram.cmdFor(new String[]{"sleep", "200"});
    Process process = Runtime.getRuntime().exec(cmd);
    ProcessWatcher watcher = new ProcessWatcher(process, 50L);
    watcher.start();
    watcher.cancel();
    int returnCode = process.waitFor();

    assertThat(returnCode, equalTo(0));
  }
}
