package de.dkb.codingchallenge.urlshortener.common.springdoc

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@AutoConfigureOrder
@EnableConfigurationProperties(SpringDocConfigProperties::class)
@ComponentScan(basePackageClasses = [SpringDocConfiguration::class])
class SpringDocConfiguration {

    @Bean
    fun backendApi(config: SpringDocConfigProperties): GroupedOpenApi =
        GroupedOpenApi.builder()
            .group("api")
            .packagesToScan(config.packageName)
            .build()

    @Bean
    fun apiInfo(): OpenAPI {
        val info = Info()
        info.title = "URL shortener"
        info.version = "1.0"
        info.description = "Coding challenge for DKB"
        return OpenAPI().info(info)
    }
}