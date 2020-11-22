package com.github.wpanas.spring.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

const val topic = "com.github.wpanas.orders"

@Configuration
class KafkaConfig {

    @Bean
    fun topic(): NewTopic {
        return TopicBuilder.name(topic)
            .build()
    }
}
