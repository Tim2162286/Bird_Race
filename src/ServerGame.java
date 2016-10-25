import java.util.ArrayList;

/**
 * Created by jon on 10/25/2016.
 */
public class ServerGame {

    private static final int MAX_PLAYERS = 10;

    private ArrayList<String> playerNames;
    private ArrayList<Integer> playerIds;
    private ArrayList<Integer> playerPositions;
    private int gameId;

    /**
     * Create a new game instance on the server
     */
    public ServerGame() {
        this.gameId = (int)(Math.random()*Integer.MAX_VALUE);

    }

    public int joinGame(String playerName, int playerId) {
        if (playerNames.size() < MAX_PLAYERS) {
            for (String name : playerNames) {
                if (name.equals(playerName)) {
                    return 2;   // Player name already taken
                }
            }
            for (int id : playerIds) {
                if (id == playerId) {
                    return 3;   // Player ID already taken
                }
            }
            this.playerNames.add(playerName);
            this.playerIds.add(playerId);
            return 0;   // Player joined successfully
        }
        return 1;   // There were already 10 players in the game
    }

    public String getPositions() {
        StringBuilder positions = new StringBuilder();
        for (int pos : playerPositions) {
            positions.append(pos);
            positions.append(" ");
        }
        return positions.toString().trim();
    }

    public synchronized void updatePosition(int playerId, int position) {
        int i = 0;
        for (; i < playerIds.size(); i++) {
            if (playerIds.get(i) == playerId) {
                break;
            }
        }
        if (i < playerIds.size()) {
            playerPositions.set(i, position);
        }
    }


}