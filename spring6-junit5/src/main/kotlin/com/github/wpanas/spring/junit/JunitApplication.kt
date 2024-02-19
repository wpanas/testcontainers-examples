package com.github.wpanas.spring.junit

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class JunitApplication

fun main(args: Array<String>) {
    runApplication<JunitApplication>(*args)
}

@Entity
data class Cat(
    @field:Id
    @field:GeneratedValue(strategy = IDENTITY)
    val id: Long?,
    val name: String,
)

data class CreateCatDto(val name: String) {
    fun toCat() = Cat(id = null, name = name)
}

data class ShowCatDto(val id: Long, val name: String)

fun Cat.toDto() = ShowCatDto(id!!, name)

@Repository
interface CatRepository : JpaRepository<Cat, Long>

@RestController
@RequestMapping("/cats")
class CatController(private val catRepository: CatRepository) {
    @GetMapping
    fun findAll(pageable: Pageable): Page<ShowCatDto> =
        catRepository.findAll(pageable)
            .map(Cat::toDto)

    @PostMapping
    fun save(
        @RequestBody catDto: CreateCatDto,
    ): ShowCatDto = catRepository.save(catDto.toCat()).toDto()
}