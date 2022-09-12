package de.dkb.codingchallenge.urlshortener.api

import de.dkb.codingchallenge.urlshortener.api.model.ApiUrlShortened
import de.dkb.codingchallenge.urlshortener.api.model.ApiUrlCreationRequest
import de.dkb.codingchallenge.urlshortener.common.web.UrlNotFoundException
import de.dkb.codingchallenge.urlshortener.service.UrlShortenerService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@Tag(name = "UrlShortener", description = "Shorten URLs or redirect to them")
@Validated
@RequestMapping("/")
@RestController
class UrlShortenerController(
    val urlShortenerService: UrlShortenerService,
    val modelConverter: UrlShortenerApiModelConverter
) {

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    fun shortenAndSaveUrl(request: HttpServletRequest, @RequestBody @Valid url: ApiUrlCreationRequest): ApiUrlShortened =
        urlShortenerService.shortenUrl(
            modelConverter.convertToUrlDto(url)
        ).let {
            modelConverter.convertToApiUrlShortened(it, request)
        }

    @GetMapping("/{hash}")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    fun getAndRedirect(response: HttpServletResponse, @Valid @PathVariable hash: String) {
        val url = urlShortenerService.getShortenedUrl(hash)
        if (url.isPresent) {
            response.setHeader("location", url.get().url)
        } else {
            throw UrlNotFoundException("The URL with hash $hash was not found")
        }
    }
}