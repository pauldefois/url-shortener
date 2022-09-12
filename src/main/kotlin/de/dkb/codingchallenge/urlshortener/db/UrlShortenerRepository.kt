package de.dkb.codingchallenge.urlshortener.db

import de.dkb.codingchallenge.urlshortener.db.model.UrlShortenedEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UrlShortenerRepository : JpaRepository<UrlShortenedEntity, Long> {

    fun findByHash(hash: String): Optional<UrlShortenedEntity>
}