package com.searchfight.search.service;


import com.searchfight.search.exception.ApiException;
import com.searchfight.search.model.bing.BingSearchInformation;
import com.searchfight.search.model.bing.BingSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

class BingSearchServiceTest {

    private RestTemplate restTemplate;
    private BingSearchService bingSearchService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        bingSearchService = new BingSearchService(restTemplate, "https://test.com", "12345");
    }

    @Test
    void shouldGetHitsWhenHttpStatusIsOk() {
        BingSearchResult bingSearchResult = new BingSearchResult();
        BingSearchInformation bingSearchInformation = new BingSearchInformation();
        bingSearchInformation.setTotalEstimatedMatches(123);
        bingSearchResult.setWebPages(bingSearchInformation);
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(BingSearchResult.class)))
                .thenReturn(new ResponseEntity<>(bingSearchResult, OK));
        long hits = bingSearchService.getHits("hi");
        assertEquals(123, hits);
    }

    @Test
    void shouldThrowExceptionWhenApiFails() {
        when(restTemplate.exchange(anyString(), eq(GET), any(), eq(BingSearchResult.class)))
                .thenReturn(new ResponseEntity<>(null, INTERNAL_SERVER_ERROR));
        assertThrows(ApiException.class, () -> bingSearchService.getHits("hi"));
    }
}