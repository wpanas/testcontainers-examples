import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.kotlin.jvm) apply false
}

buildscript {
	repositories {
		mavenCentral()
	}
}

allprojects {
	version = "0.0.1-SNAPSHOT"

	tasks.withType<KotlinCompile> {
		compilerOptions {
			freeCompilerArgs.set(listOf("-Xjsr305=strict"))
			jvmTarget.set(JvmTarget.JVM_17)
		}
	}
}