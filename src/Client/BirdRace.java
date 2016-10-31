package Client;

/**
 * Created by Celso on 10/30/2016.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javafx.scene.shape.Circle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.Timer;

public class BirdRace implements ActionListener, KeyListener{
    public static BirdRace birdrace;
    public final int WIDTH = 1280;
    public final int HEIGHT = 720;
    public Renderer renderer;
    public Circle bird;
    public int press;
    public int yMotion;

    public BirdRace(){
        JFrame frame = new JFrame();
        renderer = new Renderer();
        Timer timer = new Timer(20,this);

        frame.add(renderer);
        frame.setTitle("BirdRace");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setVisible(true);

        //Creates bird and sets size
        bird = new Circle(WIDTH/2-10, HEIGHT/2-10, 30);
        timer.start();
    }

    //Repaints after movements
    public void repaint(Graphics g){
        g.setColor(Color.blue);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.yellow);
        g.fillOval((int)bird.getCenterX(), (int)bird.getCenterY(), 60, 60);
    }

    public static void main(String[] args){
        birdrace = new BirdRace();
    }

    //Moves bird in y direction when spacebar is pressed
    public void flap(){
        if (yMotion > 0){
            yMotion = 0;
        }
        yMotion = yMotion - 10;
    }
    @Override
    public void actionPerformed(ActionEvent e){
        int speed = 10;
        press++;
        if (press % 2 == 0){
            yMotion += 2;
        }
        bird.setCenterY(bird.getCenterY() + yMotion);
        renderer.repaint();
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
}
