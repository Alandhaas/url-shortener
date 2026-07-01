package com.alandhaas.urlshortener.repository;

import com.alandhaas.urlshortener.domain.UrlMapping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByLongUrl(String longUrl);
}
