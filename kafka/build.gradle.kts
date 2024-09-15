plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.spring.boot.legacy)
	alias(libs.plugins.spring.dependency.management)
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

	implementation(libs.spring.kafka)
	implementation(libs.spring.boot.starter)
	implementation(libs.spring.boot.web)
	testImplementation(libs.spring.boot.test)

	testImplementation(libs.bundles.awaitility)
	testImplementation(libs.spring.boot.kafka.test)
	testImplementation(libs.testcontainers.kafka)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<JavaExec>("bootLocalRun") {
	dependsOn("testClasses")
	description = "It allows to boot app locally with Testcontainers"
	group = "application"
	classpath = project.the<SourceSetContainer>()["test"].runtimeClasspath
	mainClass.set("com.github.wpanas.spring.kafka.LocalKafkaApplicationKt")
}