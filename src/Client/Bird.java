package Client;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by Tim on 11/4/2016.
 */
public class Bird implements Runnable {
    private Ellipse2D bird;
    private double yVel;
    private double yPos;
    private static final double yAccel = 400;
    private static final int birdDiameter = 60;
    private static int updateDelay;
    private int width;
    private int height;
    private int bottomHeight;
    private boolean crashed = false;

    public Bird(int width, int height, int bottomHeight, int updateDelay){
        this.width = width;
        this.height = height;
        this.bottomHeight = bottomHeight;
        yPos = this.height /2;
        Bird.updateDelay = updateDelay;
        bird = new Ellipse2D.Double(birdDiameter, birdDiameter, this.width /4-10,yPos);
    }

    public void flap(){yVel -= 300;}

    public boolean update(){
        //crashed = false;
        yPos += yVel * ((double) updateDelay / 1000.);
        yVel += yAccel * ((double) updateDelay / 1000.);
        synchronized (this) {
            if (yPos < 0) {
                yPos = 0;
                yVel = 0;
                bird.setFrame(width / 4 - 10, yPos, birdDiameter, birdDiameter);
                return false;
            } else if (yPos > height - bottomHeight - birdDiameter) {
                yPos = height - bottomHeight - birdDiameter;
                yVel = 0;
                System.out.println("hit at " + yPos);
                bird.setFrame(width / 4 - 10, yPos, birdDiameter, birdDiameter);
                return false;
            } else {
                bird.setFrame(width / 4 - 10, yPos, birdDiameter, birdDiameter);
                return true;
            }
        }
    }

    public Shape getShape(){
        return bird;
    }

    //If bird has crashed then there is a slight pause before restarting
    public synchronized void pause() {
        bird.setFrame(width /4-10, yPos, birdDiameter, birdDiameter);
        long current = System.currentTimeMillis();
        while(System.currentTimeMillis() < current + 3000){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) { }
        }
        yVel = 0;
        yPos = 240;
    }

    //Returns True if bird has crashed with obstacle, ceiling, or floor
    public boolean crashed(){
        return crashed;
    }

    public void run() {
        while(!false) {
            if(!this.update()) {
                crashed = true;
                this.pause();
                crashed = false;
            }
            try {
                Thread.sleep(updateDelay);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
    }

}
