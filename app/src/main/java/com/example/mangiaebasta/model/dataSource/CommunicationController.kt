package com.example.mangiaebasta.model.dataSource

import android.net.Uri
import android.util.Log
import com.example.mangiaebasta.model.dataClasses.APILocation
import com.example.mangiaebasta.model.dataClasses.BuyOrderRequest
import com.example.mangiaebasta.model.dataClasses.Menu
import com.example.mangiaebasta.model.dataClasses.MenuDetails
import com.example.mangiaebasta.model.dataClasses.MenuImage
import com.example.mangiaebasta.model.dataClasses.Order
import com.example.mangiaebasta.model.dataClasses.ResponseError
import com.example.mangiaebasta.model.dataClasses.User
import com.example.mangiaebasta.model.dataClasses.UserDetail
import com.example.mangiaebasta.model.dataClasses.UserUpdateParams
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


class CommunicationController {
    companion object {
        private val BASE_URL = "https://develop.ewlab.di.unimi.it/mc/2425"
        private val TAG = CommunicationController::class.simpleName
    }


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

    suspend fun genericRequest(
        url: String, method: HttpMethod,
        queryParameters: Map<String, Any> = emptyMap(),
        bodyParams: Any? = null
    ): HttpResponse {

        val urlUri = Uri.parse(url)
        val urlBuilder = urlUri.buildUpon()
        queryParameters.forEach { (key, value) ->
            urlBuilder.appendQueryParameter(key, value.toString())
        }
        val completeUrlString = urlBuilder.build().toString()
        Log.d(TAG, completeUrlString)
        val request: HttpRequestBuilder.() -> Unit = {
            bodyParams?.let {
                contentType(ContentType.Application.Json)
                setBody(bodyParams)
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
                throw Exception(error.message)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in request: $e")
            throw e
        }
    }

    // User data management
    suspend fun registerUser(): User {
        Log.d(TAG, "registerUser")
        val url = "$BASE_URL/user"
        val httpResponse = genericRequest(url, HttpMethod.POST)
        val result: User = httpResponse.body()
        return result
    }

    suspend fun getUserData(sid: String, uid: Int): UserDetail {
        val url = "$BASE_URL/user/$uid"
        val queryParameters = mapOf("sid" to sid, "uid" to uid)
        Log.d(TAG, "getUserData + query params: $queryParameters")
        val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
        val result: UserDetail = httpResponse.body()
        return result
    }

    suspend fun putUserData(sid: String, uid: Int, updateData: UserUpdateParams): HttpResponse {
        val url = "$BASE_URL/user/$uid"
        val queryParameters = mapOf("sid" to sid)
        Log.d(TAG, "putUserData + query params: $queryParameters")
        val httpResponse = genericRequest(
            url, HttpMethod.PUT, bodyParams = updateData,
            queryParameters = queryParameters
        )
        return httpResponse
    }

    // Menu data management
    suspend fun getNearbyMenus(lat: Double, lng: Double, sid: String): List<Menu> {
        val url = "$BASE_URL/menu"
        val queryParameters = mapOf("lat" to lat, "lng" to lng, "sid" to sid)
        Log.d(TAG, "getNearbyMenus + query params: $queryParameters")
        val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
        val result: List<Menu> = httpResponse.body()
        return result
    }

    suspend fun getMenuImage(mid: Int, sid: String): MenuImage {
        val url = "$BASE_URL/menu/$mid/image"
        val queryParameters = mapOf("sid" to sid)
        Log.d(TAG, "getMenuImage + query params: $queryParameters")
        val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
        val result: MenuImage = httpResponse.body()
        return result
    }

    suspend fun getMenuDetail(mid: Int, lat: Double, lng: Double, sid: String): MenuDetails {
        val url = "$BASE_URL/menu/$mid"
        val queryParameters = mapOf("lat" to lat, "lng" to lng, "sid" to sid)
        Log.d(TAG, "getMenuDetail + query params: $queryParameters")
        val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
        val result: MenuDetails = httpResponse.body()
        return result
    }

    suspend fun buyMenu(sid: String, deliveryLocation: APILocation, mid: Int): Order {
        val url = "$BASE_URL/menu/$mid/buy"
        val bodyParams = BuyOrderRequest(sid, deliveryLocation)
        Log.d(TAG, "buyMenu + body params: $bodyParams")
        val httpResponse = genericRequest(url, HttpMethod.POST, bodyParams = bodyParams)
        val result: Order = httpResponse.body()
        return result
    }

    // Order data management
    suspend fun getOrderDetail(oid: Int, sid: String): Order {
        val url = "$BASE_URL/order/$oid"
        val queryParameters = mapOf("oid" to oid, "sid" to sid)
        Log.d(TAG, "getOrderDetail + query params: $queryParameters")
        val httpResponse = genericRequest(url, HttpMethod.GET, queryParameters)
        val result: Order = httpResponse.body()
        return result
    }


}