import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMaster {
    private static final int PORTNUM = 29517;

    public static void main(String[] argv) {
        try {
            ServerSocket serverSocket = new ServerSocket(29517);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerThread clientThread = new ServerThread(clientSocket);
                (new Thread(clientThread)).start();
            }
        }
        catch (Throwable e) {
            System.out.println(e);
        }
    }
}