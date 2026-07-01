package com.alandhaas.urlshortener.api;

import com.alandhaas.urlshortener.service.UrlShortenerService;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {

    private final UrlShortenerService service;

    public RedirectController(UrlShortenerService service) {
        this.service = service;
    }

    @GetMapping("/{shortCode:[0-9a-zA-Z]+}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        return service.findLongUrl(shortCode)
                .map(longUrl -> ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, URI.create(longUrl).toString())
                        .<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
