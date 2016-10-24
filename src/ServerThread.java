import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jon on 10/24/2016.
 */
public class ServerThread implements Runnable {

    private Socket playerSocket;

    public ServerThread(Socket playerSocket) {
        this.playerSocket = playerSocket;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                outputLine = "echo: " + inputLine;
                out.println(outputLine);
                System.out.println(inputLine);
                if (inputLine.equals("Bye")) {
                    break;
                }
            }
        }  catch (Throwable e) {}
    }
}
