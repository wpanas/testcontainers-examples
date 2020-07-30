package com.github.wpanas.spring.local

import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.testcontainers.containers.PostgreSQLContainer

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
