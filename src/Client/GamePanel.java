package Client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jon on 10/28/2016.
 */
public class GamePanel extends JPanel {

    private ArrayList<ObstacleMasterClass> obstacles;

    public GamePanel(ArrayList<ObstacleMasterClass> obstaces) {
        super();
        this.obstacles = obstaces;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        /* use the Graphics class methods to draw onto the panel */
    }

}
