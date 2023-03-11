package com.daribear.PrefyBackend.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class ComputerIp {

    public static String getComputerAddress(){
        String systemipaddress = "";
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
            systemipaddress = socket.getLocalAddress().toString();
        } catch (Exception e){
            System.out.println("Exception with ip e:" + e);
        }
        return systemipaddress;
    }
}
