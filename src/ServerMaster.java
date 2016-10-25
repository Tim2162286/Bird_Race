import java.net.ServerSocket;
import java.net.Socket;

public class ServerMaster {
    private static final int PORTNUM = 29517;

    public static void main(String[] argv) {
        try {
            ServerSocket serverSocket = new ServerSocket(29517);
            ServerGame newGame = new ServerGame();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerPlayer newPlayer = new ServerPlayer(clientSocket);
                if (!newGame.isOpen() || newGame.addPlayer(newPlayer)) {
                    // If the game is closed or the player can't be added, create a new game and add the player
                    (newGame = new ServerGame()).addPlayer(newPlayer);
                }
                (new Thread(newPlayer)).start();

            }
        }
        catch (Throwable e) {
            System.out.println(e);
        }
    }
}