plugins {
	id("org.springframework.boot") version "2.3.3.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("plugin.spring") version "1.4.0"
}

group = "com.github.wpanas.spring"

dependencies {
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testImplementation("org.awaitility:awaitility:4.0.3")
	testImplementation("org.awaitility:awaitility-kotlin:4.0.3")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	implementation("org.testcontainers:kafka")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
