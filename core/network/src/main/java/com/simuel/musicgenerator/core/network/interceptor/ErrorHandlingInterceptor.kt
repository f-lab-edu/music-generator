package com.simuel.musicgenerator.core.network.interceptor

import com.simuel.musicgenerator.core.network.exception.ApiException
import com.simuel.musicgenerator.core.network.model.ErrorResponse
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ErrorHandlingInterceptor(
    private val json: Json
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        
        // Handle error responses for all endpoints
        if (!response.isSuccessful) {
            val responseBody = response.body?.string()
            
            // If no body, throw generic API exception
            if (responseBody.isNullOrEmpty()) {
                throw ApiException(
                    code = response.code,
                    error = "http_error_${response.code}",
                    errorDescription = response.message.ifEmpty { "HTTP ${response.code} Error" },
                    payload = null
                )
            }
            
            try {
                // Try to parse the error response
                val errorResponse = json.decodeFromString<ErrorResponse>(responseBody)
                
                // Throw custom exception with parsed error details
                throw ApiException(
                    code = response.code,
                    error = errorResponse.error,
                    errorDescription = errorResponse.errorDescription,
                    payload = errorResponse.payload
                )
            } catch (e: Exception) {
                // If parsing fails or it's not our ApiException, throw generic error
                if (e is ApiException) throw e
                
                // If JSON parsing failed, throw a generic API exception
                throw ApiException(
                    code = response.code,
                    error = "parse_error",
                    errorDescription = "Failed to parse error response: $responseBody",
                    payload = responseBody
                )
            }
        }
        
        return response
    }
}
