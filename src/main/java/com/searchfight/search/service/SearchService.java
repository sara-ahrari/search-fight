package com.searchfight.search.service;

import com.searchfight.search.factory.SearchFactory;
import com.searchfight.search.model.SearchEngineName;
import com.searchfight.search.model.SearchTermResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
@Data
public class SearchService {

    private final SearchFactory searchFactory;

    public Map<String, List<SearchTermResult>> search(Set<String> terms) {
        var results = new HashMap<String, List<SearchTermResult>>();
        for (var term : terms) {
            for (var engineName : SearchEngineName.values()) {
               var engine = searchFactory.getSearchEngine(engineName);
                var hits = engine.getHits(term);
                if (results.get(term) == null) {
                    var engineList = new ArrayList<>(
                            singletonList(new SearchTermResult(term, engineName.name(), hits)));
                    results.put(term, engineList);
                } else {
                    results.get(term).add(new SearchTermResult(term, engineName.name(), hits));
                }
            }
        }
        printResultsPerTerm(results);
        return results;
    }

    public Map<String, String> getEnginesWinner(Map<String, List<SearchTermResult>> results) {
        Map<String, String> winners = new HashMap<>();
        for (int i = 0; i < SearchEngineName.values().length; i++) {
            long engineWinnerHits = 0;
            String engineName = "";
            String winnerTerm = "";
            for (Map.Entry<String, List<SearchTermResult>> entry : results.entrySet()) {
                long currentEngineHit = entry.getValue().get(i).getHitResult();
                if (engineWinnerHits < currentEngineHit) {
                    engineWinnerHits = currentEngineHit;
                    winnerTerm = entry.getKey();
                    engineName = entry.getValue().get(i).getEngineName();
                }
            }
            winners.put(engineName, winnerTerm);
        }
        printEnginesWinner(winners);
        return winners;
    }

    public String getTotalWinner(Map<String, List<SearchTermResult>> results) {
        String winner = "";
        long winnerHits = 0;
        for (Map.Entry<String, List<SearchTermResult>> entry : results.entrySet()) {
            Long termTotalEnginesHits = entry.getValue().stream()
                    .map(SearchTermResult::getHitResult)
                    .reduce(0L, Long::sum);
            if (winnerHits < termTotalEnginesHits) {
                winnerHits = termTotalEnginesHits;
                winner = entry.getKey();
            }
        }
        return winner;
    }

    private void printEnginesWinner(Map<String, String> engineWinners) {
        var str = new StringBuilder();
        for (Map.Entry<String, String> entry : engineWinners.entrySet()) {
            str.append(entry.getKey()).append(" Winner: ").append(entry.getValue());
            str.append(" \n");
        }
        System.out.println(str.toString().trim());
    }

    private void printResultsPerTerm(Map<String, List<SearchTermResult>> results) {
        var str = new StringBuilder();
        for (Map.Entry<String, List<SearchTermResult>> entry : results.entrySet()) {
            str.append(entry.getKey()).append(": ");
            for (SearchTermResult searchTerm : entry.getValue()) {
                str.append(" ").append(searchTerm.getEngineName())
                        .append(": ").append(searchTerm.getHitResult());
            }
            str.append(" \n");
        }
        System.out.println(str.toString().trim());
    }
}