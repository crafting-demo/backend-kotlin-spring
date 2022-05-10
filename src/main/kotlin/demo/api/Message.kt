package demo.api

data class Message(
	var meta: Meta, 
	var actions: Array<Action>
)

data class Meta(
	var caller: String,
	var callee: String,
	var callTime: String,
	var returnTime: String?
)

data class Action(
	var serviceName: String?,
	var action: String,
	var payload: Payload,
	var status: String?,
	var returnTime: String?
)

data class Payload(
	var serviceName: String?,
	var actions: Array<Action>?,
	var key: String?,
	var value: String?
)
