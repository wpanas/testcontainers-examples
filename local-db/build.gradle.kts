plugins {
	kotlin("jvm") version "1.9.22"
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
	id("org.jmailen.kotlinter") version "4.2.0"
}

group = "com.github.wpanas.spring"

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("org.testcontainers:testcontainers-bom:1.19.5"))
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.springframework.boot:spring-boot-starter")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.testcontainers:postgresql")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<JavaExec>("bootLocalRun") {
	dependsOn("testClasses")
	group = "application"
	description = "It allows to boot app locally with Testcontainers"
	classpath = project.the<SourceSetContainer>()["test"].runtimeClasspath
	mainClass.set("com.github.wpanas.spring.local.LocalApplicationKt")
}
