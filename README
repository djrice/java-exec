## What? ##


A simple library for executing a subprocess from within a Java program.

java-exec provides:
* Simple capture of output as a string
* Handling output as streaming lines
* Piping the output to another stream
* Error capture
* Subprocess timeout


## Examples ##

### Simple output capture ###

    CommandExecutor executor = new CommandExecutor(Arrays.asList("ls"));
    String dirContents = executor.run();
    System.out.println(dirContents);
    

### Process output as lines ###

    CommandExecutor executor = new CommandExecutor(Arrays.asList("ls"));
    executor.run(new LineHandler() {
      public void handleLine(String line) {
        System.out.println(line);
      }
    });


### Stream output ###

    CommandExecutor executor = new CommandExecutor(Arrays.asList("ls"));
    executor.run(System.out);


### Subprocess timeout ###

    long cmdTimeout = 3000; // 3 seconds
    CommandExecutor executor = new CommandExecutor(Arrays.asList("sleep", "5"), cmdTimeout);
    try {
      executor.run();
    } catch (CommandExecutorException e) {
      System.out.println("Caught exception: " + e.getMessage());
      // handle timeout if you like, however
      // CommandExecutorException extends RuntimeException, so
      // you can also let it kill your program if the
      // scenario cannot reasonably be handled
    }


### Copyright
    
Copyright 2010 ThoughtWorks, Inc.

Written by David Rice (david.rice at gmail.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy of the
License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed
to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.
