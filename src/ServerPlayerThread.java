import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by jon on 10/24/2016.
 */
public class ServerPlayerThread implements Runnable {

    private Socket playerSocket;    // The socket to which the remote player client is connected
    private int playerId;       // The player's unique numeric identifier
    private String handle;      // The player's nickname or handle
    private ServerGame game;    // The game that this player is a member of

    /**
     * Creates a new ServerPlayerThread with a random playerId and the default handle
     * @param playerSocket the Socket to which the player is connected
     */
    public ServerPlayerThread(Socket playerSocket) {
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
                    case "status":  //Update the player's status on the server
                        response = "not_implemented";
                        break;

                    case "setid":   // Set the ID of the player, useful for reconnecting
                        try {
                            this.playerId = Integer.parseInt(command[1]);
                            response = "valid";
                        } catch (IndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("Exception at clientid command: " + e);
                            response = "invalid";
                        }
                        break;

                    case "handle":  // Set the handle of the player
                        try {
                            this.handle = command[1];
                            response = "valid";
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Error at handle command " + e);
                            response = "invalid";
                        }
                        break;

                    case "disconnect":  // Disconnect from the server
                        disconnect = true;
                        response = "closing";
                        break;

                    default:
                        response = "invalid_command";

                }
                out.println(response);
                //System.out.println(inputLine);
            }
        }  catch (Throwable e) {
            System.out.println("There was an error in the ServerPlayerThread");
        }
    }
}
