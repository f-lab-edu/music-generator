package com.simuel.musicgenerator.data.model

data class SongDto(
    val id: String,
    val title: String,
    val duration: Int,
    val musicFilePath: String,
    val waveFormFilePath: String,
    val createdAt: String,
    val bpm: Int? = null,
    val musicKeyId: Int? = null,
    val musicKeyName: String? = null,
    val musicKeyActive: Boolean? = null
)