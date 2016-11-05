package Client;

/**
 * Created by Celso on 10/30/2016.
 */

import javax.swing.*;


public class BirdRace {
    public static GamePanel birdracepanel;
    public final int WIDTH = 1280;  // 16:9 aspect ratio
    public final int HEIGHT = 720;
    //public Renderer renderer

    private static final int UPDATE_DELAY = 25;     // Time between updates in ms

    public BirdRace(){
        JFrame frame = new JFrame();

        frame.add(new GamePanel());
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
