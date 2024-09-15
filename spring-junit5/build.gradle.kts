plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.spring.boot.legacy)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.spring.jpa)
	alias(libs.plugins.spring.kotlin)
	alias(libs.plugins.kotlinter)
}

group = "com.github.wpanas.spring"

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform(libs.testcontainers.bom))
	implementation(libs.bundles.kotlin)

	implementation(libs.spring.boot.starter)
	implementation(libs.spring.boot.jpa)
	implementation(libs.spring.boot.web)
	testImplementation(libs.spring.boot.test)

	runtimeOnly(libs.postgres)

	testImplementation(libs.testcontainers.postgres)
	testImplementation(libs.testcontainers.junit)
}

tasks.withType<Test> {
	useJUnitPlatform()
}


