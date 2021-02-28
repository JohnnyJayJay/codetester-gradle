plugins {
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.github.johnnyjayjay"
version = "0.1.0"

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

tasks.compileJava

gradlePlugin {
    plugins {
        create("codeTester") {
            id = "com.github.johnnyjayjay.codetester"
            implementationClass = "com.github.johnnyjayjay.codetester.gradle.CodeTesterPlugin"
        }
    }
}
