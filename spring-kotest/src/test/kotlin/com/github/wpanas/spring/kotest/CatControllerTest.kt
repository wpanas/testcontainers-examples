package com.github.wpanas.spring.kotest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.wpanas.spring.PostgreSQLTestContainer.Companion.postgreContainer
import com.github.wpanas.spring.PostgreSQLTestContainer.Companion.stopPostgreContainer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
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

@SpringBootTest
@AutoConfigureMockMvc
class CatControllerTest(
    val mockMvc: MockMvc,
    val catRepository: CatRepository,
    val objectMapper: ObjectMapper,
) : FunSpec({
        test("should add a new cat") {
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
                        jsonPath("$.id", `is`(CoreMatchers.notNullValue()))
                    }.toCat(objectMapper)

            val foundCat = catRepository.findByIdOrNull(cat.id!!)

            cat shouldBe foundCat
        }

        test("should list cats") {
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

        afterTest {
            catRepository.deleteAll()
        }

        afterSpec {
            stopPostgreContainer()
        }
    }) {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun postgreProperties(registry: DynamicPropertyRegistry) {
            val container = postgreContainer()
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }
}

private fun ResultActionsDsl.toCat(objectMapper: ObjectMapper) =
    andReturn()
        .response
        .contentAsString
        .let {
            objectMapper.readValue<Cat>(it)
        }
