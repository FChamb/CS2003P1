import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleServer {

    /**
     * The private integer, port, exists to enable easy access in the class. This is the same for
     * the ServerSocket object, server. The protocol array stores the valid arguments for the
     * specification defined communication protocol. This array also stores two extra values, "VALID".
     * These values are to ensure that the final message is valid before cutting the connection.
     */
    private static int port;
    private static ServerSocket server;

    private static String[] protocol = new String[]{"HELLO ADVISER", "HELLO ADVISEE",
            "ADVISE ME ON TO CS2003", "YOU ARE ADVISED ON TO CS2003", "THANK YOU",
            "YOU'RE WELCOM", "VALID", "VALID"};

    /**
     * The main method has two actions. It first grabs the command line argument and ensures that there
     * is only the appropriate item, port number. An array index out of bounds exception is
     * thrown if the arguments are not provided and a helpful hint message is supplied to the user.
     * The second action of the main method is to start the client connection using a socket with the user
     * designated port.
     * @param args the command line arguments - port number
     */
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
            System.out.println("Connection refused");
            System.exit(1);
        }
    }

    /**
     * Start server takes the user defined port number to create a server socket
     * connection point. An appropriate message is printed if the connection
     * is made and also if it is refused.
     */
    public static void startServer() {
        try {
            server = new ServerSocket(port);
            System.out.println("--> Starting Server " + server + " <--");
        } catch (IOException e) {
            System.out.println("Connection refused");
            System.exit(1);
        }
    }

    /**
     * This method takes a socket connection parameter and begins communication.
     * A try/catch loop is implemented to catch and errors that might occur in the protocol. A
     * buffered reader and print stream are created to receive and write data from and to the
     * server. Two strings are created for the client's and server's response. ProtoNumber is
     * an integer created to keep track of which protocol argument the message is currently on.
     * A while loop that executes until protoNumber reach max length enables the server to grab
     * the next valid message. First a condition statement ensures the last two "VALID" messages
     * are not print. Another conditional statement checks that the server response is equal
     * to protoNumber minus one which would be the appropriate previous message. There is one
     * final check which exists to exit and stop printing to the terminal if the message equals
     * one of the last two protocol messages, "VALID". For each iteration, the server's response
     * is printed and sent to the client. Should the received message not be valid the previous
     * message in the protocol will be printed. Finally, the connection is closed.
     * @param connection a sever socket which enables client connections
     */
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
