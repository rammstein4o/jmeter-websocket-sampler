package rammstein4o.jmeter.samplers.websocket;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import static org.awaitility.Awaitility.await;

public class Client {

    private final SessionHandler sessionHandler = new SessionHandler();

    public void connect(String token, String url) {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        if (!token.isEmpty()) {
            headers.add("Authorization", "Bearer " + token);
        }

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        stompClient.connect(url, headers, sessionHandler);

        await().until(sessionHandler::isConnected);
    }

    public void subscribe(String topic, boolean appendSession) {
        sessionHandler.subscribe(topic, appendSession);
    }

    public void disconnect() {
        sessionHandler.disconnect();
    }

}
