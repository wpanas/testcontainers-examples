plugins {
    kotlin("jvm") version "1.6.20"
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version "1.7.10"
    kotlin("plugin.jpa") version "1.6.20"
    id("org.jmailen.kotlinter") version "3.10.0"
}

group = "com.github.wpanas.spring"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.testcontainers:testcontainers-bom:1.17.1"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    testImplementation("org.testcontainers:postgresql")

    testImplementation(platform("io.kotest:kotest-bom:5.2.3"))
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}