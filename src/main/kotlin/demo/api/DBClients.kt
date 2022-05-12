package demo.api

data class OpResponse(
    var value: String?,
    var errors: String?
)

fun readEntity(store: String?, key: String?): OpResponse {
    when (store) {
        "mysql" -> return readMySQL(key)
        "mongodb" -> return readMongoDB(key)
        else -> return OpResponse(null, store + " not supported")
    }
}

fun writeEntity(store: String?, key: String?, value: String?): OpResponse {
    when (store) {
        "mysql" -> return writeMySQL(key, value)
        "mongodb" -> return writeMongoDB(key, value)
        else -> return OpResponse(null, store + " not supported")
    }
}

fun readMySQL(key: String?): OpResponse {
    return OpResponse(key, null)
}

fun writeMySQL(key: String?, value: String?): OpResponse {
    return OpResponse(value, null)
}

fun readMongoDB(key: String?): OpResponse {
    return OpResponse(key, null)
}

fun writeMongoDB(key: String?, value: String?): OpResponse {
    return OpResponse(value, null)
}
