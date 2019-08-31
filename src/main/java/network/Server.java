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

    private final boolean silent;

    private boolean customMap;

    private String mapPath;

    private int timeLimit;

    public Server(int port, boolean silent, String mapPath, int timelimit) {
        this.port = port;
        this.silent = silent;
        this.mapPath = mapPath;
        if (!mapPath.equals(""))
            customMap = true;
        this.timeLimit = timelimit;
    }

    @Override
    public void run() {
        byte clientCount = 0;
        int turn = 0;
        String map;

        if (mapPath.equals("")) {
            mapPath = "trivial.txt";
        }

        // setup sockets
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load map
        BufferedReader br = null;
        if (customMap) {
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(mapPath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(String.format("maps/%s", mapPath))));
        }
        map = br.lines().collect(Collectors.joining("\n"));

        // process map
        GameMap.getInstance().generateMapFromString(map);
        if (GameMap.getInstance().getMapHeight() > 50 || GameMap.getInstance().getMapWidth() > 50) {
            if (!silent)
                System.err.println("Map is too big.");
            System.exit(-1);
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

            if (!silent) {
                System.out.println("Accepted new connection: " + client);
            }
            ServerWorker sw = new ServerWorker(this, client, clientCount, silent, timeLimit);
            worker.add(sw);
            sw.start();
        }

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
            if (!silent) {
                System.out.println(GameMap.getInstance().toString());
            }
        	try {
                worker.get((turn + 1) % 2).handleMove();
            } catch (IOException e) {
                e.printStackTrace();
            }

            turn = (turn + 1) % 2;
        }

        // announce winner & game end
        endGame(MapUtil.maxNumberOfStones(GameMap.getInstance()));
    }

    // -----------------------------------------------  Getter/Setter
    ArrayList<ServerWorker> getWorker() {
        return worker;
    }

    void endGame(char winner) {
        if (!silent) {
            System.out.println("Player " + winner + " has won.");
        }
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

        System.exit(0);
    }
}
