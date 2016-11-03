package Client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jon on 10/28/2016.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javafx.scene.shape.Circle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.Object;
import java.awt.geom.RectangularShape;
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

    public GamePanel(){
        Timer timer = new Timer(UPDATE_DELAY, this);

        //Creates bird and sets size
        bird = new Ellipse2D.Double(WIDTH/4-10, HEIGHT/2-10, 30, 30);
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
        /*press++;
        if (press % 2 == 0){
            yMotion += 2;
        }*/
        yPos += yVel * ((double) UPDATE_DELAY / 1000.);
        yVel += yAccel * ((double) UPDATE_DELAY / 1000.);
        bird = new Ellipse2D.Double(WIDTH/4-10, (double)yPos, 30.0, 30.0);
        if (bird.getCenterY() <= 0) {
            //bird.setCenterY(1);
            yPos = 1;
            yVel = 0;
            System.out.println(yPos);
        } else if (bird.getCenterY()+180>HEIGHT) {
            bird = new Ellipse2D.Double(WIDTH/4-10, HEIGHT-195, 30.0, 30.0);
            yVel = 0;
        } else {

        }
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
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.yellow);
        g.fillOval((int)bird.getCenterX(), (int)bird.getCenterY(), 60, 60);
    }
}