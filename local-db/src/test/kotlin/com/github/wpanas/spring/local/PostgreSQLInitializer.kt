package com.github.wpanas.spring.local

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgreSQLInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private companion object {
        val postgreSQLContainer =
            PostgreSQLContainer(
                DockerImageName.parse("postgres:12.4"),
            )
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        postgreSQLContainer.start()

        mapOf(
            "spring.datasource.url" to postgreSQLContainer.jdbcUrl,
            "spring.datasource.username" to postgreSQLContainer.username,
            "spring.datasource.password" to postgreSQLContainer.password,
        ).let(TestPropertyValues::of)
            .applyTo(applicationContext)
    }
}
