package de.dkb.codingchallenge.urlshortener.service.hashing

interface UrlHashingService {

    fun hashUrl(url: String): String
}