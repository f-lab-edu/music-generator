package com.simuel.musicgenerator.domain.usecase.account

import com.simuel.musicgenerator.domain.model.AccountLimitInfo
import com.simuel.musicgenerator.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountLimitsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): List<AccountLimitInfo> {
        return accountRepository.getAccountLimits()
    }
}