# Spring JPA with local PostgreSQL

This is all you need to set up PostgreSQL container that will start along with your application.

## Local Application code

```kotlin
fun main(args: Array<String>) {
	runApplication<Application>(*args) {
		addInitializers(PostgreSQLInitializer())
	}
}

class PostgreSQLInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
	companion object {
		val postgreSQLContainer = PostgreSQLContainer<Nothing>()
	}

	override fun initialize(applicationContext: ConfigurableApplicationContext) {
		postgreSQLContainer.start()

		val properties = mapOf(
				Pair("spring.datasource.url", postgreSQLContainer.jdbcUrl),
				Pair("spring.datasource.username", postgreSQLContainer.username),
				Pair("spring.datasource.password", postgreSQLContainer.password)
		)

		applicationContext.environment.apply {
			propertySources.addFirst(MapPropertySource(
					"local",
					properties
			))
		}
	}
}
```

[Full code](./src/main/kotlin/com/github/wpanas/spring/local/LocalApplication.kt)

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
    implementation(platform("org.testcontainers:testcontainers-bom:1.14.3"))
    implementation("org.testcontainers:postgresql")
}
```

[Full code](./build.gradle.kts)