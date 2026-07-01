package com.alandhaas.urlshortener.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.alandhaas.urlshortener.api.ShortenResponse;
import com.alandhaas.urlshortener.config.UrlShortenerProperties;
import org.junit.jupiter.api.Test;

class UrlShortenerServiceTests {

    private final UrlShortenerProperties properties = new UrlShortenerProperties();
    private final UrlShortenerService service = new UrlShortenerService(
            new Base62Encoder(),
            new UrlValidator(),
            properties
    );

    @Test
    void shortensValidatedUrl() {
        properties.setBaseUrl("http://sho.rt");

        ShortenResponse response = service.shorten(" https://example.com/docs/../guide ");

        assertThat(response.longUrl()).isEqualTo("https://example.com/guide");
        assertThat(response.shortCode()).isEqualTo("1");
        assertThat(response.shortUrl()).isEqualTo("http://sho.rt/1");
    }
}
