//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import java.io.OutputStreamWriter;

public class TestProgram {

  public static String[] cmdFor(String[] args) {
    String[] cmd = new String[args.length + 4];
    cmd[0] = "java";
    cmd[1] = "-cp";
    cmd[2] = System.getProperty("java.class.path");
    cmd[3] = TestProgram.class.getName();
    for (int i = 4; i < cmd.length; i++) {
      cmd[i] = args[i - 4];
    }
    return cmd;
  }

  public static void main(String[] args) throws Exception {
    if (args[0].equals("sleep")) {
      Thread.sleep(Integer.valueOf(args[1]));
    } else if (args[0].equals("echo")) {
      OutputStreamWriter writer = new OutputStreamWriter(System.out, "UTF-8");
      writer.write(args[1]);
      writer.flush();
    } else if (args[0].equals("error")) {
      OutputStreamWriter writer = new OutputStreamWriter(System.err, "UTF-8");
      writer.write(args[1]);
      writer.flush();
      System.exit(Integer.valueOf(args[2]));
    } else if (args[0].equals("chinese")) {
      OutputStreamWriter writer = new OutputStreamWriter(System.out, "UTF-8");
      writer.write("这是中文");
      writer.flush();
    } else if (args[0].equals("pwd")) {
      OutputStreamWriter writer = new OutputStreamWriter(System.out, "UTF-8");
      writer.write(System.getProperty("user.dir"));
      writer.flush();
    }
  }
}
