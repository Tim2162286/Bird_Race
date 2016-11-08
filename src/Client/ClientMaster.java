package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Tim on 10/24/2016.
 */
public class ClientMaster implements Runnable {
    static final int PORT = 29517;
    static final String HOSTNAME = "jonbush.net";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    public void run() {
        try {
            clientSocket = new Socket(HOSTNAME, PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Throwable e) {
            System.out.println("There was an error connecting to the server.");
        }
    }

    public int getGameId() {
        try {
            out.println("gameid");
            return Integer.parseInt(in.readLine());
        } catch (IOException | NumberFormatException e) {
            return -1;
        }
    }

    public boolean setHandle(String handle) {
        try {
            out.println("handle " + handle.trim().replace(' ','_'));
            return in.readLine().equals("valid");
        } catch (IOException e) {
            return false;
        }
    }
}
