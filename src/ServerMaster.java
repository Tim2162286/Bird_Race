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
            String inputLine;
            ServerSocket serverSocket = new ServerSocket(29517);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                String outputLine = "echo: " + inputLine;
                out.println(outputLine);
                if (!outputLine.equals("Bye")) continue;
                break;
            }
        }
        catch (Throwable e) {
            System.out.println(e);
        }
    }
}