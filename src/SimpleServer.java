import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleServer {

    private static int port;
    private static ServerSocket server;

    private static String[] protocol = new String[]{"HELLO ADVISER", "HELLO ADVISEE",
            "ADVISE ME ON TO CS2003", "YOU ARE ADVISED ON TO CS2003", "THANK YOU",
            "YOU'RE WELCOME"};

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
            Scanner clientInput = new Scanner(connection.getInputStream());
            PrintStream print = new PrintStream(connection.getOutputStream());
            String clientResponse, serverResponse;
            boolean active = true;
            int protoNumber = 1;

            while (active) {
                if (!clientInput.hasNext()) {
                    Thread.sleep(5000);
                    continue;
                }
                clientResponse = clientInput.nextLine();
                serverResponse = protocol[protoNumber];
                System.out.println("Output: " + clientResponse);
                if (clientResponse.equals(protocol[protoNumber - 1])) {
                    System.out.println("Input: " + serverResponse);
                    print.println(serverResponse);
                    protoNumber += 2;
                } else {
                    System.out.println("Input: " + protocol[protoNumber - 1]);
                }
                if (protoNumber >= protocol.length) {
                    active = false;
                }
            }

            connection.close();
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }
}
