import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.22" apply false
}

buildscript {
	repositories {
		mavenCentral()
	}
}

allprojects {
	version = "0.0.1-SNAPSHOT"

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}
}