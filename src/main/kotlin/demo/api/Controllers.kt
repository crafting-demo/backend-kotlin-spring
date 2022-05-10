package demo.api

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

import com.google.gson.GsonBuilder

import java.time.Instant
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@RestController
class ApiController() {

	@PostMapping("/api")
	fun NestedCallHandler(@RequestBody message: Message): String {
		val receivedAt = Instant.now().toString()
		var errors = arrayListOf<String>()
		val gson = GsonBuilder().create()

		val request = gson.toJson(message)

		for (action in message.actions) {
			when (action.action) {
				"Echo" -> {
					action.status = "Passed"
				}

				"Read" -> {
					val res = readEntity(action.payload.serviceName, action.payload.key)
					if (res.errors != null) {
						errors.addAll(listOf(res.errors ?: ""))
						action.status = "Failed"
					} else {
						action.status = "Passed"
						action.payload.value = res.value
					}
				}

				"Write" -> {
					val res = writeEntity(action.payload.serviceName, action.payload.key, action.payload.value)
					if (res.errors != null) {
						errors.addAll(listOf(res.errors ?: ""))
						action.status = "Failed"
					} else {
						action.status = "Passed"
					}
				}

				"Call" -> {
					val resp = serviceCall(action.payload)
					if (resp == null) {
						errors.addAll(listOf("failed to call service " + action.payload.serviceName))
						action.status = "Failed"
					} else {
						action.status = "Passed"
						action.payload.actions = resp.actions
					}
				}
			}

			action.serviceName = "backend-kotlin-spring"
			action.returnTime = Instant.now().toString()
		}

		message.meta.returnTime = Instant.now().toString()

		val response = gson.toJson(message)

		LogContext(request, response, errors, receivedAt)

		return response
	}

	fun serviceCall(payload: Payload): Message? {
		val gson = GsonBuilder().create()
		val client = HttpClient.newBuilder().build()

		val meta: Meta = Meta("backend-kotlin-spring", payload.serviceName ?: "", Instant.now().toString(), null)
		val message: Message = Message(meta, payload.actions ?: arrayOf<Action>())
		val messageStringJSON = gson.toJson(message)

		val request = HttpRequest.newBuilder()
			.uri(URI.create(serviceEndpoint(payload.serviceName ?: "")))
			.header("Content-Type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(messageStringJSON))
			.build()
		val response = client.send(request, HttpResponse.BodyHandlers.ofString())

		return gson.fromJson<Message>(response.body(), Message::class.java)
	}

	fun serviceEndpoint(serviceName: String): String {
		val suffix = System.getenv("SANDBOX_ENDPOINT_DNS_SUFFIX") + "/api"
		when (serviceName) {
			"backend-go-gin" -> return "https://gin" + suffix
			"backend-typescript-express" -> return "https://express" + suffix
			"backend-ruby-rails" -> return "https://rails" + suffix
			"backend-kotlin-spring" -> return "https://spring" + suffix
			"backend-python-django" -> return "https://django" + suffix
			else -> return "unknown"
		}
	}
}
