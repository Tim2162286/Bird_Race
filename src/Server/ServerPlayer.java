package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Represents and individual player on the server side
 * The ServerPlayer handles communication between the client and server,
 * with one thread allocated per connection.
 *
 * @author Jonathan Bush
 * @since 24 October 2016
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
    private long finishTime;        // Score for this player

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
        this.finishTime = -1;
    }

    /**
     * Attach this player to a game state
     * @param gameState The game state to attach
     * @return true if the action was completed successfully
     */
    public boolean attachState(GameState gameState) {
        this.gameState = gameState;
        this.gameState.setHandle(this.playerNum, this.handle);
        return true;
    }

    /**
     * Set the number of this player
     * @param playerNum Number of the player in the game
     */
    public void setPlayerNum(int playerNum) {

        this.playerNum = playerNum;
        this.playerId = playerNum;
    }

    /**
     * Get the number of this player
     * @return player number
     */
    public int getPlayerNum() {
        return this.playerNum;
    }

    public boolean isConnected() {
        return playerThread.isAlive();
    }

    /**
     * Gets the time in milliseconds that it took the player to finish
     * @return time in milliseconds, -1 if not finished
     */
    public long getTime() {
        return this.finishTime;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            String inputLine, response;
            boolean disconnect = false;
            while (!disconnect && (inputLine = in.readLine()) != null) {
                String[] command = inputLine.split("\\s+");
                response = "error";
                switch (command[0]) {   // Perform the action corresponding to the command issued by client
                    case "status":  //Update the player's status on the server
                        try {
                            System.out.println("Setting status of player " + playerNum);
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

                    case "getstatus":
                        response = "";
                        for (int i = 0; i < gameState.getNumPlayers(); i++) {
                            response += gameState.getPosition(i) + " ";
                        }
                        response = response.trim();
                        break;

                    case "getscores":
                        response = "";
                        for (int i = 0; i < gameState.getNumPlayers(); i++) {
                            response += gameState.getTime(i) + " ";
                        }
                        response = response.trim();
                        break;

                    case "ready":
                        this.ready = true;
                        while (this.gameState == null) {
                            //System.out.println("Game state is: " + gameState);
                            try {
                                Thread.sleep(50);

                            } catch (InterruptedException e) {}
                        }
                        response = "start";
                        break;

                    case "setid":   // Set the ID of the player, u seful for reconnecting
                        try {
                            this.playerId = Integer.parseInt(command[1]);
                            response = "valid";
                        } catch (IndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("Exception at clientid command: " + e);
                            response = "invalid";
                        }
                        break;

                    case "getid":
                            response = Integer.toString(this.playerId);
                        break;

                    case "handle":  // Set the handle of the player
                        try {
                            this.handle = command[1];
                            if (this.gameState != null) {
                                this.gameState.setHandle(this.playerNum, this.handle);
                            }
                            response = "valid";
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Error at handle command " + e);
                            response = "invalid";
                        }
                        break;

                    case "users":
                        if (this.gameState != null) {
                            response = this.gameState.getHandles();
                        } else {
                            response = "game_not_initialized";
                        }
                        break;

                    case "finished":
                        try {
                            this.finishTime = Long.parseLong(command[1]);
                            this.gameState.setFinishTime(this.playerNum, this.finishTime);
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            System.out.println("\"finished\" command called improperly");
                        }
                        break;

                    case "gameid":
                        try {
                            response = Integer.toString(gameState.getGameId());
                        } catch (NullPointerException e) {
                            response = "game_not_initialized";
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
            }
        }  catch (IOException e) {
            System.out.println("An error has occurred: " + e);
        }
    }
}
