package de.dkb.codingchallenge.urlshortener.common.springdoc

import org.springframework.boot.context.properties.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties(prefix = "url-shortener.swagger")
class SpringDocConfigProperties {

    @NotBlank val packageName: String = "de.dkb.codingchallenge.urlshortener"
}