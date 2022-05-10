package demo.api

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
class ApiController() {
	@PostMapping("/api")
	fun NestedCallHandler(@RequestBody message: Message): Message {
		return message
	}
}
