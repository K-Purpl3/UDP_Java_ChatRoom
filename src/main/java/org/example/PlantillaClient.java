package org.example;

import java.net.DatagramSocket;

public class PlantillaClient {
    DatagramSocket socket = new DatagramSocket();
    socket.send(new DatagramPacket(
            data, data.length, ip, port
            ));

}
