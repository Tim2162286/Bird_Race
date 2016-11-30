package Client;

/**
 * Created by Celso on 10/30/2016.
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class BirdRace {
    public String name = "Tim";
    public static GamePanel birdracepanel;
    public final int WIDTH = 1280;  // 16:9 aspect ratio
    public final int HEIGHT = 720;
    public ArrayList<ObstacleMasterClass> obstacleList;
    private JFrame frame;
    //public Renderer renderer

    private static final int UPDATE_DELAY = 40;     // Time between updates in ms

    public BirdRace() {
        frame = new JFrame();
        do {
            name = JOptionPane.showInputDialog(frame, "Enter a nickname (no spaces):");
        } while (name == null);
        name = NameFormatter.format(name);
        frame.setTitle("BirdRace");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(true);
        JPanel temp = new JPanel();
        JLabel text = new JLabel("Waiting for players...");
        text.setFont(new Font("Calibri",1,20));
        temp.add(text);
        frame.add(temp);
        frame.setVisible(true);
        GamePanel game = new GamePanel(name, frame);
        frame.add(game);
        frame.remove(temp);
        game.requestFocusInWindow();
        frame.revalidate();

        frame.addKeyListener(birdracepanel);

    }



    public static void main(String[] args){
        new BirdRace();
    }

}
