package com.simuel.musicgenerator.data.model

data class SongGenerationDto(
    val prompt: String,
    val durationInSeconds: Int? = null,
    val isTest: Boolean = false,
    val structureId: Int? = null
)
