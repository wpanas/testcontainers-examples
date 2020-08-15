package com.github.wpanas.spring.junit

import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
internal class CatControllerTest {

    companion object {
        @Container
        @JvmStatic
        val postgreSQLContainer = PostgreSQLContainer<Nothing>()

        @DynamicPropertySource
        @JvmStatic
        fun postgreProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var catRepository: CatRepository

    @AfterEach
    internal fun tearDown() {
        catRepository.deleteAll()
    }

    @Test
    internal fun `should add new cat`() {
        mockMvc.post("/cats") {
            content =
                """{"name": "Sherry"}"""
            contentType = APPLICATION_JSON
            accept = APPLICATION_JSON
        }
            .andDo { print() }
            .andExpect {
                status { isOk }
                jsonPath("$.name", `is`("Sherry"))
            }
    }

    @Test
    internal fun `should list cats`() {
        val benny = catRepository.save(Cat(id = null, name = "Benny"))
        val linda = catRepository.save(Cat(id = null, name = "Linda"))

        mockMvc.get("/cats") {
            accept = APPLICATION_JSON
        }
            .andDo { print() }
            .andExpect {
                status { isOk }
                jsonPath("$.totalElements", `is`(2))
                jsonPath("$.content[0].id", `is`(benny.id?.toInt()))
                jsonPath("$.content[0].name", `is`(benny.name))
                jsonPath("$.content[1].id", `is`(linda.id?.toInt()))
                jsonPath("$.content[1].name", `is`(linda.name))
            }
    }
}
