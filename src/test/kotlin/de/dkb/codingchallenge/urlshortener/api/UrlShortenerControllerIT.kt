package de.dkb.codingchallenge.urlshortener.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import de.dkb.codingchallenge.urlshortener.AbstractIntegrationTest
import de.dkb.codingchallenge.urlshortener.api.model.ApiUrlShortened
import de.dkb.codingchallenge.urlshortener.common.db.TruncateDatabaseHelper
import de.dkb.codingchallenge.urlshortener.db.UrlShortenerRepository
import de.dkb.codingchallenge.urlshortener.db.model.UrlShortenedEntity
import de.dkb.codingchallenge.urlshortener.service.hashing.UrlHashingService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class UrlShortenerControllerIT : AbstractIntegrationTest() {

    @Autowired
    lateinit var truncateDatabaseHelper: TruncateDatabaseHelper

    @Autowired
    lateinit var urlHashingService: UrlHashingService

    @Autowired
    lateinit var urlShortenerRepository: UrlShortenerRepository

    @AfterEach
    fun setUp() {
        truncateDatabaseHelper.truncateTables()
    }

    @Test
    fun `when creating a URL, then save it to the DB`() {
        val response = defaultRestAssured()
            .body(resource("urlshortener/api/create-url.json"))
            .post("/create")

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED.value())
        val expectedHash = urlHashingService.hashUrl("https://www.google.com")
        assertApiUrlShortened(response.prettyPrint(), "https://www.google.com", "http://localhost:$servicePort/$expectedHash", expectedHash)
        assertDbUrlShortened("https://www.google.com", expectedHash)
    }

    @Test
    fun `when creating a URL with a customised hash, then save it to the DB`() {
        val response = defaultRestAssured()
            .body(resource("urlshortener/api/create-url-with-customised-hash.json"))
            .post("/create")

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED.value())
        assertApiUrlShortened(response.prettyPrint(), "https://www.youtube.com", "http://localhost:$servicePort/bonjour", "bonjour")
        assertDbUrlShortened("https://www.youtube.com", "bonjour")
    }

    @Test
    fun `when creating a URL with an empty customised hash, then return an error`() {
        val response = defaultRestAssured()
                .body(resource("urlshortener/api/create-url-with-empty-customised-hash.json"))
                .post("/create")

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.prettyPrint().trim()).isEqualTo("The customised hash is empty")
    }

    @Test
    fun `when creating a URL with an customised hash that exists in the DB, then return an error`() {
        urlShortenerRepository.save(UrlShortenedEntity(
            null,
            "testURL",
            "bonjour",
            LocalDateTime.now()
        ))
        val response = defaultRestAssured()
            .body(resource("urlshortener/api/create-url-with-customised-hash.json"))
            .post("/create")

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value())
        Assertions.assertThat(response.prettyPrint().trim()).isEqualTo("The customised hash already exists")
    }

    @Test
    fun `when getting an existing URL, then redirect to it`() {
        urlShortenerRepository.save(UrlShortenedEntity(
            null,
            "https://mail.google.com",
            "1234qwer",
            LocalDateTime.now()
        ))
        defaultRestAssured()
            .redirects().follow(false)
            .get("/1234qwer")
            .then()
                .statusCode(303)
                .header("Location", "https://mail.google.com")
    }

    @Test
    fun `when getting an non existing URL, then return a 404`() {
        defaultRestAssured()
            .get("/hashhash")
            .then()
                .statusCode(404)
    }

    private fun assertApiUrlShortened(response: String, longUrl: String, shortUrl: String, hash: String) {
        val urlShortened: ApiUrlShortened = objectMapper().readValue(response)
        Assertions.assertThat(urlShortened.longUrl).isEqualTo(longUrl)
        Assertions.assertThat(urlShortened.shortUrl).isEqualTo(shortUrl)
        Assertions.assertThat(urlShortened.hash).isEqualTo(hash)
        Assertions.assertThat(urlShortened.creationDateTime).isNotNull
    }

    private fun assertDbUrlShortened(longUrl: String, hash: String) {
        val entity = urlShortenerRepository.findAll().first()
        Assertions.assertThat(entity).isNotNull
        Assertions.assertThat(entity.id).isNotNull
        Assertions.assertThat(entity.url).isEqualTo(longUrl)
        Assertions.assertThat(entity.hash).isEqualTo(hash)
        Assertions.assertThat(entity.creationDateTime).isNotNull
    }

    private fun objectMapper(): ObjectMapper {
        val om = jacksonObjectMapper()
                .registerModule(JavaTimeModule())
                .registerModule(Jdk8Module())
                .registerModule(ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
                .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
        om
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
            .dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        return om
    }

}