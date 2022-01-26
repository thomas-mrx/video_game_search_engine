package fr.lernejo.search.api;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class GameInfoListener {
    private final RestHighLevelClient client;

    public GameInfoListener(RestHighLevelClient client) {
        this.client = client;
    }

    @RabbitListener(queues = "game_info")
    public void onMessage(Message message) throws IOException {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        String type = message.getMessageProperties().getContentType();
        String id = headers.get("game_id").toString();
        if (id == null || type == null) {
            return;
        }
        /*DeleteIndexRequest requests = new DeleteIndexRequest("games");
        client.indices().delete(requests, RequestOptions.DEFAULT);*/

        IndexRequest request = new IndexRequest("games").id(id);
        request.source(message.getBody(), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

}
