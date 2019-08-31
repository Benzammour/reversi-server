import network.Server;

class ServerMain {
    public static void main(String[] args) {
        int port = 8080;
        boolean silent = false;
        String mapName = "";
        int timelimit = 1000;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p":
                case "--port":
                    if (args[i + 1] != null) {
                        port = Integer.parseInt(args[i + 1]);
                    } else {
                        System.out.println("Please provide a port.");
                    }

                    break;
                case "-s":
                case "--silent":
                    silent = true;

                    break;
                case "-m":
                case "--map":
                    if (args[i + 1] != null) {
                        mapName = args[i + 1];
                    } else {
                        System.out.println("Please provide a map.");
                    }

                    break;
                case "-h":
                case "--help":
                    System.out.println(
                        "Usage: java -jar SWP-Reversi-group1.jar [ -p | --port  port number ] [ -h | --help ]\n" +
                                "\n" +
                                "Arguments:\n" +
                                "  -p,  --port        custom port (standard: 8080)\n" +
                                "  -m,  --map         use custom map (provide path)\n" +
                                "  -s,  --silent      don't show any output from stdin/-err\n" +
                                "  -h,  --help        show help\n");
                    return;
            }
        }

        new Server(port, silent, mapName, timelimit).start();
    }
}