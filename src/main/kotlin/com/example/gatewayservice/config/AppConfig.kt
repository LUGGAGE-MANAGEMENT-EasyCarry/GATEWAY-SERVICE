package com.example.gatewayservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AppConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().build()
    }
    @Bean
    fun template():RestTemplate{
        return  RestTemplate()
    }

}