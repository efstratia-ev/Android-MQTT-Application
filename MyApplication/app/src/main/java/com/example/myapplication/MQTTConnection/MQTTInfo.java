package com.example.myapplication.MQTTConnection;


public final class MQTTInfo {
    private static String IP="10.0.2.2";
    private static int Port=1883;
    private static final String client = "ExampleAndroidClient";
    private static final String Topic = "sensor";

    public static void setIP(String IP){
        MQTTInfo.IP =IP;
    }

    public static void setPort(int Port){
        MQTTInfo.Port=Port;
    }

    public static String getServerURI() {
        return "tcp://"+IP+":"+Port;
    }

    public static String getIP() {
        return IP;
    }

    public static String getClient() {
        return client;
    }

    public static String getTopic() {
        return Topic;
    }
}
