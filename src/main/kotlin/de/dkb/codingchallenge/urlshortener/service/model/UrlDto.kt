package de.dkb.codingchallenge.urlshortener.service.model

data class UrlDto(
    val url: String,
    val customisedHash: String?
)