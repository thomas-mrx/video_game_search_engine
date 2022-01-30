package fr.lernejo.search.api;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GamesController {
    private final RestHighLevelClient client;

    public GamesController(RestHighLevelClient client) {
        this.client = client;
    }

    @GetMapping("/api/games")
    List<Map<String, Object>> searchGames(@RequestParam String query) throws IOException {
        if (query == null) {
            return null;
        }
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(query);
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> result = new ArrayList<>();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit hit : searchHits) {
            result.add(hit.getSourceAsMap());
        }
        return result;
    }

    @ExceptionHandler({ ElasticsearchException.class, RuntimeException.class, IOException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    List<Map<String, Object>> handleException() {
        return new ArrayList<>();
    }

}
