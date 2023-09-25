import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    /**
     * The private integer, socketTimeout, exists purely as a final value for the client to define
     * the socket timeout. This was implemented to prevent errors in communication and latency.
     * The protocol array stores the valid arguments for the specification defined communication
     * protocol. This array also stores two extra values, "VALID". These values are to ensure that
     * the final message is valid before cutting the connection.
     */
    static final int socketTimeout = 5000;
    private static String[] protocol = new String[]{"HELLO ADVISER", "HELLO ADVISEE",
            "ADVISE ME ON TO CS2003", "YOU ARE ADVISED ON TO CS2003", "THANK YOU",
            "YOU'RE WELCOME", "VALID", "VALID"};

    /**
     * The main method has two actions. It first grabs the command line argument and ensures that there
     * are the appropriate items, IP address and port number. An array index out of bounds exception is
     * thrown if these arguments are not provided and a helpful hint message is supplied to the user.
     * The second action of the main method is to start the client connection using a socket with the user
     * designated port and IP address.
     * @param args the command line arguments - IP Address and Port number
     */
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
            System.out.println("Connection refused");
            System.exit(1);
        }
    }

    /**
     * Start client takes the user defined IP address and port number to create a socket
     * connection with the server. An appropriate message is printed if the connection
     * is made and also if it is refused.
     * @param IPAddress the user defined IP address
     * @param portnumber the user defined port number
     * @return Socket connection for the client to communicate with server
     */
    public static Socket startClient(String IPAddress, String portnumber) {
        Socket connection = null;
        try {
            InetAddress address = InetAddress.getByName(IPAddress);
            int port = Integer.parseInt(portnumber);

            connection = new Socket(address, port);

            System.out.println("--> Connection to " + connection.toString() + " <--");
        } catch (IOException e) {
            System.out.println("Connection refused");
            System.exit(1);
        }
        return connection;
    }

    /**
     * This method takes a socket connection parameter and begins communication with the server.
     * A try/catch loop is implemented to catch and errors that might occur in the protocol. A
     * buffered reader and print stream are created to receive and write data from and to the
     * server. Two strings are created for the client's and server's response. ProtoNumber is
     * an integer created to keep track of which protocol argument the message is currently on.
     * A while loop that executes until protoNumber reach max length enables the client to grab
     * the valid next message. A conditional statement checks that the server response is equal
     * to protoNumber minus one which would be the appropriate previous message. If this is the
     * first message, then it is also passed. There is one final check which exists to exit and
     * stop printing to the terminal if the message equals one of the last two protocol messages,
     * "VALID". For each iteration, the client response is printed and sent to the server. Should
     * the received message not be valid the previous message in the protocol will be printed.
     * Finally, the connection is closed.
     * @param connection a socket object with a connection to the server
     */
    public static void runProtocol(Socket connection) {
        try {
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintStream print = new PrintStream(connection.getOutputStream(), true);
            String clientResponse, serverResponse = "";
            int protoNumber = 0;

            while (protoNumber < protocol.length) {
                clientResponse = protocol[protoNumber];
                if (protoNumber == 0 || serverResponse.equals(protocol[protoNumber - 1])) {
                    if (protoNumber == protocol.length - 2) {
                        print.println(clientResponse);
                        break;
                    }
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
