package Client;

import java.awt.*;
import java.util.Random;

/**
 * Created by Tim on 10/24/2016.
 */
public abstract class ObstacleMasterClass {
    protected static Random generator;
    protected int width;
    protected int xPos;
    protected int oppeningHeight;
    protected int oppeningPos;
    protected static int HEIGHT;
    protected static int BOTTOM_HEIGHT;
    protected static int UPDATE_DELAY;
    protected static int lastXPos = 0;
    protected static final int xVel = 100;
    protected boolean remove(){return (xPos+width)<0;}

    protected ObstacleMasterClass(Random random, int windowHeight, int bottomHeight, int updateDelay){
        generator = random;
        HEIGHT = windowHeight;
        BOTTOM_HEIGHT = bottomHeight;
        UPDATE_DELAY = updateDelay;
        lastXPos += generator.nextInt(100) + 150;
        oppeningHeight = random.nextInt(60)+180;
    }
}
