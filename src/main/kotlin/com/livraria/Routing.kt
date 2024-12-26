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
            try {
                val requestBody = call.receive<String>()
                println("Request Body: $requestBody")
                val blogPost = json.decodeFromString<BlogPost>(requestBody)
                println("Decoded BlogPost: $blogPost")
                createPost(firestore, blogPost)
                call.respond(HttpStatusCode.Created, json.encodeToString(blogPost))
            } catch (e: Exception) {
                println("Error in POST /blog: ${e.message}")
                e.printStackTrace() // Para imprimir o stack trace do erro
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
            }
        }
    }
}

fun deletePost(firestore: Firestore, postId: String) {
    try {
        firestore.collection("posts").document(postId).delete().get()
        println("Post with ID $postId deleted successfully")
    } catch (e: Exception) {
        println("Error deleting post with ID $postId: ${e.message}")
        throw e
    }
}

fun createPost(firestore: Firestore, blogPost: BlogPost) {
    try {
        val currentTime = System.currentTimeMillis()
        val post = mapOf(
            "authorId" to blogPost.authorId,
            "comments" to blogPost.comments,
            "content" to blogPost.content,
            "createdAt" to currentTime,
            "likes" to blogPost.likes,
            "title" to blogPost.title,
            "updatedAt" to currentTime
        )
        println("Saving post: $post")
        firestore.collection("posts").add(post)
        println("Post saved successfully!")
    } catch (e: Exception) {
        println("Error saving post: ${e.message}")
        e.printStackTrace()
        throw e
    }
}

fun Route.deletePostRoute(firestore: Firestore) {
    route("/blog") {
        delete("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid post ID")
                return@delete
            }
            try {
                deletePost(firestore, id)
                call.respond(HttpStatusCode.OK, "Post deleted successfully")
            } catch (e: Exception) {
                println("Error deleting post: ${e.message}")
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
            }
        }
    }
}



