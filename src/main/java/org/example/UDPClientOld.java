package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClientOld implements Runnable {

    public static void main(String[] args) throws Exception {
        UDPClient client = new UDPClient("Alice");
        client.run();
    }

    public UDPClient(String username) throws Exception {
        DatagramSocket socket = new DatagramSocket();
    }

    @Override
    public void run() {
        try {
            send("JOIN " + username);

            Thread receiveThread = new Thread(this::receiveLoop);
            receiveThread.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));

            while (running) {
                String line = reader.readLine();
                if (line.equals("/quit")) {
                    running = false;
                    send("QUIT");
                } else {
                    send("MSG " + line);
                }
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send(String message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(
                data, data.length, serverAddress, serverPort
        );
        socket.send(packet);
    }

    private void receiveLoop() {
        byte[] buffer = new byte[1024];

        while (running) {
            try {
                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(
                        packet.getData(), 0, packet.getLength()
                );
                System.out.println(message);

            } catch (Exception e) {
                if (running) e.printStackTrace();
            }
        }
    }

}
