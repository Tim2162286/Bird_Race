import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Tim on 10/24/2016.
 */
public class ClientMasterClass {
    static final int PORT = 29517;
    static final String HOSTNAME = "localhost";

    public static void main(String[] argv) {
        try {
            Socket clientSocket = new Socket(HOSTNAME, PORT);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String userInput, serverResponse = null;
            while ((userInput = stdin.readLine()) != null && !serverResponse.equals("closing")) {
                out.println(userInput);
                System.out.println(serverResponse = in.readLine());
            }
        } catch (Throwable e) { /* Nothing */ }
    }
}
