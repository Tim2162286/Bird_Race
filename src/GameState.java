import java.util.ArrayList;

/**
 * Created by jebush2 on 10/25/2016.
 */
public class GameState {

    private int[] playerPositions;
    private int numPlayers;
    private int gameId;

    public GameState(int gameId, int numPlayers) {
        this.gameId = gameId;
        this.numPlayers = numPlayers;
        this.playerPositions = new int[this.numPlayers];
    }

    /**
     * Set the position for the player at playerNum
     * @param playerNum Number of the player to change the position for
     * @param position New position to move to
     */
    public synchronized void setPosition(int playerNum, int position) {
        try {
            this.playerPositions[playerNum] = position;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Tried to set position with invalid player number: " + e);
        }
    }

    /**
     * Get the position of the player at playerNum
     * @param playerNum Player to get the position of
     * @return horizontal position of the player
     */
    public int getPosition(int playerNum) {
        try {
            return this.playerPositions[playerNum];
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Tried to get position with invalid player number: " + e);
            return -1;
        }
    }

    /**
     * Get the number of players in this game
     * @return Number of players in the game
     */
    public int getNumPlayers() {
        return this.numPlayers;
    }

    /**
     * Get the game ID to seed the map
     * @return unique identifier for this game
     */
    public int getGameId() {
        return gameId;
    }
}
