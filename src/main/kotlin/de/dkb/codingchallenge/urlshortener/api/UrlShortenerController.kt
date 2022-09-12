package de.dkb.codingchallenge.urlshortener.api

import de.dkb.codingchallenge.urlshortener.api.model.ApiUrlShortened
import de.dkb.codingchallenge.urlshortener.api.model.ApiUrlCreationRequest
import de.dkb.codingchallenge.urlshortener.service.UrlShortenerService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Validated
@RequestMapping("/")
@RestController
class UrlShortenerController(
    val urlShortenerService: UrlShortenerService
) {

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun shortenAndSaveUrl(@RequestBody @Valid url: ApiUrlCreationRequest): ApiUrlShortened {
        TODO()
    }

    @GetMapping("/{hash}")
    fun getAndRedirect(@Valid @PathVariable hash: String) {
        TODO()
    }
}