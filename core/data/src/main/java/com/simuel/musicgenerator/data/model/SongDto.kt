package com.simuel.musicgenerator.data.model

import java.time.LocalDateTime

data class SongDto(
    val id: String,
    val title: String,
    val durationInMillis: Int,
    val musicFileUrl: String,
    val waveFormFileUrl: String,
    val createdAt: LocalDateTime,
    val bpm: Int? = null,
    val key: MusicKeyDto? = null
)

data class MusicKeyDto(
    val id: Int,
    val name: String,
    val isActive: Boolean
)
