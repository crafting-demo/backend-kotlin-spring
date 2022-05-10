package demo.api

data class OpResponse(
    var value: String?,
    var errors: String?
)

@Suppress("UNUSED_PARAMETER")
fun readEntity(store: String?, _key: String?): OpResponse {
    return OpResponse(null, store + " client not implemented yet")
}

@Suppress("UNUSED_PARAMETER")
fun writeEntity(store: String?, _key: String?, value: String?): OpResponse {
    return OpResponse(value, store + " client not implemented yet")
}
