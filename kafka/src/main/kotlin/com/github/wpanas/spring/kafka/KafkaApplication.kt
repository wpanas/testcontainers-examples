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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@SpringBootApplication
class KafkaApplication

fun main(args: Array<String>) {
    runApplication<KafkaApplication>(*args)
}

@RestController
@RequestMapping("/order")
class OrderController(private val orderService: OrderService) {

    @PostMapping
    fun placeOrder(@RequestBody orderDto: CreateOrderDto): ShowOrderDto = OrderDetails(orderDto.coffee)
        .let { orderDetails -> orderService.placeOrder(orderDetails) }
        .let(Order::toDto)

    @GetMapping("/{id}")
    fun checkOrder(@PathVariable("id") id: UUID): ResponseEntity<ShowOrderDto> = orderService.findOne(id)
        ?.let(Order::toDto)
        .let { Optional.ofNullable(it) }
        .let { ResponseEntity.of(it) }

    @GetMapping
    fun listOrders(): List<ShowOrderDto> = orderService.findAll()
        .map(Order::toDto)
}

fun Order.toDto() = ShowOrderDto(id, coffee, isDone)

@Service
class OrderService(private val scheduler: OrderProcessingScheduler) {
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
class OrderProcessor(private val orderService: OrderService) : MessageListener<String, OrderDetails> {
    @KafkaListener(
        topics = [topic],
        groupId = "\${order-processor.group-id}",
        autoStartup = "\${order-processor.enabled:false}"
    )
    override fun onMessage(record: ConsumerRecord<String, OrderDetails>) {
        logger.info("Processing order: ${record.value()} with id: ${record.key()}")
        orderService.finishOrder(UUID.fromString(record.key()))
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrderProcessor::class.java)
    }
}

@Component
class OrderProcessingScheduler(private val kafkaTemplate: KafkaTemplate<String, OrderDetails>) {
    fun scheduleProcessing(order: Order) {
        kafkaTemplate.send(topic, order.id.toString(), OrderDetails(order.coffee))
            .addCallback(
                {
                    logger.info("Scheduled order ${order.id}")
                },
                { ex ->
                    logger.error("Scheduling order ${order.id} failed", ex)
                }
            )
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrderProcessingScheduler::class.java)
    }
}

data class OrderDetails(val coffee: String)

data class Order(val id: UUID, val coffee: String, val isDone: Boolean)

data class CreateOrderDto(val coffee: String)

data class ShowOrderDto(val id: UUID, val coffee: String, val isDone: Boolean)
