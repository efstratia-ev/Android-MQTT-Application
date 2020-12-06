package com.example.myapplication.MQTTConnection;


import com.example.myapplication.MainActivity;

public final class MQTTInfo {
    private static String IP="192.168.1.12";
    private static int Port=1883;

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

    public static int getPort(){
        return Port;
    }

    public static String getClient() {
        return "client"+ MainActivity.TerminalID;
    }
}
