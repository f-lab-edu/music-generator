package com.simuel.musicgenerator.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RandomPromptResponse(
    @SerialName("prompt")
    val prompt: String
)