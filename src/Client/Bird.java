package Client;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Created by Tim on 11/4/2016.
 */
public class Bird implements Runnable {
    private Ellipse2D bird;
    private Random rand;
    private ArrayList<ObstacleMasterClass> obstacleList;
    private double yVel;
    public double yPos;
    private static final double yAccel = 420;
    private static final int birdDiameter = 60;
    private static int updateDelay;
    public static final int xPos = 60;
    private int width;
    private int height;
    private int bottomHeight;
    private boolean crashed = false;
    private boolean finished = false;
    private int score = 0;
    private int GAME_LENGTH;
    private BufferedImage birdImage;
    private TexturePaint birdImageTP;

    /**
     * Creates object type Bird and obstacles
     */
    public Bird(Random rand, int width, int height, int bottomHeight, int updateDelay, int maxObsticals){
        this.rand = rand;
        this.width = width;
        this.height = height;
        this.bottomHeight = bottomHeight;
        GAME_LENGTH = maxObsticals;
        yPos = this.height/2;
        Bird.updateDelay = updateDelay;
        bird = new Ellipse2D.Double(birdDiameter, birdDiameter, this.width /4-10,yPos);
        try{
            birdImage = ImageIO.read(getClass().getResourceAsStream("/Client/NewBird.png"));
        }
        catch(IOException e){}
        obstacleList = new ArrayList<ObstacleMasterClass>(4);
        for (int i=0;i<4;i++){
            obstacleList.add(new SquareObstacle(rand,height,bottomHeight,updateDelay));
        }
        for (ObstacleMasterClass obst : obstacleList) {
            obst.reset();
        }
    }

    /**
     * Bird velocity changes
     */
    public void flap(){yVel -= 320;}

    /**
     *@return score, current score
     */
    public int getScore(){
        return score;
    }

    /**
     * Updates velocity, obstacles, and position
     * @return boolean, determines if bird has hit bounds
     */
    public boolean update(){
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

    /**
     * Bird and Obstacles GUI.
     * Bird filled with newBird.png texture and obstacles are yellow
     */
    public void paint(Graphics2D graphics){
        for (short i = 0; i < obstacleList.size(); i++) {
            obstacleList.get(i).paint(graphics);
        }
        birdImageTP = new TexturePaint(birdImage,new Rectangle(xPos,(int)yPos,60,60));
        graphics.setPaint(birdImageTP);
        graphics.fill(bird);
    }

    /**
     * resets bird's position
     */
    public synchronized void pause() {
        bird.setFrame(xPos, yPos, birdDiameter, birdDiameter);
        yVel = -yAccel*((double)updateDelay/1000);
        if (yPos<=0 || yPos>=height-bottomHeight-birdDiameter)
            yPos = 240;
    }

    /**
     * @return crashed, true if bird has trashed and false if it has not
     */
    public boolean crashed(){
        return crashed;
    }

    /**
     * @return finished, true if bird has reached 50 and false if it has not
     */
    public boolean finished(){return finished;}

    /**
     * Collision detection with obstacles and collision handling
     */
    public void run() {
        while(!false) {
            if (obstacleList.size()>0) {
                if (!this.update() || (obstacleList.size() > 0 && obstacleList.get(0).isCollided(bird))) {
                    crashed = true;
                    int backup = width - obstacleList.get(0).getXPos();
                    for (ObstacleMasterClass i : obstacleList) {
                        i.moveObstacleBack(backup);
                    }
                    obstacleList.get(0).moveLastXPosBack(backup);
                    this.pause();
                    crashed = false;
                }
                try {
                    Thread.sleep(updateDelay);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            else {
                finished = true;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        }
    }

}