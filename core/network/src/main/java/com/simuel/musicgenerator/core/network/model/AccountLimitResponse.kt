package com.simuel.musicgenerator.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AccountLimitResponse(
    @SerialName("request_type")
    val requestType: String,
    @SerialName("limit")
    val limit: Int,
    @SerialName("used")
    val used: Int,
    @SerialName("left")
    val left: Int,
    @SerialName("date_from")
    val dateFrom: String,
    @SerialName("date_to")
    val dateTo: String
)