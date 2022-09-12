package de.dkb.codingchallenge.urlshortener.service.hashing

import com.google.common.hash.Hashing
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class UrlMurmur32HashingService : UrlHashingService {

    override fun hashUrl(url: String): String = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString()
}