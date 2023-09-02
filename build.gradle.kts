plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-all:4.1.96.Final")
    implementation("org.conscrypt:conscrypt-openjdk-uber:2.4.0")
    testImplementation("org.slf4j:slf4j-reload4j:2.0.7")
    implementation("org.slf4j:slf4j-api:2.0.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}