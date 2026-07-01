package com.alandhaas.urlshortener.cache;

import com.alandhaas.urlshortener.config.UrlShortenerProperties;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RedirectCache {

    private final int maxEntries;
    private final Map<String, String> values;

    public RedirectCache(UrlShortenerProperties properties) {
        this.maxEntries = Math.max(0, properties.getRedirectCacheSize());
        this.values = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return maxEntries > 0 && size() > maxEntries;
            }
        };
    }

    public Optional<String> get(String shortCode) {
        if (maxEntries == 0) {
            return Optional.empty();
        }

        synchronized (values) {
            return Optional.ofNullable(values.get(shortCode));
        }
    }

    public void put(String shortCode, String longUrl) {
        if (maxEntries == 0) {
            return;
        }

        synchronized (values) {
            values.put(shortCode, longUrl);
        }
    }

    int size() {
        synchronized (values) {
            return values.size();
        }
    }
}
