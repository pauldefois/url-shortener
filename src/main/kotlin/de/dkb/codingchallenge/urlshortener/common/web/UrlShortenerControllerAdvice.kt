package de.dkb.codingchallenge.urlshortener.common.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.IllegalArgumentException

@ControllerAdvice
class UrlShortenerControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(UrlNotFoundException::class)
    fun handleInvalidArgument(e: UrlNotFoundException) = ResponseEntity(HttpStatus.NOT_FOUND.reasonPhrase, createContentTypeJsonHeader(), HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleInvalidArgument(e: IllegalArgumentException) = ResponseEntity(e.message, createContentTypeJsonHeader(), HttpStatus.BAD_REQUEST)

    private fun createContentTypeJsonHeader(): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        return headers
    }
}