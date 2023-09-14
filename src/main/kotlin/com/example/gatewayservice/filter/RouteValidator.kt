package com.example.gatewayservice.filter

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import java.util.function.Predicate

@Component
class RouteValidator {
companion object{
    val openApiEndPoints= listOf("api/auth/authenticate","api/auth/register")
}
    fun isSecured():Predicate<ServerHttpRequest> = Predicate {
        request -> openApiEndPoints.none { uri -> request.uri.path.contains(uri) }
    }
}