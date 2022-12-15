//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import java.io.*;
import java.util.List;

public class CommandExecutor {

  private List<String> cmd;
  private String standardErrorText;
  private boolean noStandardErrorTextHere;
  private int returnCode;
  private Long timeout;
  private String workingDirectory;

  public CommandExecutor(List<String> cmd) {
    this(cmd, null, null);
  }

  public CommandExecutor(List<String> cmd, Long timeout) {
    this(cmd, timeout, null);
  }

  public CommandExecutor(List<String> cmd, String workingDirectory) {
    this(cmd, null, workingDirectory);
  }

  public CommandExecutor(List<String> cmd, Long timeout, String workingDirectory) {
    this.cmd = cmd;
    this.timeout = timeout;
    this.workingDirectory = workingDirectory;
  }

  public void run(OutputStream outputStream) {
    try {
      Process process = launchProcess();
      captureOutput(process, new StreamPipeRunnable(process.getInputStream(), outputStream));
    } catch (IOException ioEx) {
      throw new CommandExecutorException("Command execution failed unexpectedly!", ioEx);
    }
  }

  public void run(OutputStream outputStream, OutputStream errorOutputStream) {
    try {
      Process process = launchProcess();
      captureBothOutput(process,
              new StreamPipeRunnable(process.getInputStream(), outputStream),
              new StreamPipeRunnable(process.getErrorStream(), errorOutputStream)
      );
    } catch (IOException ioEx) {
      throw new CommandExecutorException("Command execution failed unexpectedly!");
    }
  }

  public void run(LineHandler out) {
    try {
      Process process = launchProcess();
      captureOutput(process, new LinePipeRunnable(process.getInputStream(), out));
    } catch (IOException ioEx) {
      throw new CommandExecutorException("Command execution failed unexpectedly!", ioEx);
    }
  }

  public void run(LineHandler out, LineHandler err) {
    try {
      Process process = launchProcess();
      captureBothOutput(process,
              new LinePipeRunnable(process.getInputStream(), out),
              new LinePipeRunnable(process.getErrorStream(), err)
      );
    } catch (IOException ioEx) {
      throw new CommandExecutorException("Command execution failed unexpectedly!", ioEx);
    }
  }

  private Process launchProcess() throws IOException {
    ProcessBuilder processBuilder = new ProcessBuilder(cmd);
    if (workingDirectory != null) processBuilder.directory(new File(workingDirectory));
    return processBuilder.start();
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
    if (noStandardErrorTextHere) {
      throw new IllegalStateException("The standard error text were already taken somewhere else!");
    }
    return standardErrorText;
  }

  public int returnCode() {
    return returnCode;
  }

  public boolean isError() {
    return returnCode != 0;
  }

  private void captureOutput(Process process, PipeRunnable pipeRunnable) {
      ByteArrayOutputStream errorOutput = new ByteArrayOutputStream();
      StreamPipeRunnable errorPipeRunnable = new StreamPipeRunnable(process.getErrorStream(), errorOutput);

      captureBothOutput(process, pipeRunnable, errorPipeRunnable);

      standardErrorText = errorOutput.toString();
      noStandardErrorTextHere = false;
  }

  private void captureBothOutput(Process process, PipeRunnable pipeRunnable, PipeRunnable errorPipeRunnable) {
    Pipe pipe = new Pipe(pipeRunnable);
    Pipe errorPipe = new Pipe(errorPipeRunnable);

    try {
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
      noStandardErrorTextHere = true;

    } catch (InterruptedException intEx) {
      throw new CommandExecutorException("Command execution failed unexpectedly!", intEx);
    }
  }
}
