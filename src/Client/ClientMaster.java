package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The ClientMaster allows buffered communication with the server
 * When a request or set method is called, the appropriate command is added to the commandQueue.
 * After the previous exchange has been completed, the next command in the queue is issued and removed.
 * Requested data is retrieved with the corresponding get method.
 *
 * @author Jonathan Bush
 * @since 10/24/2016
 */
public class ClientMaster implements Runnable {
    static final int PORT = 29517;      // Port to connect to
    static final String HOSTNAME = "jonbush.net";   // Hostname to connect to
    private Socket clientSocket;        // The Socket used by this client
    private PrintWriter out;            // PrintWriter for sending commands
    private BufferedReader in;          // BufferedReader for receiving responses
    private ArrayList<String> commandQueue; // Holds the commands that need to be sent to the server
    //private int obstacleCount;
    private String[] playerNames;       // Names of all the players in join order
    private int[] obstaclesPassed;      // Number of obstacles passed in player join order
    private int[] playerFinishTimes;    // Finish times in player join order
    private int gameId;                 // gameId used for seeding Random object
    private boolean ready;              // Whether or not the game is ready to be started
    private int playerId;               // The index of this player

    /**
     * Initialize the ClientMaster
     * @throws IOException If connection with server was not successfully established
     */
    public ClientMaster() throws IOException{
        clientSocket = new Socket(HOSTNAME, PORT);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        commandQueue = new ArrayList<String>();     // Initialize all of the fields
        ready = false;
        this.obstaclesPassed = new int[] {0,0};
        this.playerFinishTimes = new int[] {0,0};
        this.playerNames = new String[] {"notset", "notset2"};
        this.gameId = 0;
        this.playerId = -1;
    }

    /**
     * Start running the ClientMaster thread to process commands added to the queue
     */
    public void run() {
        String serverResponse = "";
        String commandToSend;
        while (!serverResponse.equals("closing")) {
            try {
                Thread.sleep(100);
                //System.out.println("Here");
            } catch (InterruptedException e) {
                // something
            }
            //System.out.println(commandQueue.size());
            if (commandQueue.size() > 0) {
                commandToSend = commandQueue.remove(0);
                out.println(commandToSend);
                try {
                    serverResponse = in.readLine();
                    //System.out.println(serverResponse);
                    String[] command = commandToSend.split("\\s+");
                    switch (command[0]) {   // Perform the action corresponding to the command issued by client
                        case "status":  //Update the player's status on the server
                        case "getstatus":   // Update local status from server
                            String[] positions = serverResponse.split("\\s");
                            this.obstaclesPassed = new int[positions.length];
                            for (   int i = 0; i < positions.length; i++) {
                                this.obstaclesPassed[i] = Integer.parseInt(positions[i]);
                            }
                            break;

                        case "getscores":
                            String[] scores = serverResponse.split("\\s");
                            this.playerFinishTimes = new int[scores.length];
                            for (int i = 0; i < scores.length; i++) {
                                this.playerFinishTimes[i] = Integer.parseInt(scores[i]);
                            }
                            break;

                        case "ready":
                            this.ready = serverResponse.equals("started");
                            break;

                        case "setid":   // Set the ID of the player, useful for reconnecting
                            // no action required
                            break;

                        case "getid":
                            this.playerId = Integer.parseInt(serverResponse);
                            break;

                        case "handle":  // Set the handle of the player
                            // no action required
                            break;

                        case "users":
                            this.playerNames = serverResponse.split("\\s");
                            break;

                        case "finished":
                            // no action required
                            break;

                        case "gameid":
                            this.gameId = Integer.parseInt(serverResponse);
                            break;

                        case "disconnect":  // Disconnect from the server
                            // no action required
                            break;

                        default:


                    }
                } catch (IOException e) {
                    System.out.println("Error in server response");
                }
            }
        }
    }

    /**
     * Request Game ID from server
     */
    public void requestGameId() {
        commandQueue.add("gameid");
    }

    /**
     * Get the ID of this game
     * @return ID of the game, used for seeding Random
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     * Set the handle of this player on the server
     * @param handle String without whitespace. ' ' will be replaced with '_'
     */
    public void setHandle(String handle) {
        commandQueue.add("handle " + handle.trim().replace(' ','_'));
    }

    /**
     * Request the list of handles from the server in order of player id
     */
    public void requestHandles() {
        commandQueue.add("users");
    }

    /**
     * Get the list of names retrieved from the server
     * @return String array in player join order
     */
    public String[] getHandles() {
        return this.playerNames;
    }

    /**
     * Send how many obstacles this player has passed, and update how many obstacles everyone else has passed
     * @param numObstacles Number of obstacles passed by the player
     */
    public void updateObstaclesPassed(int numObstacles) {
        commandQueue.add("status " + numObstacles);
    }

    /**
     * Request for the number of obstacles passed by the other players to be updated
     */
    public void requestObstaclesPassed() {
        commandQueue.add("getstatus");
    }

    /**
     * Get the list of how many obstacles have been passed by each player
     * @return number of obstacles passed in player join order
     */
    public int[] getObstaclesPassed() {
        return this.obstaclesPassed;
    }

    /**
     * Check if the game is ready to start
     * @return true if the game can be started
     */
    public boolean isReady() {
        //System.out.println(backlog() + " " + commandQueue.size());
        if(!backlog())
            commandQueue.add("ready");
        return this.ready;
    }

    /**
     * Set the amount of time it took for this player to finish the course
     * @param time time in milliseconds
     */
    public void setFinishTime(long time) {
        commandQueue.add("finished " + time);
    }

    /**
     * Request for the finish times of all players to be updated
     */
    public void requestFinishTimes() {
        commandQueue.add("getscores");
    }

    /**
     * Get the list of finish times for all players
     * @return list  of finish times in player join order; 0 if not finished
     */
    public int[] getFinishTimes() {
        return this.playerFinishTimes;
    }

    /**
     * Request the index of this player
     */
    public void requestPlayerId() {
        commandQueue.add("getid");
    }

    /**
     * Get the index of this player in the arrays
     * @return index of this player
     */
    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * Disconnect from the server and cease communication
     */
    public void disconnect() {
        commandQueue.add("disconnect");
    }

    /**
     * Check if there are commands waiting in the queue
     * @return
     */
    public boolean backlog() {
        //if (commandQueue.size() > 0)
        //System.out.println(commandQueue.get(0));
        return commandQueue.size() > 0;
    }
}
