package com.github.wpanas.spring.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.MessageListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@SpringBootApplication
class KafkaApplication

fun main(args: Array<String>) {
	runApplication<KafkaApplication>(*args)
}

@RestController
@RequestMapping("/order")
class OrderController(private val orderRepository: OrderRepository) {

	@PostMapping
	fun placeOrder(@RequestBody orderDto: CreateOrderDto): ShowOrderDto = OrderDetails(orderDto.coffee)
			.let { orderDetails -> orderRepository.placeOrder(orderDetails) }
			.let(Order::toDto)

	@GetMapping("/{id}")
	fun checkOrder(@PathVariable("id") id: UUID): ResponseEntity<ShowOrderDto> = orderRepository.findOne(id)
			?.let(Order::toDto)
			.let { Optional.ofNullable(it) }
			.let { ResponseEntity.of(it) }

	@GetMapping
	fun listOrders(): List<ShowOrderDto> = orderRepository.findAll()
			.map(Order::toDto)
}

fun Order.toDto() = ShowOrderDto(id, coffee, isDone)

@Service
class OrderRepository(private val scheduler: OrderProcessingScheduler) {
	private val orders: ConcurrentMap<UUID, Order> = ConcurrentHashMap()

	fun placeOrder(orderDetails: OrderDetails): Order {
		val order = Order(UUID.randomUUID(), orderDetails.coffee, false)
		orders[order.id] = order
		scheduler.scheduleProcessing(order)
		return order
	}

	fun finishOrder(orderId: UUID): Order? = findOne(orderId)
			?.run { copy(isDone = true) }
			?.also { orders[it.id] = it }

	fun findOne(orderId: UUID): Order? = orders[orderId]

	fun findAll(): Set<Order> = orders.values.toSet()
	fun deleteAll() {
		orders.clear()
	}
}

@Component
class OrderProcessor(private val orderRepository: OrderRepository) : MessageListener<String, OrderDetails> {
	@KafkaListener(topics = [topic], groupId = "\${order-processor.group-id}", autoStartup = "\${order-processor.enabled:false}")
	override fun onMessage(data: ConsumerRecord<String, OrderDetails>?) {
		data?.let { record ->
			logger.info("Processing order: ${record.value()} with id: ${record.key()}")
			orderRepository.finishOrder(UUID.fromString(record.key()))
		}
	}

	companion object {
		private val logger: Logger = LoggerFactory.getLogger(OrderProcessor::class.java)
	}
}

@Component
class OrderProcessingScheduler(private val kafkaTemplate: KafkaTemplate<String, OrderDetails>) {
	fun scheduleProcessing(order: Order) {
		kafkaTemplate.send(topic, order.id.toString(), OrderDetails(order.coffee))
				.addCallback({
					logger.info("Scheduled order ${order.id}")
				}, { ex ->
					logger.error("Scheduling order ${order.id} failed", ex)
				})
	}

	companion object {
		private val logger: Logger = LoggerFactory.getLogger(OrderProcessingScheduler::class.java)
	}
}

data class OrderDetails(val coffee: String)

data class Order(val id: UUID, val coffee: String, val isDone: Boolean)

data class CreateOrderDto(val coffee: String)

data class ShowOrderDto(val id: UUID, val coffee: String, val isDone: Boolean)