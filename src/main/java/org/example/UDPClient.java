package org.example;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLOutput;
import java.util.Scanner;


/*
 * Cliente UDP con:
 * - Hilo para recibir mensajes
 * - Consola para enviar mensajes
 */
public class UDPClient implements Runnable {


    private DatagramSocket socket; // Socket UDP del cliente
    private InetAddress serverAddress; // IP del servidor
    private int serverPort = 5000; // Puerto del servidor
    private boolean running; // Control del bucle
    private String username; // Nombre del usuario


    public static void main(String[] args) throws Exception {
        System.out.println("Starting UDP Server");
        System.out.println("Enter your name");
        String name = new Scanner(System.in).next();
        UDPClient client = new UDPClient(name);
        client.run();
    }


    // Constructor del cliente
    public UDPClient(String username) throws Exception {
        this.username = username;
        this.socket = new DatagramSocket(); // Puerto automático
        this.serverAddress = InetAddress.getByName("127.0.0.1");
        this.running = true;
    }
    @Override
    public void run() {
        try {
// Avisamos al servidor de que nos unimos
            send("JOIN " + username);


// Hilo para recibir mensajes del servidor
            Thread receiveThread = new Thread(this::receiveLoop);
            receiveThread.start();


// Leemos mensajes desde consola
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
            receiveThread.join();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Envía un mensaje al servidor
    private void send(String message) throws Exception {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(
                data, data.length, serverAddress, serverPort
        );
        socket.send(packet);
    }


    // Bucle de recepción de mensajes
    private void receiveLoop() {
        byte[] buffer = new byte[1024];


        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
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
