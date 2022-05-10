package demo.api

fun LogContext(request: Message, response: Message, errors: ArrayList<String>, receivedAt: String) {
    println("Started POST \"/api\" at $receivedAt")

    println("  Request: $request")
    println("  Response: $response")

    if (errors != null && errors.isNotEmpty()) {
        println("  Errors: $errors")
    }
    
    println("\n")
}
