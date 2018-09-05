import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    java
    kotlin("jvm") version "1.2.61"
    `maven-publish`
    maven
}

group = "com.rnett.ligraph.eve"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://dl.bintray.com/kotlin/exposed")
    maven("https://dl.bintray.com/kotlin/ktor")
    jcenter()
}

fun getNewestCommit(gitURL: String, default: String = ""): String {
    try {
        return URL("https://api.github.com/repos/$gitURL/commits").readText()
                .substringAfter("\"sha\":\"").substringBefore("\",").substring(0, 10)
    } catch (e: Exception) {
        return default
    }
}

val sde_version = getNewestCommit("rnett/sde", "683fdbec1f")

dependencies {
    compile(kotlin("stdlib-jdk8"))
    implementation("com.github.rnett:dotlanmaps:1.0.+")
    implementation("com.github.rnett:sde:$sde_version")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(java.sourceSets["main"].allSource)
}

artifacts.add("archives", sourcesJar)

publishing {
    publications {
        create("default", MavenPublication::class.java) {
            from(components["java"])
            artifact(sourcesJar)
        }
        create("mavenJava", MavenPublication::class.java) {
            from(components["java"])
            artifact(sourcesJar)
        }
    }
    repositories {
        maven {
            url = uri("$buildDir/repository")
        }
    }
}