package com.github.wpanas.spring.junit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.BindMode.READ_ONLY
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
internal class CatControllerTest {
    companion object {
        private const val CONTAINER_PATH = "/docker-entrypoint-initdb.d/"
        private val logger: Logger = LoggerFactory.getLogger(CatControllerTest::class.java)

        @Container
        val postgreSQLContainer =
            PostgreSQLContainer(
                DockerImageName.parse("postgres:12.4"),
            ).apply {
                withLogConsumer(Slf4jLogConsumer(logger))
                withDatabaseName("cats_shelter")
                withClasspathResourceMapping("init.sql", CONTAINER_PATH, READ_ONLY)
            }

        @JvmStatic
        @DynamicPropertySource
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
        val cat =
            mockMvc
                .post("/cats") {
                    content =
                        """{"name": "Sherry"}"""
                    contentType = APPLICATION_JSON
                    accept = APPLICATION_JSON
                }.andDo { print() }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.name", `is`("Sherry"))
                    jsonPath("$.id", `is`(notNullValue()))
                }.toCat()

        val foundCat = catRepository.findByIdOrNull(cat.id!!)
        assertEquals(foundCat, cat)
    }

    @Test
    internal fun `should list cats`() {
        val benny = catRepository.save(Cat(id = null, name = "Benny"))
        val linda = catRepository.save(Cat(id = null, name = "Linda"))

        mockMvc
            .get("/cats") {
                accept = APPLICATION_JSON
            }.andDo { print() }
            .andExpect {
                status { isOk() }
                jsonPath("$.totalElements", `is`(2))
                jsonPath("$.content[0].id", `is`(benny.id?.toInt()))
                jsonPath("$.content[0].name", `is`(benny.name))
                jsonPath("$.content[1].id", `is`(linda.id?.toInt()))
                jsonPath("$.content[1].name", `is`(linda.name))
            }
    }

    @Autowired
    lateinit var objectMapper: ObjectMapper

    private fun ResultActionsDsl.toCat() =
        andReturn()
            .response
            .contentAsString
            .let {
                objectMapper.readValue<Cat>(it)
            }
}
