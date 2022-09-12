package de.dkb.codingchallenge.urlshortener.api.model

import java.time.LocalDateTime

data class ApiUrlShortened(
    val longUrl: String,
    val shortUrl: String,
    val hash: String,
    val creationDateTime: LocalDateTime
)
