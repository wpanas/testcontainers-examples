package com.github.wpanas.spring.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(properties = ["order-processor.enabled=false"])
@ContextConfiguration(initializers = [KafkaInitializer::class])
internal class OrderProcessorTest {

	@Autowired
	lateinit var underTest: OrderProcessor

	@Autowired
	lateinit var orderService: OrderService

	@Test
	internal fun `should process orders`() {
		val details = OrderDetails("Latte")
		val orderId = orderService.placeOrder(details).id

		underTest.onMessage(ConsumerRecord(topic, 0, 0, orderId.toString(), details))

		val order = orderService.findOne(orderId)

		assertNotNull(order)
		assertTrue(order?.isDone!!)
	}
}