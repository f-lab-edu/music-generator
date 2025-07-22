package com.simuel.musicgenerator.data.model

data class AccountLimitInfoDto(
    val requestType: String,
    val limit: Int,
    val used: Int,
    val left: Int,
    val dateFrom: String,
    val dateTo: String
)
