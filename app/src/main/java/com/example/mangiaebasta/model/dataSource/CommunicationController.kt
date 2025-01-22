package com.example.mangiaebasta.model.dataSource

import android.net.Uri
import android.util.Log
import com.example.mangiaebasta.model.dto.ResponseError
import com.example.mangiaebasta.model.dto.UserDetailResponse
import com.example.mangiaebasta.model.dto.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object CommunicationController {
    private val TAG = CommunicationController::class.simpleName
    private const val BASE_URL = "https://develop.ewlab.di.unimi.it/mc/2425"
    var sid : String? = null
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    enum class HttpMethod {
        GET,
        POST,
        DELETE,
        PUT
    }

    suspend fun genericRequest(url: String, method: HttpMethod,
                               queryParameters: Map<String, Any> = emptyMap(),
                               requestBody: Any? = null) : HttpResponse {

        val urlUri = Uri.parse(url)
        val urlBuilder = urlUri.buildUpon()
        queryParameters.forEach { (key, value) ->
            urlBuilder.appendQueryParameter(key, value.toString())
        }
        val completeUrlString = urlBuilder.build().toString()
        Log.d(TAG, completeUrlString)
        val request: HttpRequestBuilder.() -> Unit = {
            requestBody?.let {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
        }

        try {
            val response = when (method) {
                HttpMethod.GET -> client.get(completeUrlString)
                HttpMethod.POST -> client.post(completeUrlString, request)
                HttpMethod.DELETE -> client.delete(completeUrlString)
                HttpMethod.PUT -> client.put(completeUrlString, request)
            }
            if (response.status.value == 200 || response.status.value == 204) {
                Log.d(TAG, response.status.value.toString())
                return response
            } else {
                val error = response.body<ResponseError>()
                throw Exception("HTTP Error: ${response.status.value}, Message: ${error.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in request: $e")
            throw e
        }
    }

    suspend fun createUser(): UserResponse {
        Log.d(TAG, "createUser")
        val url = "$BASE_URL/user"
        val httpResponse = genericRequest (url, HttpMethod.POST)
        val result : UserResponse = httpResponse.body()
        return result
    }

    suspend fun getUserDetail(sid: String, uid: Int): UserDetailResponse {
        Log.d(TAG, "getUserDetail")
        val url = "$BASE_URL/user/$uid"
        val queryParameters = mapOf("sid" to sid, "uid" to uid)
        val httpResponse = genericRequest (url, HttpMethod.GET, queryParameters)
        val result : UserDetailResponse = httpResponse.body()
        return result
    }

}