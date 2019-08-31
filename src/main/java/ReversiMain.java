import network.Server;

/**
 * Created by Marc Luque on 03.04.2019.
 */
class ReversiMain {
    public static void main(String[] args) {
        int port = 8080;

        new Server(port).start();
    }
}