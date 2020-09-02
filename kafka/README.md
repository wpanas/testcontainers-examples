# Spring Kafka

This is all you need to set up Kafka container that will start along with your application 
and will be running during integration tests.

You can start application locally with configured `KafkaContainer`, starting `main` 
from [LocalKafkaApplication.kt](src/main/kotlin/com/github/wpanas/spring/kafka/LocalKafkaApplication.kt).

# How to run it?

All modules share common Gradle wrapper and configuration. Go back to 
repository's main directory and run:

```shell script
./gradlew kafka:bootRun -Plocal
```