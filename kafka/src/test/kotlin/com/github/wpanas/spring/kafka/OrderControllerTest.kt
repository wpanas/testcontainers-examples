package com.github.wpanas.spring.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.awaitility.kotlin.await
import org.awaitility.kotlin.has
import org.awaitility.kotlin.untilCallTo
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.text.IsEmptyString.emptyString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@ContextConfiguration(initializers = [KafkaInitializer::class])
@AutoConfigureMockMvc
internal class OrderControllerTest {
	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired
	lateinit var orderService: OrderService

	@Autowired
	lateinit var objectMapper: ObjectMapper

	@BeforeEach
	internal fun setUp() {
		orderService.deleteAll()
	}

	@Test
	internal fun `should be able to place order`() {
		val order = mockMvc.post("/order") {
			content = """{"coffee": "Espresso"}"""
			contentType = MediaType.APPLICATION_JSON
			accept = MediaType.APPLICATION_JSON
		}
				.andDo { print() }
				.andExpect {
					status { isOk }
					jsonPath("$.coffee", `is`("Espresso"))
					jsonPath("$.id", `is`(not(emptyString())))
					jsonPath("$.isDone", `is`(false))
				}
				.toOrder()


		await untilCallTo { orderService.findOne(order.id) } has {
			isDone
		}
	}

	@Test
	internal fun `should check order details`() {
		val order = orderService.placeOrder(OrderDetails("Latte"))

		mockMvc.get("/order/${order.id}") {
			contentType = MediaType.APPLICATION_JSON
			accept = MediaType.APPLICATION_JSON
		}
				.andDo { print() }
				.andExpect {
					status { isOk }
					jsonPath("$.coffee", `is`(order.coffee))
					jsonPath("$.id", `is`(order.id.toString()))
					jsonPath("$.isDone", isA(Boolean::class.java))
				}
	}

	@Test
	internal fun `should list orders`() {
		val firstOrder = orderService.placeOrder(OrderDetails("Latte")).toDto()
		val secondOrder = orderService.placeOrder(OrderDetails("Espresso")).toDto()

		mockMvc.get("/order") {
			contentType = MediaType.APPLICATION_JSON
			accept = MediaType.APPLICATION_JSON
		}
				.andDo { print() }
				.andExpect {
					status { isOk }
					jsonPath("$[*].coffee", containsInAnyOrder(firstOrder.coffee, secondOrder.coffee))
					jsonPath("$[*].id", containsInAnyOrder(firstOrder.id.toString(), secondOrder.id.toString()))
				}
	}

	private fun ResultActionsDsl.toOrder() = andReturn()
			.response
			.contentAsString
			.let {
				objectMapper.readValue<ShowOrderDto>(it)
			}
}
