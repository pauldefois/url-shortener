package de.dkb.codingchallenge.urlshortener

import com.google.common.io.Resources
import com.google.common.net.HttpHeaders
import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.ActiveProfiles
import java.io.IOException
import java.net.URL

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractIntegrationTest {

    @LocalServerPort
    var servicePort: Int = 0

    @Autowired
    lateinit var jsonMessageConverter: MappingJackson2HttpMessageConverter

    @BeforeEach
    fun setupRestAssured() {
        RestAssured.port = servicePort

        val objectMapperConfig = ObjectMapperConfig()
        val jackson2ObjectMapperFactory = objectMapperConfig.jackson2ObjectMapperFactory { _, _ -> jsonMessageConverter.objectMapper }
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(jackson2ObjectMapperFactory)
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
    }

    @AfterEach
    fun tearDownRestAssured() {
        RestAssured.reset()
    }

    fun defaultRestAssured(): RequestSpecification = RestAssured.given().request().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())

    @Throws(IOException::class)
    fun resource(resource: String): String {
        val uri: URL = Resources.getResource(resource)
        return Resources.toString(uri, Charsets.UTF_8)
    }
}