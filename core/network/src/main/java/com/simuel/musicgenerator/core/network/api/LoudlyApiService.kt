package com.simuel.musicgenerator.core.network.api

import com.simuel.musicgenerator.core.network.model.AccountLimitResponse
import com.simuel.musicgenerator.core.network.model.GenerateSongRequest
import com.simuel.musicgenerator.core.network.model.RandomPromptResponse
import com.simuel.musicgenerator.core.network.model.SongResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

internal interface LoudlyApiService {
    
    @GET("api/account/limits")
    suspend fun getAccountLimits(): List<AccountLimitResponse>
    
    @POST("api/ai/prompt/songs")
    suspend fun generateSong(
        @Body request: GenerateSongRequest
    ): SongResponse
    
    @GET("api/ai/prompt/random")
    suspend fun getRandomPrompt(): RandomPromptResponse
}
