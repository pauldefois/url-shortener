package de.dkb.codingchallenge.urlshortener.service.model

import java.time.LocalDateTime

data class UrlShortened(
    val id: Long?,
    val url: String,
    val hash: String,
    val creationDateTime: LocalDateTime
)