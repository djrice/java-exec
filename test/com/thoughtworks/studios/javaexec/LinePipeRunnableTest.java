//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class LinePipeRunnableTest {

  @Test
  public void pipeWorks() throws Exception {
    String content = "expected\ntext\n";
    ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());

    TestLineHandler testLineHandler = new TestLineHandler();
    PipeRunnable pipeRunnable = new LinePipeRunnable(in, testLineHandler);
    pipeRunnable.run();
    String[] expectedLines = new String[]{"expected", "text"};
    testLineHandler.assertLinesEqual(Arrays.asList(expectedLines));
  }
}
