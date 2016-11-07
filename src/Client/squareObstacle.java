package Client;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Created by Tim on 11/6/2016.
 */
public class SquareObstacle extends ObstacleMasterClass {
    private Rectangle2D topShape;
    private Rectangle2D bottomShape;
    public SquareObstacle(Random random, int windowHeight, int bottomHeight, int updateDelay, int farthest){
        super(random, windowHeight, bottomHeight, updateDelay, farthest);
        topShape = new Rectangle2D.Double(super.xPos,0,super.width, super.topHeight);
        bottomShape = new Rectangle2D.Double(super.xPos,super.bottomHeight,super.width,super.HEIGHT-super.BOTTOM_HEIGHT-super.bottomHeight);
    }

    @Override
    public void paint(Graphics2D graphics) {
        graphics.fill(topShape);;
        graphics.fill(bottomShape);
    }
    @Override
    public void update() {
        super.update();
        topShape.setFrame(super.xPos,0,super.width, super.topHeight);
        bottomShape.setFrame(super.xPos,super.bottomHeight,super.width,super.HEIGHT-super.BOTTOM_HEIGHT-super.bottomHeight);
    }
    public boolean remove(){
        return super.remove();
    }
}
