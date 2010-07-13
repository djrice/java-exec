//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class CommandExecutor {

  private List<String> cmd;
  private String standardErrorText;
  private int returnCode;
  private Long timeout;

  public CommandExecutor(List<String> cmd) {
    this(cmd, null);
  }

  public CommandExecutor(List<String> cmd, Long timeout) {
    this.cmd = cmd;
    this.timeout = timeout;
  }

  public void run(OutputStream outputStream) {
    try {
      Process process = new ProcessBuilder(cmd).start();
      PipeRunnable pipeRunnable = new StreamPipeRunnable(process.getInputStream(), outputStream);
      Pipe pipe = new Pipe(pipeRunnable);
      captureOutput(process, pipe);
    } catch (IOException ioEx) {
      throw new CommandExecutorException("Command execution failed unexpectedly!", ioEx);
    }
  }


  public void run(LineHandler out) {
    try {
      Process process = new ProcessBuilder(cmd).start();
      PipeRunnable pipeRunnable = new LinePipeRunnable(process.getInputStream(), out);
      Pipe pipe = new Pipe(pipeRunnable);
      captureOutput(process, pipe);
    } catch (IOException ioEx) {
      throw new CommandExecutorException("Command execution failed unexpectedly!", ioEx);
    }
  }

  public String run() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    run(out);
    try {
      return out.toString("UTF-8");
    } catch (UnsupportedEncodingException ex) {
      return out.toString();
    }
  }

  public String standardErrorText() {
    return standardErrorText;
  }

  public int returnCode() {
    return returnCode;
  }

  public boolean isError() {
    return returnCode != 0;
  }

  private void captureOutput(Process process, Pipe pipe) {
    try {
      ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
      StreamPipeRunnable errorPipeRunnable = new StreamPipeRunnable(process.getErrorStream(), errorOutput);
      Pipe errorPipe = new Pipe(errorPipeRunnable);

      ProcessWatcher watcher = null;
      if (timeout != null) {
        watcher = new ProcessWatcher(process, timeout);
        watcher.start();
      }

      pipe.start();
      errorPipe.start();

      pipe.join();
      errorPipe.join();
      returnCode = process.waitFor();

      if (watcher != null) {
        if (watcher.timedOut()) {
          throw new CommandExecutorException("Command (" + cmd + ") timed out (" + timeout + " msecs)!");
        } else {
          watcher.cancel();
        }
      }

      process.destroy();

      standardErrorText = errorOutput.toString();

    } catch (InterruptedException intEx) {
      throw new CommandExecutorException("Command execution failed unexpectedly!", intEx);
    }
  }
}
