package Client;

import java.awt.*;
import java.util.Random;

/**
 * Created by Tim on 10/24/2016.
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
    protected final int xVel = 750;
    private int backup = 400;

    public boolean remove(){return (xPos+width)<0;}

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

    public void paint(Graphics2D graphics){}
    public void update(int numObstacles){
        xPos -=((double)UPDATE_DELAY/1000)*xVel;
        lastXPos -=(((double)UPDATE_DELAY/1000)*xVel)/numObstacles;
    }
    public int getWidth(){return width;}

    public abstract boolean isCollided(Shape bird);

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
