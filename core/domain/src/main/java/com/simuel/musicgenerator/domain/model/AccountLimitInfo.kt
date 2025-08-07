package com.simuel.musicgenerator.domain.model

data class AccountLimitInfo(
    val requestType: String,
    val limit: Int,
    val used: Int,
    val left: Int,
    val dateFrom: String,
    val dateTo: String
)