package Client;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tim on 11/4/2016.
 */
public class Bird {
    private Ellipse2D bird;
    private double yVel;
    private double yPos;
    private static final double yAccel = 400;
    private static final int BIRD_DIAMETER = 60;
    private static int UPDATE_DELAY;
    private int WIDTH;
    private int HEIGHT;
    private int BOTTOM_HEIGHT;
    private boolean crashed = false;

    public Bird(int width, int height, int bottomHeight, int updateDelay){
        WIDTH = width;
        HEIGHT = height;
        BOTTOM_HEIGHT = bottomHeight;
        yPos = HEIGHT/2;
        UPDATE_DELAY = updateDelay;
        bird = new Ellipse2D.Double(BIRD_DIAMETER,BIRD_DIAMETER,WIDTH/4-10,yPos);
    }

    public void flap(){yVel -= 300;}

    public void update(){
        crashed = false;
        yPos += yVel * ((double) UPDATE_DELAY / 1000.);
        yVel += yAccel * ((double) UPDATE_DELAY / 1000.);

        if (yPos <= 0) {
            yPos = 1;
            yVel = 0;
            crashed = true;
        } else if (yPos+BIRD_DIAMETER> HEIGHT-BOTTOM_HEIGHT) {
            yPos = HEIGHT-BOTTOM_HEIGHT-BIRD_DIAMETER;
            yVel = 0;
            crashed = true;
        } else {

        }
        bird.setFrame(WIDTH/4-10, yPos, BIRD_DIAMETER, BIRD_DIAMETER);
    }

    public Shape getShape(){
        return bird;
    }

    public void pause() {
        crashed = true;
        long current = System.currentTimeMillis();
        while(System.currentTimeMillis() < current + 3000){

        }
        yVel = 0;
        yPos = 240;
    }

    public boolean crashed(){
        return crashed;
    }
}
