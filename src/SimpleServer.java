import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleServer {

    private static int port;
    private static ServerSocket server;

    private static String[] protocol = new String[]{"HELLO ADVISER", "HELLO ADVISEE",
            "ADVISE ME ON TO CS2003", "YOU ARE ADVISED ON TO CS2003", "THANK YOU",
            "YOU'RE WELCOM", "VALID", "VALID"};

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
            server.close();

            System.out.println("New connection ... " + connection.getInetAddress().getHostName() + ":" + connection.getPort());

            runProtocol(connection);

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

    public static void runProtocol(Socket connection) {
        try {
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintStream print = new PrintStream(connection.getOutputStream(), true);
            String clientResponse, serverResponse;
            int protoNumber = 1;

            while (protoNumber <= protocol.length) {
                clientResponse = clientInput.readLine();
                serverResponse = protocol[protoNumber];
                if (!clientResponse.equals(protocol[protocol.length - 2]) && protoNumber != protocol.length - 1) {
                    System.out.println("Output: " + clientResponse);
                }
                if (clientResponse.equals(protocol[protoNumber - 1])) {
                    if (protoNumber == protocol.length - 1) {
                        break;
                    }
                    System.out.println("Input: " + serverResponse);
                    print.println(serverResponse);
                    protoNumber += 2;
                } else {
                    if (protoNumber == 1) {
                        throw new IOException("Error in protocol!");
                    }
                    serverResponse = protocol[protoNumber - 2];
                    System.out.println("Input: " + serverResponse);
                    print.println(serverResponse);
                }
            }

            connection.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
