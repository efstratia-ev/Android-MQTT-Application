package MQTTConnection;

public final class MQTTInfo {
    private static int[] qos = {2,2};
    private static final String serverURI = "tcp://127.0.0.1:1883";
    private static final String client = "EdgeServer";
    private static final String[] SubscriptionTopics = {"ATtoES26","ATtoES27"};
    private static final String[] PublishingTopics = {"EStoAT26","EStoAT27"};

    public static String getServerURI() {
        return serverURI;
    }

    public static String getClient() {
        return client;
    }

    public static String[] getSubscriptionTopics() {
        return SubscriptionTopics;
    }

    public static String[] getPublishingTopics() {
        return PublishingTopics;
    }

    public static int[] getQos() {
        return qos;
    }
}
