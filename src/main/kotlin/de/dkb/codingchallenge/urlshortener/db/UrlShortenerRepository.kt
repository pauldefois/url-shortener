package de.dkb.codingchallenge.urlshortener.db

import de.dkb.codingchallenge.urlshortener.db.model.UrlShortenedEntity
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UrlShortenerRepository {

    fun findByHash(hash: String): Optional<UrlShortenedEntity>
}