package com.livraria

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import com.google.cloud.firestore.Firestore
import java.io.FileInputStream

fun initFirebase() {
    val serviceAccount = FileInputStream("src/main/resources/google-services.json")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://livraria-5a1e8.firebaseio.com")
        .build()

    FirebaseApp.initializeApp(options)
}

fun getFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

fun getFirestore(): Firestore = FirestoreClient.getFirestore()
