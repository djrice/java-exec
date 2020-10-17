//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class PipeTest {

  @Test
  public void runsPipeRunnable() throws Exception {
    TestPipeRunnable runnable = new TestPipeRunnable();
    Pipe pipe = new Pipe(runnable);
    pipe.start();
    pipe.join();
    runnable.assertRunExecuted();
  }

  @Test
  public void capturesIOException() throws Exception {
    final IOException expectedException = new IOException("Fake IO Exception!");
    PipeRunnable runnable = () -> {
      throw expectedException;
    };
    Pipe pipe = new Pipe(runnable);
    pipe.start();
    pipe.join();

    assertThat(pipe.isException(), equalTo(true));
    assertThat(pipe.exception(), equalTo(expectedException));
  }

  static class TestPipeRunnable implements PipeRunnable {

    private boolean runExecuted = false;

    public void run() {
      runExecuted = true;
    }

    public void assertRunExecuted() {
      if (!runExecuted) {
        fail("run() did not execute!");
      }
    }
  }
}
