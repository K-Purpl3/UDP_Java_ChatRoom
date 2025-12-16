package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class PlantillaServer {
    DatagramSocket socket = new DatagramSocket(5000);
    List<PlantillaClient> clients = new ArrayList<>();

    while (true) {
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        socket.receive(p);

        String msg = new String(p.getData(), 0, p.getLength());
        InetAddress ip = p.getAddress();
        int port = p.getPort();
    }

}
