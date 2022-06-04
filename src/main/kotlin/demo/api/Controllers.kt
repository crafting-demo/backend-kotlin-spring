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
class ApiController(private val mysqlRepo: SampleMysqlRepo, private val mongoRepo: SampleMongoRepo) {

	@PostMapping("/api")
	fun NestedCallHandler(@RequestBody message: Message): String {
		// LoggerWrite("Test Test Crafting")

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
		var host = ""
		var port = ""
		when (serviceName) {
			"backend-go-gin" -> {
				host = System.getenv("GIN_SERVICE_HOST")
				port = System.getenv("GIN_SERVICE_PORT")
			}
			"backend-typescript-express" -> {
				host = System.getenv("EXPRESS_SERVICE_HOST")
				port = System.getenv("EXPRESS_SERVICE_PORT")
			}
			"backend-ruby-rails" -> {
				host = System.getenv("RAILS_SERVICE_HOST")
				port = System.getenv("RAILS_SERVICE_PORT")
			}
			"backend-kotlin-spring" -> {
				host = System.getenv("SPRING_SERVICE_HOST")
				port = System.getenv("SPRING_SERVICE_PORT")
			}
			"backend-python-django" -> {
				host = System.getenv("DJANGO_SERVICE_HOST")
				port = System.getenv("DJANGO_SERVICE_PORT")
			}
		}
		return "http://" + host + ":" + port + "/api"
	}

	fun readEntity(store: String?, key: String?): OpResponse {
		when (store) {
			"mysql" -> return readMySQL(mysqlRepo, key)
			"mongodb" -> return readMongoDB(mongoRepo, key)
			else -> return OpResponse(null, store + " not supported")
		}
	}

	fun writeEntity(store: String?, key: String?, value: String?): OpResponse {
		when (store) {
			"mysql" -> return writeMySQL(mysqlRepo, key, value)
			"mongodb" -> return writeMongoDB(mongoRepo, key, value)
			else -> return OpResponse(null, store + " not supported")
		}
	}

	fun readMySQL(repo: SampleMysqlRepo, key: String?): OpResponse {
		val result = repo.findByUuid(key ?: "")
		if (result == null) {
			return OpResponse("Not Found", null)
		} else {
			return OpResponse(result.content, null)
		}
	}

	fun writeMySQL(repo: SampleMysqlRepo, key: String?, value: String?): OpResponse {
		repo.save(SampleMysql(key ?: "", value ?: ""))
		return OpResponse(value, null)
	}

	fun readMongoDB(repo: SampleMongoRepo, key: String?): OpResponse {
		val result = repo.findByUuid(key ?: "")
		if (result == null) {
			return OpResponse("Not Found", null)
		} else {
			return OpResponse(result.content, null)
		}
	}

	fun writeMongoDB(repo: SampleMongoRepo, key: String?, value: String?): OpResponse {
		repo.save(SampleMongo(key ?: "", value ?: ""))
		return OpResponse(value, null)
	}
}
