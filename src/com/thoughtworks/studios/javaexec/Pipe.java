//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import java.io.IOException;

public class Pipe {

  private IOException exception;
  private Thread thread;
  private PipeRunnable job;

  public Pipe(PipeRunnable job) {
    this.job = job;
  }

  public void start() {
    thread = new Thread(new Runnable() {
      public void run() {
        try {
          job.run();
        } catch (IOException ex) {
          exception = ex;
        }
      }
    });
    thread.start();
  }

  public void join() throws InterruptedException {
    thread.join();
  }

  public boolean isException() {
    return exception != null;
  }

  public IOException exception() {
    return exception;
  }

}
