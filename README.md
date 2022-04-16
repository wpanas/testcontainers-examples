# Testcontainers Examples

This repository contains sample projects
that use [Testcontainers](https://www.testcontainers.org/)
with different technologies. 
I hope it will help you use it in your own projects.

All samples are build with [Kotlin](https://kotlinlang.org/), [Gradle](https://gradle.org/)
& [JUnit 5](https://junit.org/junit5/).
Continuous integration is running at [CircleCI](https://circleci.com/).

Current tests status is:
[![wpanas](https://circleci.com/gh/wpanas/testcontainers-examples.svg?style=svg)](https://circleci.com/gh/wpanas/testcontainers-examples)

# Sample projects

- [Simplest Spring MVC & JPA based on PostgreSQL](./spring-junit5/README.md)
- [Spring MVC & JPA with Kotest tests](./spring-kotest/README.md)
- [Spring MVC & JPA with configured PostgreSQL local running](./local-db/README.md)
- [Spring Kafka with configured local running](./kafka/README.md)

# How to use it?

Running tests
```shell script
# run all tests
./gradlew check 

# run kafka module tests
./gradlew kafka:check

# run local-db module tests
./gradlew local-db:check 

# run spring-junit5 tests
./gradlew spring-junit5:check 

# run spring-kotest tests
./gradlew spring-kotest:check
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