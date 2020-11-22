package com.github.wpanas.spring.kafka

import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<KafkaApplication>(*args) {
        addInitializers(KafkaInitializer())
    }
}
