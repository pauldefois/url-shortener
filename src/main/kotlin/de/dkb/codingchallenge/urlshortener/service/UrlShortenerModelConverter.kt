package de.dkb.codingchallenge.urlshortener.service

import de.dkb.codingchallenge.urlshortener.db.model.UrlShortenedEntity
import de.dkb.codingchallenge.urlshortener.service.model.UrlShortened
import org.springframework.stereotype.Component

@Component
class UrlShortenerModelConverter {

    fun convertToUrlShortened(entity: UrlShortenedEntity): UrlShortened =
        UrlShortened(
            entity.id,
            entity.url,
            entity.hash,
            entity.creationDateTime
        )
}