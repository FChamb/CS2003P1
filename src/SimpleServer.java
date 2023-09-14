import java.io.IOException;
import java.net.ServerSocket;

public class SimpleServer {

    private static int port;
    private static ServerSocket server;

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new ArrayIndexOutOfBoundsException("\n SimpleServer <port> \n");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        port = Integer.parseInt(args[0]);
        startServer();

    }

    public static void startServer() {
        try {
            server = new ServerSocket(port);
            System.out.println("--> Starting Server " + server.toString() + " <--");
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }
}
