package demo.api

data class RequestMessage(
        var callTime: String,
        var target: String,
        var message: String,
        var key: String?,
        var value: String?,
)

data class ResponseMessage(
        var receivedTime: String,
        var returnTime: String,
        var message: String,
)
