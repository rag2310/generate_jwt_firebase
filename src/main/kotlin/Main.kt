package org.rago
import com.google.auth.oauth2.GoogleCredentials
import java.io.FileInputStream
import java.nio.file.Paths

private fun getAccessToken(path:String): String{
    val serviceAccountPath = Paths.get(path).toAbsolutePath().toString()
    val googleCredentials :GoogleCredentials = GoogleCredentials
        .fromStream(FileInputStream(serviceAccountPath))
        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
    googleCredentials.refresh()
    return googleCredentials.accessToken.tokenValue

}

fun main(){
    println("[${getAccessToken("ikatech.json")}]")
}

