//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TestLineHandler implements LineHandler {
  private final List<String> lines = new ArrayList<>();

  public void handleLine(String line) {
    lines.add(line);
  }

  public void assertLinesEqual(List<String> expectedLines) {
    assertThat(lines, equalTo(expectedLines));
  }
}