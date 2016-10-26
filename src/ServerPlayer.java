import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by jon on 10/24/2016.
 */
public class ServerPlayer implements Runnable {

    private Socket playerSocket;    // The socket to which the remote player client is connected
    private int playerId;           // The player's unique numeric identifier
    private String handle;          // The player's nickname or handle
    private ServerGame game;        // The game that this player is a member of
    private GameState gameState;    // Current state of the game
    private int playerNum;          // Which player in the current game
    private boolean ready;          // Initial communication received from server
    private Thread playerThread;    // Thread for this player

    /**
     * Creates a new ServerPlayer with a random playerId and the default handle
     * @param playerSocket the Socket to which the player is connected
     */
    public ServerPlayer(Socket playerSocket) {
        this.playerSocket = playerSocket;
        this.playerId = (int)(Math.random() * Integer.MAX_VALUE);
        this.handle = "default";
        this.playerThread = new Thread(this);
        this.playerThread.start();
    }

    public boolean attachState(GameState gameState) {
        this.gameState = gameState;
        return true;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getPlayerNum() {
        return this.playerNum;
    }

    public boolean isConnected() {
        return playerThread.isAlive();
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
                        try {
                            this.gameState.setPosition(this.playerNum, Integer.parseInt(command[1]));
                            response = "";
                            for (int i = 0; i < gameState.getNumPlayers(); i++) {
                                response += gameState.getPosition(i) + " ";
                            }
                            response = response.trim();
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Exception at status command: " + e);
                            response = "invalid";
                        }
                        break;

                    case "ready":
                        this.ready = true;
                        response = "waiting";
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
        }  catch (IOException e) {
            System.out.println("An error has occurred: " + e);
        }
    }
}
