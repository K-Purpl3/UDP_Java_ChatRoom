package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {



    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");

        byte[] data = "JOIN Alice".getBytes();
        DatagramPacket packet = new DatagramPacket(
                data, data.length, serverAddress, 5000
        );

        socket.send(packet);
        socket.close();
    }

}
