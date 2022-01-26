package fr.lernejo.fileinjector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) {
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
            if (args.length == 1) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, Object>> map = mapper.readValue(Paths.get(args[0]).toFile(), new TypeReference<>() {});

                    RabbitTemplate rabbitTemplate = springContext.getBean(RabbitTemplate.class);
                    rabbitTemplate.setRoutingKey("game_info");

                    for (Map<?, ?> el : map) {
                        try {
                            String orderJson = new ObjectMapper().writeValueAsString(el);
                            Message message = MessageBuilder
                                .withBody(orderJson.getBytes())
                                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                                .setHeader("game_id", el.get("id"))
                                .build();
                            rabbitTemplate.convertAndSend(message);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
