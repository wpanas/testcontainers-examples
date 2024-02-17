package com.github.wpanas.spring

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.BindMode.READ_ONLY
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.utility.DockerImageName

class PostgreSQLTestContainer : PostgreSQLContainer<Nothing>(
    DockerImageName.parse("postgres:12.4"),
) {
    companion object {
        private const val CONTAINER_PATH = "/docker-entrypoint-initdb.d/"
        private lateinit var instance: PostgreSQLTestContainer
        private val logger: Logger = LoggerFactory.getLogger(PostgreSQLTestContainer::class.java)

        @JvmStatic
        fun startPostgreContainer() {
            if (!Companion::instance.isInitialized) {
                instance =
                    PostgreSQLTestContainer()
                        .apply {
                            withLogConsumer(Slf4jLogConsumer(logger))
                            withDatabaseName("cats_shelter")
                            withClasspathResourceMapping("init.sql", CONTAINER_PATH, READ_ONLY)
                        }
            }
            instance.start()
        }

        @JvmStatic
        fun stopPostgreContainer() {
            instance.stop()
        }

        @JvmStatic
        fun postgreContainer(): PostgreSQLContainer<Nothing> =
            startPostgreContainer()
                .run { instance }
    }
}
