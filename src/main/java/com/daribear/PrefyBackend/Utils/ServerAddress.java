package com.daribear.PrefyBackend.Utils;

public class ServerAddress {

    /**
     * Returns the base url of the springboot server
     * @return base url of this server
     */
    public static String getServerAddress(){
        String host = System.getenv("SERVER_HOST");
        String port = System.getenv("SERVER_PORT");

        //Guard to check neither host nor port is null
        if (host == null || port == null) {
            throw new IllegalStateException("Server host and port must be set in environment variables!");
        }

        return "http://" + host + ":" + port;
    }
}
