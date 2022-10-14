plugins {
	kotlin("jvm") version "1.7.20"
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("plugin.spring") version "1.6.20"
	kotlin("plugin.jpa") version "1.6.20"
	id("org.jmailen.kotlinter") version "3.10.0"
}

group = "com.github.wpanas.spring"

repositories {
	mavenCentral()
}

dependencies {
	implementation(platform("org.testcontainers:testcontainers-bom:1.17.4"))
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter")
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
	classpath = project.the<SourceSetContainer>()["test"].runtimeClasspath
	mainClass.set("com.github.wpanas.spring.local.LocalApplicationKt")
}
