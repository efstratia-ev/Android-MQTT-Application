package com.example.myapplication.MQTTConnection;

public final class MQTTInfo {
    private static String serverURI = "tcp://192.168.1.12:1883";
    private static final String client = "ExampleAndroidClient";
    private static final String Topic = "sensor";

    public void change_serverURI(String IP,String Port){
        serverURI="tcp://"+IP+":"+Port;
    }

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
