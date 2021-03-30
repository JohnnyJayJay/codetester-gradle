plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.12.0"
}

group = "com.github.johnnyjayjay"
version = "0.2.0"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.retrofit2", "converter-gson", "2.9.0")
    implementation("com.squareup.retrofit2", "retrofit", "2.9.0")
}

val desc = "A Gradle plugin to automate remote CodeTester " +
        "(https://github.com/I-Al-Istannen/SimpleCodeTester) tests."

pluginBundle {
    vcsUrl = "https://github.com/JohnnyJayJay/codetester-gradle"
    website = vcsUrl
    tags = listOf("testing", "codetester", "education")
    description = desc
}

gradlePlugin {
    plugins {
        create("CodeTester") {
            id = "com.github.johnnyjayjay.codetester"
            implementationClass = "com.github.johnnyjayjay.codetester.gradle.CodeTesterPlugin"
            description = desc
            displayName = "CodeTester"
        }
    }
}
