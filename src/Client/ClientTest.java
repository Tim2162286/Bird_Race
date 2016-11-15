package Client;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by jebush2 on 11/8/2016.
 */
public class ClientTest {



    public static void main(String[] args) {
        ClientMaster client;
        try {
            client = new ClientMaster();
            (new Thread(client)).start();
            /*while (!client.isReady()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    //thread interrupted
                }
            }*/
            Scanner in = new Scanner(System.in);
            String inputLine = in.nextLine();
            while (!inputLine.equals("exit")) {
                String[] command = inputLine.split("\\s+");
                switch (command[0]) {
                    case "backlog":
                        System.out.println("\t " + client.backlog());
                        break;

                    case "updateObstaclesPassed":
                        client.updateObstaclesPassed(Integer.parseInt(command[1]));
                        System.out.println("\tUpdating obstacles passed");
                        break;

                    case "requestObstaclesPassed":
                        client.requestObstaclesPassed();
                        System.out.println("\tRequested obstacles passed update from server");
                        break;

                    case "getObstaclesPassed":
                        System.out.println("\tGot obstacles passed: " +Arrays.toString(client.getObstaclesPassed()));

                        break;

                    case "requestGameId":  //Update the player's status on the server
                        client.requestGameId();
                        System.out.println("\tRequested Game ID");
                        break;

                    case "getGameId":
                        System.out.println("\tGot Game ID: " + client.getGameId());
                        break;

                    case "setHandle":
                        client.setHandle(command[1]);
                        System.out.println("\tSet handle");
                        break;

                    case "requestHandles":
                        client.requestHandles();
                        System.out.println("\tRequested list of handles from server");
                        break;

                    case "getHandles":
                        System.out.println("\tGot Handles: " + Arrays.toString(client.getHandles()));
                        break;

                    case "requestFinishTimes":
                        client.requestFinishTimes();
                        System.out.println("\tRequested finish times from server");
                        break;

                    case "getFinishTimes":
                        System.out.println("\tGot finish times: " + Arrays.toString(client.getFinishTimes()));
                        break;

                    case "isReady":
                        System.out.println("\tThe game is " + ((client.isReady()) ? "ready" : "not ready"));
                        break;

                    case "setFinishTime":
                        client.setFinishTime(Integer.parseInt(command[1]));
                        System.out.println("\tSet player finish time");
                        break;

                    case "disconnect":  // Disconnect from the server
                        client.disconnect();
                        System.out.println("\tDisconnecting from server");
                        break;

                    default:
                        System.out.println("\tCommand not recognized");

                }
                inputLine = in.nextLine();
            }

        } catch (IOException e) {
            System.out.println("There was an error connecting to the server.");
        }

    }
}
