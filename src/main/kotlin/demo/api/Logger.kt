package demo.api

fun LogContext(request: String, response: String, errors: ArrayList<String>, receivedAt: String) {
    println("Started POST \"/api\" at $receivedAt")

    println("  Request: $request")
    println("  Response: $response")

    if (errors.isNotEmpty()) {
        println("  Errors: $errors")
    }

    println("\n")
}
