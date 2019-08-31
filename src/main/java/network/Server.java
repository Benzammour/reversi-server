package network;


import map.GameMap;
import util.MapUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Server extends Thread {

    private final int port;

    private final ArrayList<ServerWorker> worker = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        byte clientCount = 0;
        int turn = 0;
        String mapName = "2holes1map.txt";
        String map = "";
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // establish connection with clients
        while (clientCount < 2) {
            clientCount++;
            Socket client = new Socket();

            try {
                client = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Accepted new connection: " + client);
            ServerWorker sw = new ServerWorker(this, client, clientCount);
            worker.add(sw);
            sw.start();
        }

        // load map
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(String.format("maps/%s", mapName))))) {
            map = br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.err.println("Couldn't read file.");
            e.printStackTrace();
        }

        // processes map
        GameMap.getInstance().generateMapFromString(map);
        System.out.println(GameMap.getInstance().toString());

        // send map to clients
        try {
            for (ServerWorker sw : worker) {
                sw.sendString((byte) 1, map.getBytes().length, map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send player ids to clients
        try {
            for (ServerWorker sw : worker) {
                sw.sendPlayerNumber();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // game loop
        while (!MapUtil.getMovesForPlayer(GameMap.getInstance(), (char) ('0' + worker.get((turn + 1) % 2).getPlayerID())).isEmpty()) {
			System.out.println(GameMap.getInstance().toString());
        	try {
                worker.get((turn + 1) % 2).handleMove();
            } catch (IOException e) {
                e.printStackTrace();
            }

            turn = (turn + 1) % 2;
        }

        // announce winner & game end
		System.out.println("Player " + MapUtil.maxNumberOfStones(GameMap.getInstance()) + " has won.");
		try {
			for (ServerWorker sw : worker) {
				sw.announceEnd();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// close connection with sockets
        for (ServerWorker sw : worker) {
            sw.close();
        }

    }

    // -----------------------------------------------  Getter/Setter
    ArrayList<ServerWorker> getWorker() {
        return worker;
    }
}
