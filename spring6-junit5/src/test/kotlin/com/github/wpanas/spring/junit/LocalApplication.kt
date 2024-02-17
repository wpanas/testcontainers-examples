package com.github.wpanas.spring.junit

import org.springframework.boot.SpringApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    SpringApplication.from { arguments -> runApplication<JunitApplication>(*arguments) }
        .with(PostgreSQLConfiguration::class.java)
        .run(*args)
}
