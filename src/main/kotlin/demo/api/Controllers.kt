package demo.api

import com.google.gson.GsonBuilder
import java.time.Instant
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(private val mysqlRepo: SampleMysqlRepo) {

    @PostMapping("/api")
    fun ApiCallHandler(@RequestBody message: RequestMessage): String {

        val receivedAt = Instant.now().toString()
        println("\n\nReceived API message $message.message at $receivedAt")

        var responseMessage: String = ""

        when (message.message) {
            "Hello! How are you?" -> {
                responseMessage = "Hello! This is Kotlin Spring service."
            }
            "Please echo" -> {
                responseMessage = "Echo from Kotlin Spring service: " + message.value
            }
            "Read from database" -> {
                val result = readEntity("mysql", message.key)
                if (result == null) {
                    println("Not found key $message.key")
                } else {
                    responseMessage =
                            "Kotlin Spring service: successfully read from database, value: " +
                                    result
                }
            }
            "Write to database" -> {
                if (message.key == null || message.value == null) {
                    println("Invalide key/value: key $message.key value $message.value")
                } else {
                    writeEntity("mysql", message.key, message.value)
                    responseMessage = "Kotlin Spring service: successfully write to database"
                }
            }
        }

        val gson = GsonBuilder().create()
        val response = ResponseMessage(receivedAt, Instant.now().toString(), responseMessage)
        println("Finish handling API message at $response.returnTime")

        return gson.toJson(response)
    }

    fun readEntity(store: String?, key: String?): String? {
        when (store) {
            "mysql" -> {
                val result = mysqlRepo.findByUuid(key ?: "")
                if (result == null) {
                    return null
                }
                return result.content
            }
            else -> return null
        }
    }

    fun writeEntity(store: String?, key: String?, value: String?) {
        when (store) {
            "mysql" -> {
                mysqlRepo.save(SampleMysql(key ?: "", value ?: ""))
            }
        }
    }
}
