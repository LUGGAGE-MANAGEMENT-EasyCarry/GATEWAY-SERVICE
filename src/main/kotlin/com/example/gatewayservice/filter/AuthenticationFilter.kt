package com.example.gatewayservice.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AuthenticationFilter(private val validator: RouteValidator, private val restTemplate: RestTemplate) :
    AbstractGatewayFilterFactory<AuthenticationFilter.Config>(Config::class.java) {

    override fun apply(config: Config): GatewayFilter {
        return  GatewayFilter { exchange, chain ->
            if (hasRouteInValidator(exchange.request)) {
                doesntHaveAuthorizationHeader(exchange.request)
                val authHeader = exchange.request.headers[org.springframework.http.HttpHeaders.AUTHORIZATION]?.get(0)
                getToken(authHeader)
                callAuthServiceToValidateToken(restTemplate, authHeader)
            }
            chain.filter(exchange)
        }
    }

    private fun hasRouteInValidator(request: ServerHttpRequest): Boolean {
        return if (validator.isSecured().test(request)) true else throw RuntimeException("The app doesnt have any token")
    }

    private fun callAuthServiceToValidateToken(restTemplate: RestTemplate, authHeader: String?) {
        restTemplate.getForObject(AUTH_REQUEST_URL+authHeader, String::class.java)
            ?: throw RuntimeException("Invalid Access\nUnauthorized access to application")
    }

    private fun doesntHaveAuthorizationHeader(request: ServerHttpRequest) {
        if (!request.headers.containsKey(org.springframework.http.HttpHeaders.AUTHORIZATION)) {
            throw RuntimeException("Method doesnt have a auth header")
        }
    }

    private fun getToken(authHeader: String?) {
        if (authHeader?.startsWith("Bearer ") == true && authHeader.isNotEmpty()) authHeader.substring(7)
    }

    companion object{
        private const val AUTH_REQUEST_URL="http://localhost:7078/api/auth/validate?token="
    }

    data class Config(val enabled: Boolean = true)
}