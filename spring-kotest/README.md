# Spring JPA with PostgreSQL

This is all you need to set up PostgreSQL container inside Spring Boot Test with Kotest.
Inspired by [this post](https://akobor.me/posts/using-testcontainers-with-micronaut-and-kotest).

## Container code

```kotlin
class PostgreSQLTestContainer : PostgreSQLContainer<Nothing>(
    DockerImageName.parse("postgres:12.4")
) {
    companion object {
        private const val containerPath = "/docker-entrypoint-initdb.d/"
        private lateinit var instance: PostgreSQLTestContainer
        private val logger: Logger = LoggerFactory.getLogger(PostgreSQLTestContainer::class.java)

        @JvmStatic
        fun startPostgreContainer() {
            if (!Companion::instance.isInitialized) {
                instance = PostgreSQLTestContainer()
                    .apply {
                        withLogConsumer(Slf4jLogConsumer(logger))
                        withDatabaseName("cats_shelter")
                        withClasspathResourceMapping("init.sql", containerPath, READ_ONLY)
                    }
            }
            instance.start()
        }

        @JvmStatic
        fun stopPostgreContainer() {
            instance.stop()
        }

        @JvmStatic
        fun postgreContainer(): PostgreSQLContainer<Nothing> = startPostgreContainer()
            .run { instance }
    }
}
```

## Test code

```kotlin
@SpringBootTest
@Testcontainers
internal class CatControllerTest {
    companion object {
        @DynamicPropertySource
        @JvmStatic
        fun postgreProperties(registry: DynamicPropertyRegistry) {
            val container = postgreContainer()
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }
}
```

[Full code](./src/test/kotlin/com/github/wpanas/spring/kotest/CatControllerTest.kt)

## Gradle dependencies

```kotlin
dependencies {
    implementation(platform("org.testcontainers:testcontainers-bom:1.16.3"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")

    testImplementation(platform("io.kotest:kotest-bom:5.8.0"))
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
}
```

[Full code](./build.gradle.kts)
