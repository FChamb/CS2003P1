import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SimpleClient {

    private String IPAddress = "";
    private String portnumber = "";

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
            System.err.println("IO Exception: " + e.getMessage());
        }
        return connection;
    }
}
