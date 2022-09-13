package de.dkb.codingchallenge.urlshortener.common.db

import de.dkb.codingchallenge.urlshortener.db.UrlShortenerRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class TruncateDatabaseHelper(
    private val urlShortenerRepository: UrlShortenerRepository
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackForClassName = ["java.lang.Throwable"])
    fun truncateTables() {
        try {
            urlShortenerRepository.deleteAll()
        } catch (t: Throwable) {
            truncateTables()
        }
    }
}