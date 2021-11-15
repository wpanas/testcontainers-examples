plugins {
	id("org.springframework.boot") version "2.5.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("plugin.spring") version "1.6.0"
	kotlin("plugin.jpa") version "1.5.31"
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

	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
}

tasks.withType<Test> {
	useJUnitPlatform()
}


