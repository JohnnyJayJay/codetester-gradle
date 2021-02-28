# codetester-gradle

**Plugin is not tested/ready yet, currently this is just the initial state.**

`codetester-gradle` is a Gradle plugin that can be used to automatically test your code via a 
[CodeTester](https://github.com/I-Al-Istannen/SimpleCodeTester) instance.

## Installation
Currently only available by building it yourself:

```
$ git clone https://github.com/JohnnyJayJay/codetester-gradle
$ cd codetester-gradle
$ ./gradlew publishToMavenLocal
```

In your settings.gradle(.kts), add: 
```gradle
pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}
```

In your build.gradle(.kts):
```gradle
plugins {
    id("com.github.johnnyjayjay.codetester") version "0.1.0"
}

codeTester {
    username.set("YourUserName")
    password.set("YourPassword")
    category.set("Task Category Name")
    // Optionally (https://codetester.ialistannen.de/ by default):
    baseUrl.set("codetesterURL")
}
```