package Client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jon on 10/28/2016.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final Random rand = new Random(System.currentTimeMillis());
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private static final int UPDATE_DELAY = 25;
    private static final int BOTTOM_HEIGHT = 120;
    private Bird bird;
    private ObstacleMasterClass obstacle;
    public int press;


    public GamePanel(){
        Timer timer = new Timer(UPDATE_DELAY, this);

        //this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        timer.start();
        bird = new Bird(WIDTH, HEIGHT, BOTTOM_HEIGHT, UPDATE_DELAY);
        obstacle = new SquareObstacle(rand,HEIGHT,BOTTOM_HEIGHT,UPDATE_DELAY);
        this.repaint();
        (new Thread(bird)).start();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        /*synchronized (this) {
            if (!bird.update()) {
                this.repaint();
                bird.pause();
            } else {
                this.repaint();
            }
        }*/
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE && (!bird.crashed()))
        {
            bird.flap();
        }
    }
    @Override
    public void keyTyped(KeyEvent e)
    {

    }
    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.black);
        g.fillRect(0, HEIGHT - BOTTOM_HEIGHT, WIDTH, BOTTOM_HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - BOTTOM_HEIGHT + 5, WIDTH, BOTTOM_HEIGHT);

        g.setColor(Color.yellow);
        Graphics2D g2 = (Graphics2D)g;
        obstacle.paint(g2);
        g2.fill(bird.getShape());

        System.out.println(bird.getShape().getBounds().getY());

    }
}