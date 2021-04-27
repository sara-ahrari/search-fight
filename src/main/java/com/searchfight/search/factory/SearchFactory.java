package com.searchfight.search.factory;

import com.searchfight.search.model.SearchEngineName;
import com.searchfight.search.service.BingSearchService;
import com.searchfight.search.service.GoogleSearchService;
import com.searchfight.search.service.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchFactory {

    @Autowired
    private BingSearchService bingSearchService;
    @Autowired
    private GoogleSearchService googleSearchService;

    public Search getSearchEngine(SearchEngineName searchEngineName) {
        switch (searchEngineName) {
            case BING:
                return bingSearchService;
            case GOOGLE:
                return googleSearchService;
        }
        throw new RuntimeException("Engine not found");
    }
}
