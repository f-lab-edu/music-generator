package com.simuel.musicgenerator.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SongResponse(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("music_file_path")
    val musicFilePath: String,
    @SerialName("wave_form_file_path")
    val waveFormFilePath: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("bpm")
    val bpm: Int? = null,
    @SerialName("key")
    val key: MusicKey? = null
)

@Serializable
data class MusicKey(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("active")
    val active: Boolean
)