package com.simuel.musicgenerator.core.network.exception

class ApiException(
    val code: Int,
    val error: String,
    val errorDescription: String,
    val payload: String? = null
) : Exception("API Error: $error - $errorDescription")