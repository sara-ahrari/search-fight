package com.searchfight.search.service;

import com.searchfight.search.exception.ApiException;
import com.searchfight.search.model.google.GoogleSearchInformation;
import com.searchfight.search.model.google.GoogleSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

class GoogleSearchServiceTest {

    private RestTemplate restTemplate;
    private GoogleSearchService googleSearchService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        googleSearchService = new GoogleSearchService(restTemplate, "https://test.com", "123", "12345");
    }

    @Test
    void shouldGetHitsWhenHttpStatusIsOk() {
        GoogleSearchResult googleSearchResult = new GoogleSearchResult();
        GoogleSearchInformation googleSearchInformation = new GoogleSearchInformation();
        googleSearchInformation.setTotalResults(124);
        googleSearchResult.setSearchInformation(googleSearchInformation);
        when(restTemplate.getForEntity(anyString(), eq(GoogleSearchResult.class)))
                .thenReturn(new ResponseEntity<>(googleSearchResult, OK));
        long hits = googleSearchService.getHits("hi");
        assertEquals(124, hits);
    }

    @Test
    void shouldThrowExceptionWhenApiFails() {
        when(restTemplate.getForEntity(anyString(), eq(GoogleSearchResult.class)))
                .thenReturn(new ResponseEntity<>(null, INTERNAL_SERVER_ERROR));
        assertThrows(ApiException.class, () -> googleSearchService.getHits("hi"));
    }
}