package demo.api

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@CrossOrigin
@PostMapping("/api")
fun NestedCallHandler(@RequestBody message: Message): Message {
	return message
}
