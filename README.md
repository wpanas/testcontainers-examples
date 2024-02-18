# Testcontainers Examples

This repository contains sample projects
that use [Testcontainers](https://www.testcontainers.org/)
with different technologies. 
I hope it will help you use it in your own projects.

All samples are build with [Kotlin](https://kotlinlang.org/), [Gradle](https://gradle.org/)
& [JUnit 5](https://junit.org/junit5/).

👉 If you use JDK 17+ & Spring Boot 3.1+ go straight
to the [simplest project](./spring6-junit5/README.md). 
Other projects use older Spring Boot 2.7 to show
how to use Testcontainers without all great advancements
that since were made. As for now, 2.7 is still commercially supported
until 2025-08-24. I encourage you to update Spring Boot according
to the [official support timeline](https://spring.io/projects/spring-boot#support)
to have the best experience with Testcontainers and what not.

Current tests status is:
[![wpanas](https://github.com/wpanas/testcontainers-examples/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/wpanas/testcontainers-examples/actions/workflows/ci.yml?query=branch%3Amaster)

# Sample projects

- **NEW!** [[Spring Boot 3.1+]: The simplest Spring MVC, JPA on PostgreSQL & local running](./spring6-junit5/README.md)
- [[Spring Boot 2.7]: MVC & JPA with PostgreSQL](./spring-junit5/README.md)
- [[Spring Boot 2.7]: MVC & JPA with Kotest tests](./spring-kotest/README.md)
- [[Spring Boot 2.7]: MVC & JPA with PostgreSQL & local running](./local-db/README.md)
- [[Spring Boot 2.7]: Kafka with local running](./kafka/README.md)

# How to use it?

Running tests
```shell script
# run all tests
./gradlew check 

# run kafka module tests
./gradlew kafka:check

# run local-db module tests
./gradlew local-db:check 

# run spring6-junit5 tests
./gradlew spring6-junit5:check 

# run spring-junit5 tests
./gradlew spring-junit5:check 

# run spring-kotest tests
./gradlew spring-kotest:check
```

💥 **NEW!** To run locally application with a PostgreSQL container configured by Testcontainers & Spring Boot 3.1+.
```shell script
./gradlew spring6-junit5:bootTestRun
```

To run locally application with a Kafka container configured by Testcontainers.
```shell script
./gradlew kafka:bootLocalRun
```

To run locally application with a PostgreSQL container configured by Testcontainers.
```shell script
./gradlew local-db:bootLocalRun
```

# Troubleshooting

## ContainerFetchException
If you got `ContainerFetchException` 
because of failing `Docker environment should have more than 2GB free disk space`,
increase available disc space in Docker for Mac. See [more](https://github.com/testcontainers/testcontainers-java/issues/1726).