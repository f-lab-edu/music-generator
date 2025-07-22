package com.simuel.musicgenerator.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GenerateSongRequest(
    @SerialName("prompt")
    val prompt: String,
    @SerialName("duration")
    val duration: Int? = null,
    @SerialName("test")
    val test: Boolean? = null,
    @SerialName("structure_id")
    val structureId: Int? = null
)
