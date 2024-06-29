import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("maven-publish")
}

group = "dev.peytob.ecs"
version = "1.0"

repositories {
    mavenCentral()
}

publishing {

    repositories {
        mavenLocal()
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "kotlin-ecs"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

dependencies {
    implementation("com.google.guava:guava:33.1.0-jre")
    implementation("org.apache.commons:commons-lang3:3.14.0")

    testImplementation(kotlin("test"))
    testImplementation("org.mockito:mockito-core:4.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}