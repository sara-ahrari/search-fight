package com.searchfight.search.model;

import lombok.Data;

@Data
public class SearchTermResult {

    private String searchTerm;
    private String engineName;
    private long hitResult;

    public SearchTermResult(String searchTerm, String engineName, long hitResult) {
        this.searchTerm = searchTerm;
        this.engineName = engineName;
        this.hitResult = hitResult;
    }
}
