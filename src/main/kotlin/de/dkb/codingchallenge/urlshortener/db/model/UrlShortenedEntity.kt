package de.dkb.codingchallenge.urlshortener.db.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "url")
class UrlShortenedEntity(

    @Id
    @GeneratedValue(generator = "URL_SEQ_ID", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "URL_SEQ_ID", initialValue = 1, allocationSize = 10)
    var id: Long?,

    val url: String,
    val hash: String,
    val creationDateTime: LocalDateTime
)