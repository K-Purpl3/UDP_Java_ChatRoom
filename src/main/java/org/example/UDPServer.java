package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {
    private DatagramSocket socket;
    private List<ClientInfo> clients;

    static class ClientInfo {
        InetAddress address;
        int port;
        String username;
    }

    public static void main(String[]args){
        try {
            UDPServer server = new UDPServer();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public UDPServer() throws SocketException{
        socket = new DatagramSocket(5000);
        clients = new ArrayList<>();
    }

    public void start() throws IOException{
        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            handlePacket(packet);
        }
    }

    /*private void handlePacket (DatagramPacket packet){
        String message = new String(
                packet.getData(), 0, packet.getLength()
        );
        System.out.println("Recieved from " + packet.getAddress() + ":" + packet.getPort() + " -> " + message);
    }*/

    private void handlePacket(DatagramPacket packet) {
        String message = new String(
                packet.getData(), 0, packet.getLength()
        );

        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        if (message.startsWith("JOIN ")) {
            String username = message.substring(5);

            ClientInfo existing = findClient(address, port);
            if (existing == null) {
                ClientInfo client = new ClientInfo();
                client.address = address;
                client.port = port;
                client.username = username;
                clients.add(client);

                System.out.println(username + " joined from " + address + ":" + port);
            }
        }
    }


    private void broadcast(String message) throws IOException{
        byte[] data = message.getBytes();

        for (ClientInfo client : clients) {
            DatagramPacket packet = new DatagramPacket(
                    data, data.length,
                    client.address, client.port
            );
            socket.send(packet);
        }
    }

    private ClientInfo findClient(InetAddress address, int port) {
        for (ClientInfo client : clients) {
            if (client.address.equals(address) && client.port == port) {
                return client;
            }
        }
        return null;
    }


}

/*
*  public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(5000);
        System.out.println("UDP Server started");

        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String message = new String(
                    packet.getData(), 0, packet.getLength()
            );

            System.out.println(
                    "From " + packet.getAddress() + ":" +
                            packet.getPort() + " -> " + message
            );
        }
    }

    void broadcast(String message) throws IOException {
        byte[] data = message.getBytes();

        for (UDPClient.ClientInfo client : clients) {
            DatagramPacket packet = new DatagramPacket(
                    data, data.length,
                    client.address, client.port
            );
            socket.send(packet);
        }
    }
}
*/