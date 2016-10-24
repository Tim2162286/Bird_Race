import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Tim on 10/24/2016.
 */
public class ClientMasterClass {
    public static void main(String[] argv) {
        int port = 29517;
        try {
            Socket clientSocket = new Socket("192.168.1.112", port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = stdin.readLine()) != null) {
                out.println(userInput);
                System.out.println(in.readLine());
            }
        } catch (Throwable e) { /* Nothing */ }
    }
}
