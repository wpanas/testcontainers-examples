package com.github.wpanas.spring.junit

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class PostgreSQLConfiguration {
    @Bean
    @ServiceConnection
    fun postgreSqlContainer() =
        PostgreSQLContainer(
            DockerImageName.parse("postgres:12.4"),
        ).apply {
            withDatabaseName("cats_shelter")
            withClasspathResourceMapping("init.sql", CONTAINER_PATH, BindMode.READ_ONLY)
        }

    companion object {
        private const val CONTAINER_PATH = "/docker-entrypoint-initdb.d/"
    }
}
