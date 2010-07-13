//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class StreamPipeRunnableTest {

  @Test
  public void pipeWorks() throws Exception {
    String content = "expected text";
    ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PipeRunnable pipeRunnable = new StreamPipeRunnable(in, out);
    pipeRunnable.run();
    assertThat(out.toString(), equalTo(content));
  }
}
