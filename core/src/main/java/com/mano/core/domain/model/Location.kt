package com.mano.core.domain.model

data class Location(
    val id: Int,
    val name: String,
    val image: String,
    val link: String = "",
    val jesus: String = "",
    val channelId: String
)