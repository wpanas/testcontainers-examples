package com.github.wpanas.spring.local

import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        addInitializers(PostgreSQLInitializer())
    }
}
