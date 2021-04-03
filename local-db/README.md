# Spring JPA with local PostgreSQL

This is all you need to set up PostgreSQL container that will start along with your application.

## Local Application code

```kotlin
fun main(args: Array<String>) {
	runApplication<Application>(*args) {
		addInitializers(PostgreSQLInitializer())
	}
}
```

[Full code](./src/test/kotlin/com/github/wpanas/spring/local/LocalApplication.kt)

## Test code

```kotlin
@SpringBootTest
@ContextConfiguration(initializers = [PostgreSQLInitializer::class])
@AutoConfigureMockMvc
internal class CatControllerTest {

}
```

[Full code](./src/test/kotlin/com/github/wpanas/spring/local/CatControllerTest.kt)

## Gradle dependencies

```kotlin
dependencies {
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.15.2"))
    testImplementation("org.testcontainers:postgresql")
}
```

[Full code](./build.gradle.kts)

# How to run it locally?

All modules share common Gradle wrapper and configuration. Go back to 
repository's main directory and run:

```shell script
./gradlew local-db:bootLocalRun
```