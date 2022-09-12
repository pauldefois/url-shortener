package de.dkb.codingchallenge.urlshortener.api

import de.dkb.codingchallenge.urlshortener.api.model.ApiUrlCreationRequest
import de.dkb.codingchallenge.urlshortener.api.model.ApiUrlShortened
import de.dkb.codingchallenge.urlshortener.service.model.UrlDto
import de.dkb.codingchallenge.urlshortener.service.model.UrlShortened
import org.springframework.stereotype.Component
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.servlet.http.HttpServletRequest

@Component
class UrlShortenerApiModelConverter {

    fun convertToUrlDto(url: ApiUrlCreationRequest): UrlDto =
        UrlDto(url.url, url.customisedHash)

    fun convertToApiUrlShortened(url: UrlShortened, request: HttpServletRequest): ApiUrlShortened =
        ApiUrlShortened(
            url.url,
            getFullAppUri(request),
            url.hash,
            url.creationDateTime
        )

    private fun getFullAppUri(request: HttpServletRequest): String =
        ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString()
}