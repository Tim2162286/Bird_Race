package Client;

/**
 * Created by Celso on 10/30/2016.
 */

import javax.swing.*;
import java.util.ArrayList;


public class BirdRace {
    public String name = "Tim";
    public int obstacleCount = 50;
    public static GamePanel birdracepanel;
    public final int WIDTH = 1280;  // 16:9 aspect ratio
    public final int HEIGHT = 720;
    public ArrayList<ObstacleMasterClass> obstacleList;
    //public Renderer renderer

    private static final int UPDATE_DELAY = 40;     // Time between updates in ms

    public BirdRace(){
        JFrame frame = new JFrame();

        frame.add(new GamePanel(name));
        frame.setTitle("BirdRace");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addKeyListener(birdracepanel);

    }



    public static void main(String[] args){
        new BirdRace();
    }

}
