//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class TestProgram {

  public static String[] cmdFor(String[] args) {
    String[] cmd = new String[args.length + 4];
    cmd[0] = "java";
    cmd[1] = "-cp";
    cmd[2] = System.getProperty("java.class.path");
    cmd[3] = TestProgram.class.getName();
    System.arraycopy(args, 0, cmd, 4, cmd.length - 4);
    return cmd;
  }

  public static void main(String[] args) throws Exception {
    switch (args[0]) {
      case "sleep":
        Thread.sleep(Integer.parseInt(args[1]));
        break;
      case "echo": {
        OutputStreamWriter writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
        writer.write(args[1]);
        writer.flush();
        break;
      }
      case "error": {
        OutputStreamWriter writer = new OutputStreamWriter(System.err, StandardCharsets.UTF_8);
        writer.write(args[1]);
        writer.flush();
        System.exit(Integer.parseInt(args[2]));
      }
      case "tee": {
        OutputStreamWriter outWriter = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
        OutputStreamWriter errWriter = new OutputStreamWriter(System.err, StandardCharsets.UTF_8);
        outWriter.write(args[1]);
        errWriter.write(args[1]);
        outWriter.flush();
        errWriter.flush();
        break;
      }
      case "chinese": {
        OutputStreamWriter writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
        writer.write("这是中文");
        writer.flush();
        break;
      }
      case "pwd": {
        OutputStreamWriter writer = new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
        writer.write(System.getProperty("user.dir"));
        writer.flush();
        break;
      }
    }
  }
}
