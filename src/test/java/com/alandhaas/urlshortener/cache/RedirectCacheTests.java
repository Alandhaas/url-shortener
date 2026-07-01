package com.alandhaas.urlshortener.cache;

import static org.assertj.core.api.Assertions.assertThat;

import com.alandhaas.urlshortener.config.UrlShortenerProperties;
import org.junit.jupiter.api.Test;

class RedirectCacheTests {

    @Test
    void storesRecentRedirects() {
        RedirectCache cache = cacheWithSize(2);

        cache.put("a", "https://example.com/a");

        assertThat(cache.get("a")).contains("https://example.com/a");
    }

    @Test
    void removesOldestEntryWhenFull() {
        RedirectCache cache = cacheWithSize(2);

        cache.put("a", "https://example.com/a");
        cache.put("b", "https://example.com/b");
        cache.put("c", "https://example.com/c");

        assertThat(cache.get("a")).isEmpty();
        assertThat(cache.get("b")).contains("https://example.com/b");
        assertThat(cache.get("c")).contains("https://example.com/c");
        assertThat(cache.size()).isEqualTo(2);
    }

    @Test
    void canBeDisabled() {
        RedirectCache cache = cacheWithSize(0);

        cache.put("a", "https://example.com/a");

        assertThat(cache.get("a")).isEmpty();
        assertThat(cache.size()).isZero();
    }

    private RedirectCache cacheWithSize(int size) {
        UrlShortenerProperties properties = new UrlShortenerProperties();
        properties.setRedirectCacheSize(size);
        return new RedirectCache(properties);
    }
}
