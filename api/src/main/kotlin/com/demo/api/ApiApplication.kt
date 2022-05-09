package com.demo.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping

@SpringBootApplication
@RestController
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}

@CrossOrigin
@PostMapping("/api")
fun NestedCallHandler(@RequestBody message: Message): Message {
	return message
}

data class Message(
	val meta: Meta, 
	val actions: Array<Action>
)

data class Meta(
	val caller: String,
	val callee: String,
	val callTime: String,
	val returnTime: String?
)

data class Action(
	val serviceName: String?,
	val action: String,
	val payload: Payload,
	val status: String?,
	val returnTime: String?
)

data class Payload(
	val serviceName: String?,
	val actions: Array<Action>?,
	val key: String?,
	val value: String?
)
