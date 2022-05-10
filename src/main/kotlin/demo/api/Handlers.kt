package demo.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@PostMapping("/api")
fun NestedCallHandler(@RequestBody message: Message): Message {
	return message
}
