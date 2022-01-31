package fr.lernejo.search.api;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class GameInfoListenerTest {

    @Test
    void listener_id_null(){
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
            RabbitTemplate rabbitTemplate = springContext.getBean(RabbitTemplate.class);
            rabbitTemplate.setRoutingKey("game_info");
            Message message = MessageBuilder.withBody("{}".getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
            rabbitTemplate.convertAndSend(message);
        }
    }

    @Test
    void listener_ok(){
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
            File resourcesDirectory = new File("src/test/resources");
            String file = resourcesDirectory.getAbsolutePath()+"/game.json";
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(Paths.get(file).toFile(), new TypeReference<>() {});
            RabbitTemplate rabbitTemplate = springContext.getBean(RabbitTemplate.class);
            rabbitTemplate.setRoutingKey("game_info");
            Message message = MessageBuilder.withBody(new ObjectMapper().writeValueAsString(map).getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON).setHeader("game_id","42").build();
            rabbitTemplate.convertAndSend(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
