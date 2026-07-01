package com.alandhaas.urlshortener.service;

import com.alandhaas.urlshortener.api.ShortenResponse;
import com.alandhaas.urlshortener.config.UrlShortenerProperties;
import com.alandhaas.urlshortener.domain.UrlMapping;
import com.alandhaas.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UrlShortenerService {

    private final UrlMappingRepository repository;
    private final Base62Encoder encoder;
    private final UrlValidator validator;
    private final UrlShortenerProperties properties;

    public UrlShortenerService(
            UrlMappingRepository repository,
            Base62Encoder encoder,
            UrlValidator validator,
            UrlShortenerProperties properties
    ) {
        this.repository = repository;
        this.encoder = encoder;
        this.validator = validator;
        this.properties = properties;
    }

    @Transactional
    public ShortenResponse shorten(String longUrl) {
        String normalizedUrl = validator.validateAndNormalize(longUrl);
        UrlMapping mapping = repository.findByLongUrl(normalizedUrl)
                .orElseGet(() -> createMapping(normalizedUrl));

        return new ShortenResponse(
                mapping.getShortCode(),
                shortUrlFor(mapping.getShortCode()),
                mapping.getLongUrl()
        );
    }

    private UrlMapping createMapping(String normalizedUrl) {
        UrlMapping mapping = repository.save(new UrlMapping(normalizedUrl));
        String shortCode = encoder.encode(mapping.getId());
        mapping.setShortCode(shortCode);
        return repository.save(mapping);
    }

    private String shortUrlFor(String shortCode) {
        String baseUrl = properties.getBaseUrl().replaceAll("/+$", "");
        return baseUrl + "/" + shortCode;
    }
}
