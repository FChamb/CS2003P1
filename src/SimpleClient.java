import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    private String IPAddress = "";
    private String portnumber = "";
    static int userResponseTime = 5000;
    static String LINEEND = "\r\n";
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
            OutputStream out = connection.getOutputStream();
            InputStream in = connection.getInputStream();
//            Thread.sleep(userResponseTime);

            runProtocol(in, out, connection);

        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
//        } catch (InterruptedException e) {
//            System.err.println("Interrupted Exception: " + e.getMessage());
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
            System.err.println("IO Exception: " + e.getMessage());
        }
        return connection;
    }

    public static void runProtocol(InputStream in, OutputStream out, Socket connection) {
        try {
            Scanner serverInput = new Scanner(new InputStreamReader(connection.getInputStream()));
            PrintStream p = new PrintStream(connection.getOutputStream());
            String clientResponse, serverResponse;
            boolean active = true;
            int protoNumber = 0;

            while (active) {
                clientResponse = protocol[protoNumber];
                System.out.println("Input: " + clientResponse);
                p.println(clientResponse);
//                p.println(clientResponse + LINEEND);
                p.flush();
//                Thread.sleep(1000);
                serverResponse = serverInput.nextLine();
                System.out.println("Output: " + serverResponse);
                protoNumber += 2;
                if (protoNumber >= protocol.length) {
                    active = false;
                }
            }

            connection.close();
//        } catch (InterruptedException e) {
//            System.err.println("Interrupted Exception: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }
}
