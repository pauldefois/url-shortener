package de.dkb.codingchallenge.urlshortener.api.model

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
data class ApiUrlCreationRequest(
    @field: NotBlank val url: String,
    @field: NotBlank val customisedHash: String?
)