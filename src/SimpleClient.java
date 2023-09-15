import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

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
            Thread.sleep(userResponseTime);

            runProtocol(in, out, connection);

        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception: " + e.getMessage());
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
            byte[] buffer = new byte[80];
            int b = 0;
            if (System.in.available() > 0) {
                b = System.in.read(buffer);
            }

            if (b > 0) {
                out.write(buffer, 0, b);
            }

            Thread.sleep(userResponseTime);
            buffer = new byte[80];
            b = in.read(buffer);

//            if (b > 0) {
//                String s = new String(buffer);
//                System.out.println("-> " + s);
//            }

            connection.close();
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }
}
