# codetester-gradle

`codetester-gradle` is a Gradle plugin that can be used to automatically test your code via a 
[CodeTester](https://github.com/I-Al-Istannen/SimpleCodeTester) instance. 

This is useful for students of the "Programmieren" (Programming) lecture of the KIT (Karlsruhe Institute of Technology) who want to automate tests for their excersises/finals before submission. Prior knowledge of Gradle is not strictly necessary, but I won't cover the details here. If you want to learn Gradle, I'd
recommend the [official documentation](https://docs.gradle.org/current/samples/sample_building_java_applications.html) (it'll be useful if you do further Java development). 

As an alternative and if you don't want to bother learning or setting up Gradle, there's the [codetester-idea](https://plugins.jetbrains.com/plugin/15284-codetester-idea) plugin for IntelliJ. 

## Installation

**Prerequisites:**
- [JDK 11](https://adoptopenjdk.net)
- A [Gradle](https://gradle.org/) project
  - [How to set up a Gradle project in IntelliJ](https://www.jetbrains.com/help/idea/gradle.html#convert_project_to_gradle)

### Applying

In your build.gradle(.kts):
```gradle
plugins {
    id("com.github.johnnyjayjay.codetester") version "0.2.0"
}
```

## Configuration
The plugin provides an extension where you can set the most important things, namely username, password and task name you want to use for the code tester instance.
```gradle
import java.time.Duration
import java.time.temporal.ChronoUnit

codeTester {
    username.set("YourUserName")
    password.set("YourPassword")
    category.set("Task Category Name")
    // Optionally (https://codetester.ialistannen.de/ by default):
    baseUrl.set("codetesterURL")
    // Optionally (1 minute by default)
    readTimeout.set(Duration.of(5, ChronoUnit.MINUTES)) 
}
```

The two tasks this plugin adds are `zipSource` (of type `ZipSource`) and `codeTester` (of type `CodeTester`). You can register own tasks of these
types or configure the existing ones (setting a different directory for source zips, excluding/including different files (`edu.kit.informatik.Terminal` is excluded by default), etc.

### Building it Yourself

You will need a [Git Installation](https://git-scm.com/downloads) to build it yourself.

Instructions (on Windows, run this in PowerShell or git bash): 

```
$ git clone https://github.com/JohnnyJayJay/codetester-gradle
$ cd codetester-gradle
$ ./gradlew publishToMavenLocal
```

## Running
To get test results, simply run the `codeTester` task (either from the command line `./gradlew codeTester` or from your IDE integration). 

The task will first fetch a login token and get access to the remote codetester instance. It will then upload your source code and wait for a response.
There can be three different outcomes:

1. A http issue occurred (e.g. if your credentials are invalid)
2. Your code was submitted, but could not be compiled (thus no tests were run)
3. Your code was submitted and the tests were run

The task will give you feedback in the console and if the tests were run, a log for each file that was tested can be found in `${buildDir}/codeTester/your.class.Name.java.log` where the interactions of all tests are saved.
