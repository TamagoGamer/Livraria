package com.livraria

import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import com.google.cloud.firestore.Firestore

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        initFirebase() // Inicializar Firebase
        val firestore = getFirestore() // Obter instância do Firestore
        module(firestore)
    }.start(wait = true)
}

fun Application.module(firestore: Firestore) {
    routing {
        blogRoutes(firestore)
        deletePostRoute(firestore) // Nova rota para deletar
    }
}
