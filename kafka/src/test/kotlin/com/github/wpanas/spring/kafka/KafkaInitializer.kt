package com.github.wpanas.spring.kafka

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

class KafkaInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private companion object {
        val kafkaContainer =
            KafkaContainer(
                DockerImageName.parse("confluentinc/cp-kafka"),
            )
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        kafkaContainer.start()

        mapOf(
            "spring.kafka.bootstrapServers" to kafkaContainer.bootstrapServers,
        ).let(TestPropertyValues::of)
            .applyTo(applicationContext)
    }
}
