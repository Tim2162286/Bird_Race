package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Tim on 10/24/2016.
 */
public class ClientMaster implements Runnable {
    static final int PORT = 29517;
    static final String HOSTNAME = "localhost";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private ArrayList<String> commandQueue;

    private String[] playerNames;
    private int[] playerPositions;
    private int[] playerFinishTimes;
    private int gameId;
    private boolean ready;

    public ClientMaster() throws IOException{
        clientSocket = new Socket(HOSTNAME, PORT);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        commandQueue = new ArrayList<String>();
        ready = false;
    }

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
                        case "getstatus":
                            String[] positions = serverResponse.split("\\s");
                            this.playerPositions = new int[positions.length];
                            for (int i = 0; i < positions.length; i++) {
                                this.playerPositions[i] = Integer.parseInt(positions[i]);
                            }
                            break;

                        case "getscores":
                            String[] scores = serverResponse.split("\\s");
                            this.playerPositions = new int[scores.length];
                            for (int i = 0; i < scores.length; i++) {
                                this.playerPositions[i] = Integer.parseInt(scores[i]);
                            }
                            break;

                        case "ready":
                            this.ready = serverResponse.equals("start");
                            break;

                        case "setid":   // Set the ID of the player, useful for reconnecting
                            // no action required
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

    public void requestGameId() {
        commandQueue.add("gameid");
    }

    public int getGameId() {
        return this.gameId;
    }


    public void setHandle(String handle) {
        commandQueue.add("handle " + handle.trim().replace(' ','_'));
    }


    public void requestUsers() {
        commandQueue.add("users");
    }

    public String[] getUsers() {
        return this.playerNames;
    }

    public boolean isReady() {
        System.out.println(backlog() + " " + commandQueue.size());
        if(!backlog())
            commandQueue.add("ready");
        return this.ready;
    }

    public void setFinishTime(int time) {
        commandQueue.add("finished " + time);
    }

    public void disconnect() {
        commandQueue.add("disconnect");
    }

    public boolean backlog() {
        if (commandQueue.size() > 0)
        System.out.println(commandQueue.get(0));
        return commandQueue.size() > 0;
    }
}
