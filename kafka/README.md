# Spring Kafka

This is all you need to set up Kafka container that will start along with your application 
and will be running during integration tests.

You can start application locally too with the same `KafkaContainer`.
All modules share common Gradle wrapper and configuration. Go back to 
repository's main directory and run:

```shell script
./gradlew kafka:bootLocalRun
```

## How can you achieve it on your own?

```groovy
tasks.register<JavaExec>("bootLocalRun") {
	dependsOn("testClasses")
	description = "It allows to boot app locally with Testcontainers"
	group = "application"
	classpath = project.the<SourceSetContainer>()["test"].runtimeClasspath
	mainClass.set("com.github.wpanas.spring.kafka.LocalKafkaApplicationKt")
}
```
Change the `mainClass` and you're ready to go.