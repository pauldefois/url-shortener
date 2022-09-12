package de.dkb.codingchallenge.urlshortener.service

import de.dkb.codingchallenge.urlshortener.service.model.UrlDto
import de.dkb.codingchallenge.urlshortener.service.model.UrlShortened
import java.util.Optional

interface UrlShortenerService {

    fun shortenUrl(url: UrlDto): UrlShortened

    fun getShortenedUrl(urlHash: String): Optional<UrlShortened>
}