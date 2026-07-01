package com.alandhaas.urlshortener.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alandhaas.urlshortener.api.ShortenResponse;
import com.alandhaas.urlshortener.cache.RedirectCache;
import com.alandhaas.urlshortener.config.UrlShortenerProperties;
import com.alandhaas.urlshortener.domain.UrlMapping;
import com.alandhaas.urlshortener.repository.UrlMappingRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
        assertThat(response.shortCode()).isNotBlank();
        assertThat(response.shortUrl()).isEqualTo("http://sho.rt/" + response.shortCode());
    }

    @Test
    void reusesExistingShortCodeForSameLongUrl() {
        ShortenResponse first = service.shorten("https://example.com/reused");
        ShortenResponse second = service.shorten("https://example.com/reused");

        assertThat(second.shortCode()).isEqualTo(first.shortCode());
        assertThat(second.shortUrl()).isEqualTo(first.shortUrl());
    }

    @Test
    void findsLongUrlByShortCode() {
        ShortenResponse response = service.shorten("https://example.com/redirect-target");

        assertThat(service.findLongUrl(response.shortCode()))
                .contains("https://example.com/redirect-target");
        assertThat(service.findLongUrl("missing"))
                .isEmpty();
    }

    @Test
    void cachesRedirectLookupAfterDatabaseHit() {
        UrlMappingRepository repository = Mockito.mock(UrlMappingRepository.class);
        UrlMapping mapping = new UrlMapping("https://example.com/cached");
        mapping.setShortCode("abc");
        when(repository.findByShortCode("abc")).thenReturn(Optional.of(mapping));

        UrlShortenerService cachedService = new UrlShortenerService(
                repository,
                new Base62Encoder(),
                new UrlValidator(),
                new RedirectCache(new UrlShortenerProperties()),
                new UrlShortenerProperties()
        );

        assertThat(cachedService.findLongUrl("abc")).contains("https://example.com/cached");
        assertThat(cachedService.findLongUrl("abc")).contains("https://example.com/cached");

        verify(repository, times(1)).findByShortCode("abc");
    }
}
