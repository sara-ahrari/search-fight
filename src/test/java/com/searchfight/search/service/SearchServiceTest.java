package com.searchfight.search.service;

import com.searchfight.search.factory.SearchFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.searchfight.search.model.SearchEngineName.BING;
import static com.searchfight.search.model.SearchEngineName.GOOGLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchServiceTest {

   private SearchService searchService;
   private SearchFactory searchFactory;
   private GoogleSearchService googleSearchService;
   private BingSearchService bingSearchService;

   @BeforeEach
   void setup() {
       searchFactory = mock(SearchFactory.class);
       googleSearchService = mock(GoogleSearchService.class);
       bingSearchService = mock(BingSearchService.class);
       searchService = new SearchService(searchFactory);
       when(searchFactory.getSearchEngine(GOOGLE)).thenReturn(googleSearchService);
       when(searchFactory.getSearchEngine(BING)).thenReturn(bingSearchService);
       when(googleSearchService.getHits("Sara")).thenReturn(12L);
       when(bingSearchService.getHits("Sara")).thenReturn(13L);
       when(googleSearchService.getHits("Daria")).thenReturn(15L);
       when(bingSearchService.getHits("Daria")).thenReturn(16L);
   }

    @Test
    void shouldSearchAndReturnCorrectResult() {
        var results = searchService
                .search(new String[]{"Sara", "Daria"});

        assertEquals(2, results.size());
        assertEquals("GOOGLE", results.get("Sara").get(0).getEngineName());
        assertEquals("BING", results.get("Sara").get(1).getEngineName());
        assertEquals(13, results.get("Sara").get(1).getHitResult());

        assertEquals("GOOGLE", results.get("Daria").get(0).getEngineName());
        assertEquals("BING", results.get("Daria").get(1).getEngineName());
        assertEquals(15, results.get("Daria").get(0).getHitResult());
    }

    @Test
    void shouldReturnCorrectTotalWinner() {
        when(googleSearchService.getHits("Hanna")).thenReturn(17L);
        when(bingSearchService.getHits("Hanna")).thenReturn(50L);
        var results = searchService
                .search(new String[]{"Sara", "Daria", "Hanna"});
        var totalWinner = searchService.getTotalWinner(results);
        assertEquals("Hanna", totalWinner);
    }

    @Test
    void shouldReturnCorrectWinnersForEachSearchEngine() {
        var results = searchService
                .search(new String[]{"Sara", "Daria"});
        var engineWinners = searchService.getEnginesWinner(results);
        assertEquals(2, engineWinners.size());
        assertEquals("Daria", engineWinners.get("GOOGLE"));
        assertEquals("Daria",engineWinners.get("BING"));
    }
}