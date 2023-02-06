plugins {
	kotlin("jvm") version "1.7.20"
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("plugin.spring") version "1.8.10"
	id("org.jmailen.kotlinter") version "3.12.0"
}

group = "com.github.wpanas.spring"

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("org.testcontainers:testcontainers-bom:1.17.5"))
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testImplementation("org.awaitility:awaitility:4.2.0")
	testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:kafka")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<JavaExec>("bootLocalRun") {
	dependsOn("testClasses")
	group = "application"
	classpath = project.the<SourceSetContainer>()["test"].runtimeClasspath
	mainClass.set("com.github.wpanas.spring.kafka.LocalKafkaApplicationKt")
}