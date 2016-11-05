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
    //public Renderer renderer;
    private Ellipse2D bird;
    public int press;
    //public int yMotion;

    private double yVel;
    private double yPos;

    private static final double yAccel = 400;
    private static final int UPDATE_DELAY = 25;
    private static final int BIRD_DIAMETER = 60;
    private static final int BOTTOM_HEIGHT = 120;

    public GamePanel(){
        Timer timer = new Timer(UPDATE_DELAY, this);

        //Creates bird and sets size
        bird = new Ellipse2D.Double(WIDTH/4-10, HEIGHT/2-10, BIRD_DIAMETER, BIRD_DIAMETER);
        //this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        timer.start();
        this.repaint();
        this.yVel = -10;
        this.yPos = 100;
    }


    //Moves bird in y direction when spacebar is pressed
    private void flap(){
        /*if (yMotion > 0){
            yMotion = 0;
        }
        yMotion = yMotion - 10;*/
        yVel -= 300;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        yPos += yVel * ((double) UPDATE_DELAY / 1000.);
        yVel += yAccel * ((double) UPDATE_DELAY / 1000.);

        if (yPos <= 0) {
            yPos = 1;
            yVel = 0;
        } else if (yPos+BIRD_DIAMETER> HEIGHT-BOTTOM_HEIGHT) {
            yPos = HEIGHT-BOTTOM_HEIGHT-BIRD_DIAMETER;
            yVel = 0;
        } else {

        }
        bird.setFrame(WIDTH/4-10, yPos, BIRD_DIAMETER, BIRD_DIAMETER);
        this.repaint();
    }
    @Override
    public void keyReleased(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            flap();
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
        g.fillOval((int)bird.getX(), (int)bird.getY(), BIRD_DIAMETER, BIRD_DIAMETER);
    }
}