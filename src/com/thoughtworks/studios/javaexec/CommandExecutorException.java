//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

public class CommandExecutorException extends RuntimeException {

  public CommandExecutorException(String s) {
    super(s);
  }

  public CommandExecutorException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
