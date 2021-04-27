package com.searchfight.search.service;

import com.searchfight.search.exception.ApiException;
import com.searchfight.search.model.google.GoogleSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Service
@RequiredArgsConstructor
public class GoogleSearchService extends Search {

    private RestTemplate restTemplate;
    private String baseUrl;
    private String cx;
    private String apiKey;

    @Autowired
    public GoogleSearchService(RestTemplate restTemplate, @Value("${baseUrl.google}") String baseUrl,
                               @Value("${googleCx}") String cx, @Value("${apiKey.google}") String apiKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.cx = cx;
        this.apiKey = apiKey;
    }

    @Override
    public long getHits(String searchTerm) {
        try {
            ResponseEntity<GoogleSearchResult> response
                    = restTemplate.getForEntity(buildUrl(searchTerm), GoogleSearchResult.class);
            if (response.getStatusCode() == OK) {
                return response.getBody().getSearchInformation().getTotalResults();
            } else {
                throw new ApiException("No response found " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            throw new ApiException("Google API failed", e);
        }
    }

    private String buildUrl(String searchTerm) {
        UriComponentsBuilder uri = fromUriString(baseUrl)
                .queryParam("key", apiKey)
                .queryParam("cx", cx).queryParam("q", searchTerm);
        return uri.toUriString();
    }
}
