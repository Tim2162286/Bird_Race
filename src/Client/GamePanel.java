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
import java.awt.geom.Ellipse2D;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private static final int UPDATE_DELAY = 25;
    private static final int BOTTOM_HEIGHT = 120;
    private Bird bird = new Bird(WIDTH, HEIGHT, BOTTOM_HEIGHT, UPDATE_DELAY);
    public int press;


    public GamePanel(){
        Timer timer = new Timer(UPDATE_DELAY, this);

        //this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        timer.start();
        this.repaint();

    }

    @Override
    public void actionPerformed(ActionEvent e){
        bird.update();
        this.repaint();
    }
    @Override
    public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
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

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - BOTTOM_HEIGHT, WIDTH, BOTTOM_HEIGHT);

        g.setColor(Color.yellow);
        Graphics2D g2 = (Graphics2D)g;
        g2.fill(bird.getShape());
        //g.fillOval(WIDTH/4-10, (int)bird.getYPos(), 60, 60);
    }
}