//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamPipeRunnable implements PipeRunnable {

  private OutputStream out;
  private InputStream in;

  public StreamPipeRunnable(InputStream in, OutputStream out) {
    this.out = out;
    this.in = in;
  }

  public void run() throws IOException {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = in.read(buffer)) > -1) {
      out.write(buffer, 0, bytesRead);
    }
  }

}
