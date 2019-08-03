package rammstein4o.jmeter.samplers;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.testelement.property.BooleanProperty;
import rammstein4o.jmeter.samplers.websocket.Client;

public class WebSocketSampler extends AbstractSampler {
    private static final String URL = "WebSocketSampler.url";
    private static final String TOKEN = "WebSocketSampler.token";
    private static final String PUBLIC_TOPIC = "WebSocketSampler.publicTopic";
    private static final String PRIVATE_TOPIC = "WebSocketSampler.privateTopic";
    private static final String APPEND_SESSION = "WebSocketSampler.appendSession";

    public WebSocketSampler() {
        super.setName("WebSocket Sampler");
    }

    String getUrl() {
        return getPropertyAsString(WebSocketSampler.URL);
    }

    void setUrl(String url) {
        setProperty(new StringProperty(WebSocketSampler.URL, url));
    }

    String getToken() {
        return getPropertyAsString(WebSocketSampler.TOKEN);
    }

    void setToken(String token) {
        setProperty(new StringProperty(WebSocketSampler.TOKEN, token));
    }

    String getPublicTopic() {
        return getPropertyAsString(WebSocketSampler.PUBLIC_TOPIC);
    }

    void setPublicTopic(String topic) {
        setProperty(new StringProperty(WebSocketSampler.PUBLIC_TOPIC, topic));
    }

    String getPrivateTopic() {
        return getPropertyAsString(WebSocketSampler.PRIVATE_TOPIC);
    }

    void setPrivateTopic(String topic) {
        setProperty(new StringProperty(WebSocketSampler.PRIVATE_TOPIC, topic));
    }

    boolean getAppendSession() {
        return getPropertyAsBoolean(WebSocketSampler.APPEND_SESSION);
    }

    void setAppendSession(boolean value) {
        setProperty(new BooleanProperty(WebSocketSampler.APPEND_SESSION, value));
    }

    @Override
    public SampleResult sample(Entry entry) {
        String url = getUrl();
        String token = getToken();
        String topic = token.isEmpty() ? getPublicTopic() : getPrivateTopic();

        SampleResult result = new SampleResult();
        result.setSampleLabel(getName());
        result.setSamplerData("");

        result.sampleStart();
        Client wsClient = new Client();

        wsClient.connect(token, url);
        result.setSamplerData(result.getSamplerData() + "Connected to " + getUrl() + "\n");

        wsClient.subscribe(topic, getAppendSession());
        result.setSamplerData(result.getSamplerData() + "Subscribed for " + topic + "\n");

        result.sampleEnd();
        result.setSuccessful(true);

        return result;
    }
}
