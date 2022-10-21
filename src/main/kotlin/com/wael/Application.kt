package com.wael

import com.wael.database.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.wael.plugins.*

fun main() {
    embeddedServer(Netty, port = 8090, host = "0.0.0.0") {
        DatabaseFactory.init()

        configureRouting()
        configureSerialization()
        configureMonitoring()
        configureSecurity()
    }.start(wait = true)
}
