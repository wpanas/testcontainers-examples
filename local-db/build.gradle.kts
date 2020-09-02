plugins {
	id("org.springframework.boot") version "2.3.3.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("plugin.spring") version "1.4.0"
	kotlin("plugin.jpa") version "1.4.0"
}

group = "com.github.wpanas.spring"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	implementation("org.testcontainers:postgresql")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

springBoot {
	mainClassName = when (project.hasProperty("local")) {
		true -> "com.github.wpanas.spring.local.LocalApplicationKt"
		else -> "com.github.wpanas.spring.local.ApplicationKt"
	}
}

