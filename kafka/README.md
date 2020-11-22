# Spring Kafka

This is all you need to set up Kafka container that will start along with your application 
and will be running during integration tests.

You can start application locally too with the same `KafkaContainer`.
All modules share common Gradle wrapper and configuration. Go back to 
repository's main directory and run:

```shell script
./gradlew kafka:bootLocalRun
```