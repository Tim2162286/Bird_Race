import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jon on 10/24/2016.
 */
public class ServerThread implements Runnable {

    private Socket playerSocket;
    private int playerId;
    private String handle;

    public ServerThread(Socket playerSocket) {
        this.playerSocket = playerSocket;
        this.playerId = (int)(Math.random() * Integer.MAX_VALUE);
        this.handle = "default";
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            String inputLine, outputLine;
            boolean disconnect = false;
            while (!disconnect && (inputLine = in.readLine()) != null) {
                String[] command = inputLine.split("\\s+");
                String response = null;
                switch (command[0]) {
                    case "status":
                        response = "not_implemented";
                        break;
                    case "setid":
                        try {
                            this.playerId = Integer.parseInt(command[1]);
                            response = "valid";
                        } catch (IndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("Exception at clientid command: " + e);
                            response = "invalid";
                        }
                        break;

                    case "handle":
                        try {
                            this.handle = command[1];
                            response = "valid";
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Error at handle command " + e);
                            response = "invalid";
                        }
                        break;

                    case "disconnect":
                        disconnect = true;
                        response = "closing";
                        break;

                    default:
                        response = "invalid_command";

                }
                out.println(response);
                //System.out.println(inputLine);
            }
        }  catch (Throwable e) {}
    }
}
