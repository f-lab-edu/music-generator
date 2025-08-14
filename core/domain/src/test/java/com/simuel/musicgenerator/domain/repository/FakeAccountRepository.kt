package com.simuel.musicgenerator.domain.repository

import com.simuel.musicgenerator.domain.model.AccountLimitInfo

class FakeAccountRepository : AccountRepository {
    private var limits = emptyList<AccountLimitInfo>()
    
    fun setLimits(limits: List<AccountLimitInfo>) {
        this.limits = limits
    }
    
    override suspend fun getAccountLimits(): List<AccountLimitInfo> = limits
}