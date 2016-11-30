package Client;

import java.awt.*;
import java.util.Random;

/**
 * ObstacleMasterClass is an abstract class used as a base for creating obstacles of various types.
 * Additionally contains code for generating the common fields used by all obstacle types.
 *
 * @author Tim
 * @since 10/24/2016.
 */
public abstract class ObstacleMasterClass {
    protected Graphics2D g2;
    private static double lastXPos = 0;
    protected int width;
    protected double xPos;
    protected int topHeight;
    protected int bottomHeight;
    protected int openingHeight;
    protected int openingPos;
    protected int HEIGHT;
    protected int BOTTOM_HEIGHT;
    protected int UPDATE_DELAY;
    protected final int xVel = 250;
    private int backup = 400;

    /**
     * Constructor used to generate fields coman to any obstacle type, including x position of the obstacle,
     * width of obstacle, and size and position of the opening for the bird to fly through.
     * @param random The random number generator to be used for obstacle generation.
     * @param windowHeight The Height of the game window.
     * @param bottomHeight The height of the space used at the bottom of the game window.
     * @param updateDelay The amount of time in milliseconds between game updates.
     */
    public ObstacleMasterClass(Random random, int windowHeight, int bottomHeight, int updateDelay){
        HEIGHT = windowHeight;
        BOTTOM_HEIGHT = bottomHeight;
        UPDATE_DELAY = updateDelay;
        lastXPos += random.nextInt(115)+210;
        xPos = lastXPos;
        width = random.nextInt(200)+100;
        lastXPos += width;
        openingHeight = random.nextInt(60)+180;
        openingPos = random.nextInt(HEIGHT-BOTTOM_HEIGHT-openingHeight)+(openingHeight/2);
        topHeight = openingPos-openingHeight/2;
        this.bottomHeight = openingPos+openingHeight/2;

    }

    /**
     * Checks if the obstacle should be removed
     * @return true if it should be removed, else false.
     */
    public boolean remove(){return (xPos+width)<0;}

    /**
     * Paint method prototype, must be implemented in derived obstacle class.
     * @param graphics The Graphics2D object to be used in the paint method.
     */
    public abstract void paint(Graphics2D graphics);

    /**
     * Changes the x position of the obstacle object in a normal game tick,
     * also updates lastXPos for generation of future obstacles.
     * @param numObstacles The number of obstacles that will be updated in current tick.
     *                     Should be the size of the current obstacle list.
     */
    public void update(int numObstacles){
        xPos -=((double)UPDATE_DELAY/1000)*xVel;
        lastXPos -=(((double)UPDATE_DELAY/1000)*xVel)/numObstacles;
    }

    /**
     * Method prototype for collision detection to be implemented in derived obstacle classes.
     * @param bird The shape to check collision on, should be the bird.
     * @return Boolean flag, true if collided, false if not.
     */
    public abstract boolean isCollided(Shape bird);

    /**
     * Backs the obstacle up by the width of the game window, should be called on all
     * obstacles currently in existence whenever a collision is detected.
     */
    public void reset(){
        xPos += backup;
        lastXPos += backup/4;
    }

    public void moveObstacleBack(int px) {
        xPos += px;
    }

    public void moveLastXPosBack(int px) {
        lastXPos += px;
    }

    public int getXPos() {
        return (int)xPos;
    }
}
