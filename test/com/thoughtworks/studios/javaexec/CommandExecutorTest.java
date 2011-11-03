//Copyright 2010 ThoughtWorks, Inc. Licensed under the Apache License, Version 2.0.

package com.thoughtworks.studios.javaexec;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class CommandExecutorTest {

    private File subFolder;

    @After
    public void tearDown() throws Exception {
        if (subFolder != null && subFolder.exists() && !subFolder.delete()) {
            System.err.println("Failed to delete folder: " + subFolder);
        }
    }

    @Test
    public void captureOutputWhenStreamingToOutputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String[] cmd = TestProgram.cmdFor(new String[]{"echo", "foobar"});
        CommandExecutor exec = new CommandExecutor(Arrays.asList(cmd));
        exec.run(outputStream);
        assertThat(outputStream.toString(), equalTo("foobar"));
    }

    @Test
    public void captureErrorWhenStreamingToOutputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String[] cmd = TestProgram.cmdFor(new String[]{"error", "something went wrong", "173"});
        CommandExecutor exec = new CommandExecutor(Arrays.asList(cmd));
        exec.run(outputStream);

        assertThat(exec.isError(), equalTo(true));
        assertThat(exec.standardErrorText(), equalTo("something went wrong"));
        assertThat(exec.returnCode(), equalTo(173));
    }

    @Test
    public void timeoutWhenStreamingtoOutputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String[] cmd = TestProgram.cmdFor(new String[]{"sleep", "5000"});
        CommandExecutor exec = new CommandExecutor(Arrays.asList(cmd), null, 100L);
        try {
            exec.run(outputStream);
            fail("should have timed out!");
        } catch (CommandExecutorException ex) {
            assertThat(ex.getMessage().contains("timed out"), equalTo(true));
            assertThat(exec.returnCode(), not(equalTo(0)));
        }
    }

    @Test
    public void captureOutputWhenStreamingToLineHandler() {
        TestLineHandler lineHandler = new TestLineHandler();
        String[] cmd = TestProgram.cmdFor(new String[]{"echo", "foo\nbar\n"});
        CommandExecutor exec = new CommandExecutor(Arrays.asList(cmd));
        exec.run(lineHandler);
        String[] expectedLines = new String[]{"foo", "bar"};
        lineHandler.assertLinesEqual(Arrays.asList(expectedLines));
    }

    @Test
    public void captureOutputWithoutStreaming() {
        String[] cmd = TestProgram.cmdFor(new String[]{"echo", "foobar"});
        CommandExecutor exec = new CommandExecutor(Arrays.asList(cmd));
        assertThat(exec.run(), equalTo("foobar"));
    }

    @Test
    public void captureOutputWhenRunningInADifferentDir() throws IOException {
        subFolder = new File(new File(".").getCanonicalPath(), "subFolder");
        boolean mkdir = subFolder.mkdir();
        assertTrue("Failed to create sub-directory: " + mkdir, mkdir);
        CommandExecutor exec = new CommandExecutor(Arrays.asList("pwd"), subFolder);
        String out = exec.run();
        assertThat(new File(out.trim()).getAbsolutePath(), equalTo(subFolder.getAbsolutePath()));
    }

    @Test
    public void captureChineseOutputWhenStreamingToOutputStream() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String[] cmd = TestProgram.cmdFor(new String[]{"chinese"});
        CommandExecutor exec = new CommandExecutor(Arrays.asList(cmd));
        exec.run(outputStream);
        assertThat(outputStream.toString("UTF-8"), equalTo("这是中文"));
    }

    @Test
    public void captureChineseOutputWhenStreamingToLineHandler() {
        TestLineHandler lineHandler = new TestLineHandler();
        String[] cmd = TestProgram.cmdFor(new String[]{"chinese"});
        CommandExecutor exec = new CommandExecutor(Arrays.asList(cmd));
        exec.run(lineHandler);
        String[] expectedLines = new String[]{"这是中文"};
        lineHandler.assertLinesEqual(Arrays.asList(expectedLines));
    }

    @Test
    public void captureChineseOutputWhenNotStreaming() throws Exception {
        String[] cmd = TestProgram.cmdFor(new String[]{"chinese"});
        CommandExecutor exec = new CommandExecutor(Arrays.asList(cmd));
        assertThat(exec.run(), equalTo("这是中文"));
    }

//  @Test
//  public void README_example_one() throws Exception {
//    CommandExecutor executor = new CommandExecutor(Arrays.asList("ls"));
//    String dirContents = executor.run();
//    System.out.println(dirContents);
//  }
//
//  @Test
//  public void README_example_two() throws Exception {
//    CommandExecutor executor = new CommandExecutor(Arrays.asList("ls"));
//    executor.run(new LineHandler() {
//      public void handleLine(String line) {
//        System.out.println(line);
//      }
//    });
//  }
//
//  @Test
//  public void README_example_three() throws Exception {
//    CommandExecutor executor = new CommandExecutor(Arrays.asList("ls"));
//    executor.run(System.out);
//  }
//
//  @Test
//  public void README_example_four() throws Exception {
//    long cmdTimeout = 3000; // 3 seconds
//    CommandExecutor executor = new CommandExecutor(Arrays.asList("sleep", "5"), cmdTimeout);
//    try {
//      executor.run();
//    } catch (CommandExecutorException e) {
//      System.out.println("Caught exception: " + e.getMessage());
//      // handle timeout if you like, however
//      // CommandExecutorException extends RuntimeException, so
//      // you can also let it kill your program if the
//      // scenario cannot reasonably be handled
//    }
//  }


}
