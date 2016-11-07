package Client;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tim on 11/4/2016.
 */
public class Bird implements Runnable {
    private Ellipse2D bird;
    private Random rand;
    private ArrayList<ObstacleMasterClass> obstacleList;
    private double yVel;
    private double yPos;
    private static final double yAccel = 400;
    private static final int birdDiameter = 60;
    private static int updateDelay;
    private static final int xPos = 60;
    private int width;
    private int height;
    private int bottomHeight;
    private boolean crashed = false;

    public Bird(Random rand, int width, int height, int bottomHeight, int updateDelay){
        this.rand = rand;
        this.width = width;
        this.height = height;
        this.bottomHeight = bottomHeight;
        yPos = this.height/2;
        Bird.updateDelay = updateDelay;
        bird = new Ellipse2D.Double(birdDiameter, birdDiameter, this.width /4-10,yPos);
        obstacleList = new ArrayList<ObstacleMasterClass>(4);
        for (int i=0;i<4;i++){
            obstacleList.add(new SquareObstacle(rand,height,bottomHeight,updateDelay));
        }
    }

    public void flap(){yVel -= 300;}

    public boolean update(){
        //crashed = false;
        boolean remove = false;
        yPos += yVel * ((double) updateDelay / 1000.);
        yVel += yAccel * ((double) updateDelay / 1000.);
        for (ObstacleMasterClass i:obstacleList){
            i.update(obstacleList.size());
            if (i.remove())
                remove = true;
        }
        if (remove){
            obstacleList.remove(0);
            obstacleList.add(new SquareObstacle(rand,height,bottomHeight,updateDelay));
        }
        synchronized (this) {
            if (yPos < 0) {
                yPos = 0;
                yVel = 0;
                bird.setFrame(xPos, yPos, birdDiameter, birdDiameter);
                return false;
            } else if (yPos > height - bottomHeight - birdDiameter) {
                yPos = height - bottomHeight - birdDiameter;
                yVel = 0;
                System.out.println("hit at " + yPos);
                bird.setFrame(xPos, yPos, birdDiameter, birdDiameter);
                return false;
            } else {
                bird.setFrame(xPos, yPos, birdDiameter, birdDiameter);
                return true;
            }
        }
    }

    public void paint(Graphics2D graphics){
        for (ObstacleMasterClass i:obstacleList){
            i.paint(graphics);
        }
        graphics.setColor((Color.red));
        graphics.fill(bird);
    }

    //If bird has crashed then there is a slight pause before restarting
    public synchronized void pause() {
        bird.setFrame(xPos, yPos, birdDiameter, birdDiameter);
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
