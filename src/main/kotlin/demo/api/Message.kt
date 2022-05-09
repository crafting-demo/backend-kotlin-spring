package demo.api

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
