package com.example.gatewayservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GateWayServiceApplication

fun main(args: Array<String>) {
	runApplication<GateWayServiceApplication>(*args)
}
