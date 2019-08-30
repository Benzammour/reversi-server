package network;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

import map.GameMap;

// Created by benzammour

public class Server extends Thread {

    private final int port;
    private ArrayList<ServerWorker> worker = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            byte clientCount = 0;

            while (clientCount < 2) {
                clientCount++;
                Socket client = serverSocket.accept();

                System.out.println("Accepted new connection: " + client);
                ServerWorker sw = new ServerWorker(this, client, clientCount);
                worker.add(sw);
                sw.start();
            }

            String mapName = "2holes1map.txt";
            String map = "";

            try (BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(String.format("maps/%s", mapName))))) {
                map = br.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                System.err.println("Couldn't read file.");
                e.printStackTrace();
            }

            System.out.println(map);

            for (ServerWorker sw : worker) {
                sw.sendPlayerNumber();
                sw.sendMap(map);
            }

            System.out.println("got here");

//            for (ServerWorker sw : worker) {
//                sw.close();
//            }

            while (true) {}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ServerWorker> getWorker() {
        return worker;
    }
}
