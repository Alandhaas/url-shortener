package com.alandhaas.urlshortener.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.alandhaas.urlshortener.api.ShortenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:service-test;DB_CLOSE_DELAY=-1",
        "app.base-url=http://sho.rt"
})
class UrlShortenerServiceTests {

    @Autowired
    private UrlShortenerService service;

    @Test
    void shortensValidatedUrl() {
        ShortenResponse response = service.shorten(" https://example.com/docs/../guide ");

        assertThat(response.longUrl()).isEqualTo("https://example.com/guide");
        assertThat(response.shortCode()).isEqualTo("1");
        assertThat(response.shortUrl()).isEqualTo("http://sho.rt/1");
    }

    @Test
    void reusesExistingShortCodeForSameLongUrl() {
        ShortenResponse first = service.shorten("https://example.com/reused");
        ShortenResponse second = service.shorten("https://example.com/reused");

        assertThat(second.shortCode()).isEqualTo(first.shortCode());
        assertThat(second.shortUrl()).isEqualTo(first.shortUrl());
    }
}
