package com.alandhaas.urlshortener.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.alandhaas.urlshortener.domain.UrlMapping;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class UrlMappingRepositoryTests {

    @Autowired
    private UrlMappingRepository repository;

    @Test
    void savesAndFindsMappingByLongUrl() {
        UrlMapping mapping = new UrlMapping("https://example.com/docs");
        mapping.setShortCode("abc");

        UrlMapping saved = repository.save(mapping);

        assertThat(repository.findByLongUrl("https://example.com/docs"))
                .hasValueSatisfying(found -> {
                    assertThat(found.getId()).isEqualTo(saved.getId());
                    assertThat(found.getShortCode()).isEqualTo("abc");
                    assertThat(found.getLongUrl()).isEqualTo("https://example.com/docs");
                    assertThat(found.getCreatedAt()).isNotNull();
                });
    }
}
