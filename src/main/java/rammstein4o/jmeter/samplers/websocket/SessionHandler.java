package rammstein4o.jmeter.samplers.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class SessionHandler extends StompSessionHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(SessionHandler.class);
    private static StompSession session;

    @Override
    public void afterConnected(StompSession mySession, StompHeaders connectedHeaders) {
        log.info("New session: {}", mySession.getSessionId());
        setSession(mySession);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Object.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object response) {
        log.info("New Message {}", response);
    }

    @Override
    public void handleException(StompSession mySession, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        String message = String.format("Session %s got exception %s", mySession.getSessionId(), exception.getMessage());
        log.error(message, exception);
    }

    @Override
    public void handleTransportError(StompSession mySession, Throwable exception) {
        String message = String.format("Session %s got transport error %s", mySession.getSessionId(), exception.getMessage());
        log.error(message, exception);
    }

    private void setSession(StompSession mySession) {
        session = mySession;
    }

    boolean isConnected() {
        try {
            return session.isConnected();
        } catch (NullPointerException e) {
            return false;
        }
    }

    void subscribe(String topic, boolean appendSession) {
        synchronized (session) {
            session.subscribe(topic + (appendSession ? session.getSessionId() : ""), this);
        }
    }

    void disconnect() {
        synchronized (session) {
            session.disconnect();
        }
    }
}
