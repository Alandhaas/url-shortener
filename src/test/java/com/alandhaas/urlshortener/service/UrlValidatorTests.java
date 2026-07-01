package com.alandhaas.urlshortener.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class UrlValidatorTests {

    private final UrlValidator validator = new UrlValidator();

    @Test
    void acceptsHttpAndHttpsUrls() {
        assertThat(validator.validateAndNormalize(" https://example.com/docs/../guide "))
                .isEqualTo("https://example.com/guide");
        assertThat(validator.validateAndNormalize("http://example.com"))
                .isEqualTo("http://example.com");
    }

    @Test
    void rejectsMissingUrl() {
        assertThatThrownBy(() -> validator.validateAndNormalize(" "))
                .isInstanceOf(InvalidUrlException.class)
                .hasMessage("URL is required");
    }

    @Test
    void rejectsUnsupportedSchemes() {
        assertThatThrownBy(() -> validator.validateAndNormalize("ftp://example.com/file"))
                .isInstanceOf(InvalidUrlException.class)
                .hasMessage("Only http and https URLs are supported");
    }

    @Test
    void rejectsUrlsWithoutHost() {
        assertThatThrownBy(() -> validator.validateAndNormalize("https:///missing-host"))
                .isInstanceOf(InvalidUrlException.class)
                .hasMessage("URL must include a scheme and host");
    }
}
