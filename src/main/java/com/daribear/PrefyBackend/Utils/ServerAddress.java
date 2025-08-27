package com.daribear.PrefyBackend.Utils;

public class ServerAddress {

    public static String getServerAddress(){
        String host = System.getenv("SERVER_HOST");
        String port = System.getenv("SERVER_PORT");

        if (host == null || port == null) {
            throw new IllegalStateException("Server host and port must be set in environment variables!");
        }

        return "http://" + host + ":" + port;
    }
}
