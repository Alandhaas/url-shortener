package com.alandhaas.urlshortener.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class UrlShortenerProperties {

    private String baseUrl = "http://localhost:8080";
    private int redirectCacheSize = 1000;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getRedirectCacheSize() {
        return redirectCacheSize;
    }

    public void setRedirectCacheSize(int redirectCacheSize) {
        this.redirectCacheSize = redirectCacheSize;
    }
}
