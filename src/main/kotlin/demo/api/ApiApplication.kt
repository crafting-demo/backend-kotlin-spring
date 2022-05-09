package demo.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.RestController
import org.springframework.boot.runApplication

@SpringBootApplication
@RestController
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
