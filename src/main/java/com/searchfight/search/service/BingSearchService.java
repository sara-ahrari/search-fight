package com.searchfight.search.service;

import com.searchfight.search.exception.ApiException;
import com.searchfight.search.model.bing.BingSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Service
@RequiredArgsConstructor
public class BingSearchService extends Search {

    private RestTemplate restTemplate;
    private String baseUrl;
    private String apiKey;

    @Autowired
    public BingSearchService(RestTemplate restTemplate, @Value("${baseUrl.bing}") String baseUrl,
                             @Value("${apiKey.bing}") String apiKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    @Override
    public long getHits(String searchTerm) {
        long hits = 0;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<BingSearchResult> response = restTemplate
                    .exchange(buildUrl(searchTerm), GET, entity, BingSearchResult.class);

            if (response.getStatusCode() == OK) {
                if (response.getBody().getWebPages() != null) {
                    hits = response.getBody().getWebPages().getTotalEstimatedMatches();
                }
            } else {
                throw new ApiException("No response found " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            throw new ApiException("Bing API failed", e);
        }
        return hits;
    }

    private String buildUrl(String searchTerm) {
        UriComponentsBuilder uri = fromUriString(baseUrl)
                .queryParam("q", searchTerm);
        return uri.toUriString();
    }
}
