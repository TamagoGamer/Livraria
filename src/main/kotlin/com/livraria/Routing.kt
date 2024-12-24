package com.livraria

import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.Route
import com.google.cloud.firestore.Firestore
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString

fun Route.blogRoutes(firestore: Firestore) {
    route("/blog") {
        get {
            call.respondText("List of blog posts")
        }
        post {
            val requestBody = call.receive<String>()
            val blogPost = json.decodeFromString<BlogPost>(requestBody)
            // Salvar o blog post no Firebase
            createPost(firestore, blogPost)
            call.respond(HttpStatusCode.Created, json.encodeToString(blogPost))
        }
    }
}

fun createPost(firestore: Firestore, blogPost: BlogPost) {
    val post = mapOf(
        "title" to blogPost.title,
        "content" to blogPost.content,
        "createdAt" to System.currentTimeMillis(),
        "updatedAt" to System.currentTimeMillis()
    )
    firestore.collection("posts").add(post)
}


