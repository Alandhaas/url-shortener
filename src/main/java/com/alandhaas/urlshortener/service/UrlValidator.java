package com.alandhaas.urlshortener.service;

import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public class UrlValidator {

    private static final int MAX_URL_LENGTH = 2048;

    public String validateAndNormalize(String value) {
        String url = value == null ? "" : value.trim();

        if (url.isBlank()) {
            throw new InvalidUrlException("URL is required");
        }

        if (url.length() > MAX_URL_LENGTH) {
            throw new InvalidUrlException("URL must be 2048 characters or less");
        }

        URI uri = parse(url);
        String scheme = uri.getScheme();

        if (scheme == null || uri.getHost() == null) {
            throw new InvalidUrlException("URL must include a scheme and host");
        }

        if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
            throw new InvalidUrlException("Only http and https URLs are supported");
        }

        return uri.normalize().toString();
    }

    private URI parse(String url) {
        try {
            return URI.create(url);
        } catch (IllegalArgumentException exception) {
            throw new InvalidUrlException("URL is not valid");
        }
    }
}
