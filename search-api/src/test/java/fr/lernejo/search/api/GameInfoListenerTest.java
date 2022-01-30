package fr.lernejo.search.api;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GameInfoListenerTest {

    @Test
    void test_listener(){
        GameInfoListener test = new GameInfoListener(null);
        assertDoesNotThrow(() -> test.onMessage(new Message("test".getBytes())));
    }
}
