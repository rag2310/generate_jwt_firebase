package org.rago
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Paths

private val mapper = jacksonObjectMapper()

private fun getAccessToken(path:String): String{
    val serviceAccountPath = Paths.get(path).toAbsolutePath().toString()
    val googleCredentials :GoogleCredentials = GoogleCredentials
        .fromStream(FileInputStream(serviceAccountPath))
        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
    googleCredentials.refresh()
    println("[${googleCredentials.accessToken.expirationTime}]")
    return googleCredentials.accessToken.tokenValue

}

private fun getProjectId(path: String): String =
    mapper.readTree(File(path)).get("project_id").asText()

fun sendPush(
    serviceAccountPath: String,
    deviceToken: String,
    title: String,
    body: String,
    data: Map<String, String> = emptyMap()
): String {
    val projectId = getProjectId(serviceAccountPath)
    val accessToken = getAccessToken(serviceAccountPath)

    val message = mapOf(
        "message" to mapOf(
            "token" to deviceToken,
            "notification" to mapOf("title" to title, "body" to body),
            "data" to data
        )
    )

    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://fcm.googleapis.com/v1/projects/$projectId/messages:send"))
        .header("Authorization", "Bearer $accessToken")
        .header("Content-Type", "application/json; charset=UTF-8")
        .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(message)))
        .build()

    val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
    println("[${response.statusCode()}] ${response.body()}")
    return response.body()
}

fun main(){
    sendPush(
        serviceAccountPath = "ehhoh.json",
        deviceToken = "",
        title = "Prueba",
        body = "Push de prueba desde GenerateJWT",
        data = mapOf("source" to "GenerateJWT")
    )
}
