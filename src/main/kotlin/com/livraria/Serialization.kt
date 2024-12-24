package com.livraria

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class BlogPost(val id: String, val title: String, val content: String)

@Serializable
data class Comment(val id: String, val postId: String, val content: String, val authorId: String, val createdAt: Long, val updatedAt: Long)

val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

