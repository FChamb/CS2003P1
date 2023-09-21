import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    private String IPAddress = "";
    private String portnumber = "";
    static int socketTimeout = 5000;
    private static String[] protocol = new String[]{"HELLO ADVISER", "HELLO ADVISEE",
            "ADVISE ME ON TO CS2003", "YOU ARE ADVISED ON TO CS2003", "THANK YOU",
            "YOU'RE WELCOME"};

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new ArrayIndexOutOfBoundsException("\n SimpleClient <IPAddress> <portnumber> \n");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try {
            Socket connection = startClient(args[0], args[1]);
            connection.setSoTimeout(socketTimeout);

            runProtocol(connection);

        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }

    public static Socket startClient(String IPAddress, String portnumber) {
        Socket connection = null;
        try {
            InetAddress address = InetAddress.getByName(IPAddress);
            int port = Integer.parseInt(portnumber);

            connection = new Socket(address, port);
            connection.setSoTimeout(10);

            System.out.println("--> Connection to " + connection.toString() + " <--");
        } catch (IOException e) {
            System.out.println("Connection refused");
            System.exit(1);
        }
        return connection;
    }

    public static void runProtocol(Socket connection) {
        try {
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintStream print = new PrintStream(connection.getOutputStream(), true);
            String clientResponse, serverResponse = "";
            int protoNumber = 0;

            while (protoNumber < protocol.length) {
                clientResponse = protocol[protoNumber];
                if (protoNumber == 0 || serverResponse.equals(protocol[protoNumber - 1])) {
                    System.out.println("Input: " + clientResponse);
                    print.println(clientResponse);
                    protoNumber += 2;
                } else {
                    clientResponse = protocol[protoNumber - 2];
                    System.out.println("Input: " + clientResponse);
                    print.println(clientResponse);
                }
                serverResponse = serverInput.readLine();
                System.out.println("Output: " + serverResponse);
            }

            connection.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
