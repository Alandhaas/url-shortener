package com.alandhaas.urlshortener.service;

import com.alandhaas.urlshortener.api.ShortenResponse;
import com.alandhaas.urlshortener.config.UrlShortenerProperties;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerService {

    private final AtomicLong nextId = new AtomicLong(1);
    private final Base62Encoder encoder;
    private final UrlValidator validator;
    private final UrlShortenerProperties properties;

    public UrlShortenerService(
            Base62Encoder encoder,
            UrlValidator validator,
            UrlShortenerProperties properties
    ) {
        this.encoder = encoder;
        this.validator = validator;
        this.properties = properties;
    }

    public ShortenResponse shorten(String longUrl) {
        String normalizedUrl = validator.validateAndNormalize(longUrl);
        String shortCode = encoder.encode(nextId.getAndIncrement());

        return new ShortenResponse(
                shortCode,
                shortUrlFor(shortCode),
                normalizedUrl
        );
    }

    private String shortUrlFor(String shortCode) {
        String baseUrl = properties.getBaseUrl().replaceAll("/+$", "");
        return baseUrl + "/" + shortCode;
    }
}
