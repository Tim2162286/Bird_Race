package Client;

import javax.swing.*;
import java.awt.*;


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
    String playerNameList[] = {"P1","P2","P3","P4"};
    int playerScoreList[] = {0,5,0,0};
    String leaderList[][] = getLeaderList(playerNameList.clone(),playerScoreList.clone());
    int time = 0;
    private ClientMaster client;

    private String[][] getLeaderList(String[] playerNameList, int[] ScoreList){
        int length;
        int max;
        if (playerNameList.length>2)
            length = 3;
        else
            length = 2;
        System.out.println(length);
        String leaders[][] = new String[length][2];
        for (int i=0;i<leaders.length;i++) {
            max = 0;
            for (int j = 0; j < playerNameList.length; j++) {
                if (ScoreList[j]>ScoreList[max]) {
                    max = j;
                }
            }
            leaders[i][0] = playerNameList[max];
            leaders[i][1] = Integer.toString(ScoreList[max]);
            ScoreList[max] = -1;
        }
        return leaders;
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
        playerScoreList[0] = bird.getScore();
        time += UPDATE_DELAY;
        this.repaint();
        if (time >= 1000){
            leaderList = getLeaderList(playerNameList.clone(),playerScoreList.clone());
            time = 0;
        }
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