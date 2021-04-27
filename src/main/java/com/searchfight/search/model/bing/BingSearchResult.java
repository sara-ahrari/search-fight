package com.searchfight.search.model.bing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BingSearchResult {

    private BingSearchInformation webPages;
}
