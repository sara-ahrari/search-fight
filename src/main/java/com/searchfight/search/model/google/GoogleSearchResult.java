package com.searchfight.search.model.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoogleSearchResult {

    private GoogleSearchInformation searchInformation;
}
