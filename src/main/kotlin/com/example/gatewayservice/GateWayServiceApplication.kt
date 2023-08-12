package com.example.gatewayservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class GateWayServiceApplication

fun main(args: Array<String>) {
	runApplication<GateWayServiceApplication>(*args)
}
