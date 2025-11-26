plugins {
    kotlin("jvm") version "2.1.10"
}

group = "org.rago"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.auth0:java-jwt:3.18.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}