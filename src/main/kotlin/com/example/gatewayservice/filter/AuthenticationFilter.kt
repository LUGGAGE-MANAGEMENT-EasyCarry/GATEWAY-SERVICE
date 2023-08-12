package com.example.gatewayservice.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AuthenticationFilter(private val validator:RouteValidator,private val restTemplate:RestTemplate) : AbstractGatewayFilterFactory<AuthenticationFilter.Config>(Config::class.java) {

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            if (validator.isSecured().test(exchange.request)) {
                // Token var mı yok mu kontrol et
                if (!exchange.request.headers.containsKey(org.springframework.http.HttpHeaders.AUTHORIZATION)) {
                    throw RuntimeException("missing authorization header")
                }

                var authHeader = exchange.request.headers[org.springframework.http.HttpHeaders.AUTHORIZATION]?.get(0)

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7)
                }
                try {
                    //Rest call to auth service
                      restTemplate.getForObject("http://localhost:7078/api/auth/validate?token="+authHeader,String::class.java)
                } catch (e: Exception) {
                    println("invalid access...!")
                    throw RuntimeException("unauthorized access to application")
                }
            }
            chain.filter(exchange)
        }
    }

    data class Config(val enabled: Boolean = true)
}