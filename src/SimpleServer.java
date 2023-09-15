import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

        try {
            Socket connection = server.accept();
            OutputStream out = connection.getOutputStream();
            InputStream in = connection.getInputStream();
            server.close();

            System.out.println("New connection ... " + connection.getInetAddress().getHostName() + ":" + connection.getPort());

            runProtocol(in, out, connection);

        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }

    public static void startServer() {
        try {
            server = new ServerSocket(port);
            System.out.println("--> Starting Server " + server.toString() + " <--");
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }

    public static void runProtocol(InputStream in, OutputStream out, Socket connection) {
        try {
            byte[] buffer = new byte[80];
            int b = 0;
            while (b < 1) {
                Thread.sleep(100);
                buffer = new byte[80];
                b = in.read(buffer);
            }

            if (b > 0) {
                String s = new String(buffer);
                System.out.println("-> " + s);
                out.write(buffer, 0, b);
                connection.close();
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }
}
