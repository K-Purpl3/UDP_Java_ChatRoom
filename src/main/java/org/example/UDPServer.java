package org.example;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class UDPServer {


    // Socket UDP del servidor (escucha en un puerto fijo)
    private DatagramSocket socket;


    // Lista de clientes conectados (manual, porque UDP no mantiene estado)
    private List<ClientInfo> clients;


    /*
     * Clase interna que representa un cliente UDP
     * En UDP NO hay conexión, así que tenemos que
     * guardar IP + puerto manualmente
     */
    static class ClientInfo {
        InetAddress address; // IP del cliente
        int port; // Puerto del cliente
        String username; // Nombre de usuario
    }


    public static void main(String[] args) {
        try {
            UDPServer server = new UDPServer();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Constructor: crea el socket y la lista de clientes
    public UDPServer() throws SocketException {
        socket = new DatagramSocket(5000); // Puerto del servidor
        clients = new ArrayList<>(); // Lista vacía de clientes
    }


    // Bucle principal del servidor
    public void start() throws IOException {
        byte[] buffer = new byte[1024]; // Buffer para recibir datos


        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet); // Espera un paquete UDP


            handlePacket(packet); // Procesa el paquete recibido
        }
    }


    /*private void handlePacket(DatagramPacket packet) {
        String message = new String(
                packet.getData(), 0, packet.getLength()
        );


        InetAddress address = packet.getAddress(); // IP del cliente
        int port = packet.getPort(); // Puerto del cliente



         * Protocolo del servidor:
         * JOIN username -> registrar cliente

        if (message.startsWith("JOIN ")) {
            String username = message.substring(5);


// Comprobamos si ya existe el cliente
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
    }*/


    private void handlePacket(DatagramPacket packet) {
        String message = new String(
                packet.getData(), 0, packet.getLength()
        );

        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        // JOIN username
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
            return;
        }

        // MSG texto
        if (message.startsWith("MSG ")) {
            ClientInfo sender = findClient(address, port);
            if (sender == null) return;

            String text = message.substring(4);
            String formatted = sender.username + ": " + text;

            // Mostrar en consola del servidor
            System.out.println(formatted);

            try {
                // Enviar a todos los clientes (incluido el que lo envió)
                broadcast(formatted);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*
     * Envía un mensaje a TODOS los clientes registrados
     * Esto simula un broadcast (chat)
     */
    private void broadcast(String message) throws IOException {
        byte[] data = message.getBytes();


        for (ClientInfo client : clients) {
            DatagramPacket packet = new DatagramPacket(
                    data, data.length,
                    client.address, client.port
            );
            socket.send(packet);
        }
    }


    // Busca un cliente por IP y puerto
    private ClientInfo findClient(InetAddress address, int port) {
        for (ClientInfo client : clients) {
            if (client.address.equals(address) && client.port == port) {
                return client;
            }
        }
        return null;
    }
}