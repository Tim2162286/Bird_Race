import java.util.ArrayList;

/**
 * Created by jon on 10/25/2016.
 */
public class ServerGame implements Runnable {

    private static final int MAX_PLAYERS = 10;
    private static final long JOIN_TIME = 10000; // Wait this long (ms) for more people to join

    private ArrayList<ServerPlayer> players;
    private GameState gameState;
    private long lastJoinTime;
    private int gameId;
    private boolean open;

    /**
     * Create a new game instance on the server
     */
    public ServerGame() {
        this.gameId = (int)(Math.random() * Integer.MAX_VALUE);
        this.open = true;
        this.players = new ArrayList<ServerPlayer>(MAX_PLAYERS);
    }

    public boolean addPlayer(ServerPlayer player) {
        if (this.players.size() < MAX_PLAYERS && this.open) {
            this.players.add(player);
            this.lastJoinTime = System.currentTimeMillis();
            this.open = players.size() < MAX_PLAYERS;
            player.setPlayerNum(players.size());
            System.out.println("New player added to game");
            return true;
        }
        return false;
    }

    /**
     * Check if the game is still accepting players
     * @return true if game is open
     */
    public boolean isOpen() {
        return this.open;
    }

    public void run() {
        while (players.size() < 2 || (System.currentTimeMillis() - lastJoinTime) < JOIN_TIME) {
            try {
                Thread.sleep(500);  // Only check every half second
                //System.out.println("Waiting for more players");

                for (int i = 0; i < players.size(); i++) {
                    if (!players.get(i).isConnected()) {
                        players.remove(i);
                        System.out.println("Player " + i + " removed");
                        for (int j = 0; j < players.size(); j++) {
                            players.get(j).setPlayerNum(j);
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
        this.open = false;
        this.gameState = new GameState(gameId, players.size());
        for (int i = 0; i < players.size(); i++) {
            players.get(i).attachState(gameState);
            players.get(i).setPlayerNum(i);
        }
        System.out.println("Game started");
        /* game stuff */
    }


}