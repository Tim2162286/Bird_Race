package Client;

import java.awt.*;
import java.util.Random;

/**
 * Created by Tim on 10/24/2016.
 */
public abstract class ObstacleMasterClass {
    protected static Random generator;
    protected static Graphics2D g2;
    protected int width;
    protected int xPos;
    protected int topHeight;
    protected int bottomHeight;
    protected int openingHeight;
    protected int openingPos;
    protected static int HEIGHT;
    protected static int BOTTOM_HEIGHT;
    protected static int UPDATE_DELAY;
    protected static int lastXPos = 0;
    protected static final int xVel = 100;
    protected boolean remove(){return (xPos+width)<0;}

    public ObstacleMasterClass(Random random, int windowHeight, int bottomHeight, int updateDelay){
        generator = random;
        HEIGHT = windowHeight;
        BOTTOM_HEIGHT = bottomHeight;
        UPDATE_DELAY = updateDelay;
        lastXPos += generator.nextInt(100)+150;
        xPos = lastXPos;
        width = random.nextInt(200)+100;
        lastXPos += width;
        openingHeight = random.nextInt(60)+180;
        openingPos = random.nextInt(HEIGHT-BOTTOM_HEIGHT-openingHeight)+(openingHeight/2);
        topHeight = openingPos-openingHeight/2;
        this.bottomHeight = openingPos+openingHeight/2;

    }

    public void paint(Graphics2D graphics){}
}
