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
    private int score = 0;
    private int GAME_LENGTH;

    public Bird(Random rand, int width, int height, int bottomHeight, int updateDelay, int maxObsticals){
        this.rand = rand;
        this.width = width;
        this.height = height;
        this.bottomHeight = bottomHeight;
        GAME_LENGTH = maxObsticals;
        yPos = this.height/2;
        Bird.updateDelay = updateDelay;
        bird = new Ellipse2D.Double(birdDiameter, birdDiameter, this.width /4-10,yPos);
        obstacleList = new ArrayList<ObstacleMasterClass>(4);
        for (int i=0;i<4;i++){
            obstacleList.add(new SquareObstacle(rand,height,bottomHeight,updateDelay));
        }
        for (ObstacleMasterClass obst : obstacleList) {
            obst.reset();
        }
    }


    public void flap(){yVel -= 300;}

    public void setScore(int Score){
        score = Score;
    }
    public int getScore(){
        return score;
    }

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
            score += 1;
            obstacleList.remove(0);
            if (score+obstacleList.size()<GAME_LENGTH)
                obstacleList.add(new SquareObstacle(rand,height,bottomHeight,updateDelay));
            if (obstacleList.size() == 0){}
                //End game
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
        yVel = -yAccel*((double)updateDelay/1000);
        if (yPos<=0 || yPos>=height-bottomHeight-birdDiameter)
            yPos = 240;
    }

    //Returns true if bird has crashed with obstacle, ceiling, or floor
    public boolean crashed(){
        return crashed;
    }

    public void run() {
        while(!false) {
            if(!this.update() || obstacleList.get(0).isCollided(bird)) {
                crashed = true;
                for (ObstacleMasterClass i:obstacleList){
                    i.reset();
                }
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
