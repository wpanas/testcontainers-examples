import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.5.31" apply false
	id("org.jmailen.kotlinter") version "3.6.0"
}

subprojects {
	apply {
		plugin("org.jetbrains.kotlin.jvm")
		plugin("org.jmailen.kotlinter")
	}

	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}

	val implementation by configurations

	dependencies {
		implementation(platform("org.testcontainers:testcontainers-bom:1.16.0"))
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "1.8"
		}
	}
}