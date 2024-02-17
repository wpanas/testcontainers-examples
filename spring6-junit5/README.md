# Spring JPA with PostgreSQL

This is all you need to set up PostgreSQL container inside Spring Boot Test with JUnit 5.

## Test code

```kotlin
@SpringBootTest
@Testcontainers
internal class CatControllerTest {
    companion object {
    	@Container
    	@ServiceConnection
    	val postgreSQLContainer = PostgreSQLContainer()
    }
}
```

[Full code](./src/test/kotlin/com/github/wpanas/spring/junit/CatControllerTest.kt)

## Gradle dependencies

```kotlin
dependencies {
    implementation(platform("org.testcontainers:testcontainers-bom:1.15.2"))
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}
```

[Full code](./build.gradle.kts)
