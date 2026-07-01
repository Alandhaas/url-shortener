package com.alandhaas.urlshortener.api;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "app.base-url=http://localhost:8080",
        "spring.datasource.url=jdbc:h2:mem:controller-test;DB_CLOSE_DELAY=-1"
})
@AutoConfigureMockMvc
class UrlControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsShortUrl() throws Exception {
        mockMvc.perform(post("/api/v1/data/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"longUrl\":\"https://example.com/docs\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortCode").isString())
                .andExpect(jsonPath("$.shortUrl", startsWith("http://localhost:8080/")))
                .andExpect(jsonPath("$.longUrl").value("https://example.com/docs"));
    }

    @Test
    void redirectsShortCodeToLongUrl() throws Exception {
        String response = mockMvc.perform(post("/api/v1/data/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"longUrl\":\"https://example.com/redirect\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String shortCode = response.replaceAll(".*\"shortCode\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/" + shortCode))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com/redirect"));
    }

    @Test
    void returnsNotFoundForMissingShortCode() throws Exception {
        mockMvc.perform(get("/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnsBadRequestForInvalidUrl() throws Exception {
        mockMvc.perform(post("/api/v1/data/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"longUrl\":\"ftp://example.com/file\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Only http and https URLs are supported"));
    }
}
