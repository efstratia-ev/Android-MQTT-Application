package MQTTConnection;

public final class MQTTInfo {
    private static final String serverURI = "tcp://127.0.0.1:1883";
    private static final String client = "ExampleClient";
    private static final String Topic = "sensor";

    public static String getServerURI() {
        return serverURI;
    }

    public static String getClient() {
        return client;
    }

    public static String getTopic() {
        return Topic;
    }
}
