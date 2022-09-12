package de.dkb.codingchallenge.urlshortener.service

import de.dkb.codingchallenge.urlshortener.db.UrlShortenerRepository
import de.dkb.codingchallenge.urlshortener.db.model.UrlShortenedEntity
import de.dkb.codingchallenge.urlshortener.service.hashing.UrlHashingService
import de.dkb.codingchallenge.urlshortener.service.model.UrlDto
import de.dkb.codingchallenge.urlshortener.service.model.UrlShortened
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.util.Optional

@Service
class DefaultUrlShortenerService(
    val repository: UrlShortenerRepository,
    val urlHashingService: UrlHashingService,
    val modelConverter: UrlShortenerModelConverter
) : UrlShortenerService {

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackForClassName = ["java.lang.Throwable"])
    override fun shortenUrl(url: UrlDto): UrlShortened {
        val hash = getAndCheckCustomisedHash(url.customisedHash) ?: urlHashingService.hashUrl(url.url)
        return repository.save(
            UrlShortenedEntity(
                null,
                url.url,
                hash,
                LocalDateTime.now()
            )
        ).let {
            modelConverter.convertToUrlShortened(it)
        }
    }

    private fun getAndCheckCustomisedHash(customisedHash: String?): String? {
        if (customisedHash != null) {
            if (customisedHash.isBlank()) {
                throw IllegalArgumentException("The customised hash is empty")
            } else if (repository.findByHash(customisedHash).isPresent) {
                throw IllegalArgumentException("The customised hash already exists")
            }
        }
        return customisedHash
    }

    override fun getShortenedUrl(urlHash: String): Optional<UrlShortened> =
        repository.findByHash(urlHash).map {
            modelConverter.convertToUrlShortened(it)
        }
}