package com.simuel.musicgenerator.domain.model

data class SongGeneration(
    val prompt: String,
    val durationInSeconds: Int? = null,
    val isTest: Boolean = false,
    val structureId: Int? = null
)