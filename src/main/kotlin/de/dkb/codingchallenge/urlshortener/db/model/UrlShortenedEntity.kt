package de.dkb.codingchallenge.urlshortener.db.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class UrlShortenedEntity(

    @Id
    var id: String?,

    var url: String,
    var hash: String,
    var creationDateTime: LocalDateTime,
)