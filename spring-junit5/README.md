# Spring JPA with PostgreSQL

This is all you need to set up PostgreSQL container inside Spring Boot Test.

## Test code

```kotlin
@SpringBootTest
@Testcontainers
internal class CatControllerTest {
    companion object {
    	@Container
    	@JvmStatic
    	val postgreSQLContainer = PostgreSQLContainer<Nothing>()
    
    	@DynamicPropertySource
    	@JvmStatic
    	fun postgreProperties(registry: DynamicPropertyRegistry) {
    		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
    		registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
    		registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
    	}
    }
}
```

[Full code](./src/test/kotlin/com/github/wpanas/spring/junit/CatControllerTest.kt)

## Gradle dependencies

```kotlin
dependencies {
    implementation(platform("org.testcontainers:testcontainers-bom:1.14.3"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}
```

[Full code](./build.gradle.kts)

# How to run it?

All modules share common Gradle wrapper and configuration. Go back to 
repository's main directory and run:

```shell script
./gradlew spring-junit5:bootRun -Plocal
```