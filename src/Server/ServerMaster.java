package Server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Server.ServerMaster handles new incoming connections to the server.
 * A new instance of ServerPlayer is created for each connection, and is passed to the current open game.
 *
 * @author Jonathan Bush
 * @sincee 23 October 2016
 */

public class ServerMaster {
    private static final int PORTNUM = 29517;   // Port to listen for connections on

    /**
     * Loop continuously, adding new players to the currently open game, creating new game when closed
     * @param argv
     */
    public static void main(String[] argv) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORTNUM);
            ServerGame newGame = new ServerGame();
            (new Thread(newGame)).start();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerPlayer newPlayer = new ServerPlayer(clientSocket);
                System.out.println("Spawned new player");
                if (!newGame.isOpen() || !newGame.addPlayer(newPlayer)) {
                    // If the game is closed or the player can't be added, create a new game and add the player
                    (newGame = new ServerGame()).addPlayer(newPlayer);
                    (new Thread(newGame)).start();
                    System.out.println("New game created");
                }
                // (new Thread(newPlayer)).start();

            }
        }
        catch (Throwable e) {
            System.out.println(e);
        }
    }
}