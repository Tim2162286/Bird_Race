package Client;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * SquareObstacle is derived from ObstacleMasterClass, and  creates an obstacle consisting of 2
 * rectangles aligned vertically, separated by the opening.
 * @author Tim Cuprak
 * @since 11/6/2016.
 */
public class SquareObstacle extends ObstacleMasterClass {
    private Rectangle2D topShape;
    private Rectangle2D bottomShape;

    /**
     * Constructor, adds the the top and bottom rectangles which creates the physical obstacle.
     * @param random The random number generator to be used for obstacle generation.
     * @param windowHeight The Height of the game window.
     * @param bottomHeight The height of the space used at the bottom of the game window.
     * @param updateDelay The amount of time in milliseconds between game updates.
     */
    public SquareObstacle(Random random, int windowHeight, int bottomHeight, int updateDelay){
        super(random, windowHeight, bottomHeight, updateDelay);
        topShape = new Rectangle2D.Double(super.xPos,0,super.width, super.topHeight);
        bottomShape = new Rectangle2D.Double(super.xPos,super.bottomHeight,super.width,super.HEIGHT-super.BOTTOM_HEIGHT-super.bottomHeight);
    }

    /**
     * Paints the top and bottom shapes on the game window.
     * @param graphics The Graphics2D object to be used in the paint method.
     */
    @Override
    public void paint(Graphics2D graphics) {

        graphics.fill(topShape);
        graphics.fill(bottomShape);
    }

    /**
     * Changes the x position of the obstacle object in a normal game tick,
     * also updates lastXPos for generation of future obstacles.
     * @param numObstacles The number of obstacles that will be updated in current tick.
     */
    @Override
    public void update(int numObstacles) {
        super.update(numObstacles);
        topShape.setFrame(super.xPos,0,super.width, super.topHeight);
        bottomShape.setFrame(super.xPos,super.bottomHeight,super.width,super.HEIGHT-super.BOTTOM_HEIGHT-super.bottomHeight);
    }

    /**
     * Checks if obstacle should be removed.
     * @return true if obstacle should be removed, else false.
     */
    public boolean remove(){
        return super.remove();
    }

    /**
     * Collision detection between the shapes comprising the obstacle and the birds shape.
     * @param bird The shape to check collision on, should be the bird.
     * @return true if collided, else false.
     */
    @Override
    public boolean isCollided(Shape bird) {
        Area areaA = new Area(bird);
        areaA.intersect(new Area(topShape));
        Area areaB = new Area(bird);
        areaB.intersect(new Area(bottomShape));
        return !(areaB.isEmpty() && areaA.isEmpty());
    }

    /**
     * Backs the obstacle up by the width of the game window, should be called on all
     * obstacles currently in existence whenever a collision is detected.
     */
    public void reset(){super.reset();}
}
