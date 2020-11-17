package com.github.wpanas.spring.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.MapPropertySource
import org.springframework.kafka.config.TopicBuilder
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

const val topic = "com.github.wpanas.orders"

@Configuration
class KafkaConfig {

    @Bean
    fun topic(): NewTopic {
        return TopicBuilder.name(topic)
            .build()
    }
}

class KafkaInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private val kafkaContainer = KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka")
    )

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        kafkaContainer.start()

        val properties = mapOf(
            "spring.kafka.bootstrapServers" to kafkaContainer.bootstrapServers
        )

        applicationContext.environment.apply {
            propertySources.addFirst(
                MapPropertySource(
                    "local",
                    properties
                )
            )
        }
    }
}
