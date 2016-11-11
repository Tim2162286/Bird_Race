package Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
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
    private final Random rand = new Random(1111);
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private static final int UPDATE_DELAY = 40;
    private static final int BOTTOM_HEIGHT = 120;
    private Bird bird;
    public int press;
    String playerList[][] = {{"P1","0"},{"P2","0"},{"P3","0"},{"P4","0"}};
    String leaderList[][] = new String[3][2];
    int time = 0;
    private ClientMaster client;

    private String[][] getLeaderList(String[][] playerList){
        return new String[0][0];
    }

    public GamePanel(){
        Timer timer = new Timer(UPDATE_DELAY, this);

        //this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        timer.start();
        bird = new Bird(rand, WIDTH, HEIGHT, BOTTOM_HEIGHT, UPDATE_DELAY);
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
        time += UPDATE_DELAY;
        System.out.println(time);
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

        g.setColor(Color.orange.darker());
        g.fillRect(0, HEIGHT - BOTTOM_HEIGHT + 5, WIDTH, BOTTOM_HEIGHT);

        g.setColor(Color.yellow);
        Graphics2D g2 = (Graphics2D)g;
        bird.paint(g2);

        g.setColor(Color.white);
        playerList[0][1] = Integer.toString(bird.getScore());
        g.setFont(new Font("Arial", 1, 20));

        g.drawString("Leaderboard:",15, HEIGHT - BOTTOM_HEIGHT + 20);
        for (int i=0;i<leaderList.length && i<3;i++){
            g.drawString(leaderList[i][0]+" "+leaderList[i][1],15,HEIGHT-BOTTOM_HEIGHT+40+(18*i));
        }

        g.setFont(new Font("Arial", 1, 80));
        if(bird.crashed()){
            g.drawString("Respawning In 3 Sec...", 200, HEIGHT / 2 - 50);
        }
        else {
            g.drawString(Integer.toString(bird.getScore()), WIDTH / 2 - 25, 100);
        }
    }
}