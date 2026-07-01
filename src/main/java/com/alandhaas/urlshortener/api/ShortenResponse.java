package com.alandhaas.urlshortener.api;

public record ShortenResponse(
        String shortCode,
        String shortUrl,
        String longUrl
) {
}
