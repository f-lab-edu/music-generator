package com.simuel.musicgenerator.domain.repository

import com.simuel.musicgenerator.domain.model.AccountLimitInfo

interface AccountRepository {
    suspend fun getAccountLimits(): List<AccountLimitInfo>
}