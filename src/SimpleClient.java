import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    private String IPAddress = "";
    private String portnumber = "";
    static int userResponseTime = 5000;

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
            Scanner userInput = new Scanner(System.in);
            Scanner serverResponse = new Scanner(new InputStreamReader(connection.getInputStream()));
            System.out.println("Enter:");
            String response = userInput.nextLine();
            PrintStream p = new PrintStream(connection.getOutputStream());
            p.println(response);
            Thread.sleep(1000);
            String serverResp = serverResponse.nextLine();
            System.out.println(serverResp);
            connection.close();
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }
}
