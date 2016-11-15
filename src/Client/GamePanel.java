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
import java.awt.geom.Arc2D;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Random rand;
    private long startTime;
    private int OBSTACLE_COUNT=20;
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private static final int UPDATE_DELAY = 17;
    private static final int BOTTOM_HEIGHT = 120;
    private Bird bird;
    private String name;
    private int id;
    private boolean notDone = true;
    public int press;
    String playerNameList[];
    int playerScoreList[];
    String leaderList[][];
    int time = 0;
    private ClientMaster client;

    private String[][] getLeaderList(String[] playerNameList, int[] ScoreList){
        int length;
        int max;
        if (playerNameList.length>2)
            length = 3;
        else
            length = 2;
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

    public GamePanel(String name){;
        this.name = name;
        try{
             client = new ClientMaster();
        }
        catch(IOException e){}
        (new Thread(client)).start();
        while (!client.isReady()){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException e){}
        }
        client.setHandle(this.name);
        client.updateObstaclesPassed(0);
        client.requestGameId();
        client.requestPlayerId();
        client.requestHandles();
        client.requestObstaclesPassed();
        rand = new Random(client.getGameId());
        bird = new Bird(rand, WIDTH, HEIGHT, BOTTOM_HEIGHT, UPDATE_DELAY, OBSTACLE_COUNT);
        while (client.backlog()){System.out.println("hi");}
        id = client.getPlayerId();
        playerNameList = client.getHandles();
        playerScoreList = client.getObstaclesPassed();
        leaderList = getLeaderList(playerNameList.clone(),playerScoreList.clone());
        Timer timer = new Timer(UPDATE_DELAY, this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        timer.start();
        this.repaint();
        startTime = System.currentTimeMillis();
        (new Thread(bird)).start();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        time += UPDATE_DELAY;
        this.repaint();
        if (time >= 1000){
            client.updateObstaclesPassed(bird.getScore());
            playerScoreList = client.getObstaclesPassed();
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

        g.setColor(Color.black);
        g.fillRect(WIDTH/4-5, HEIGHT - BOTTOM_HEIGHT, WIDTH/2+10, BOTTOM_HEIGHT);

        g.setColor(Color.white);
        g.fillRect(WIDTH/4, HEIGHT - BOTTOM_HEIGHT + 5, WIDTH/2, BOTTOM_HEIGHT);

        for (int i=0;i<playerNameList.length;i++) {
            if (i==id)
                g.setColor(Color.red);
            else
                g.setColor(Color.black);
            g.fillOval(WIDTH/4+1+playerScoreList[i]*(WIDTH/2-42)/ OBSTACLE_COUNT,HEIGHT-BOTTOM_HEIGHT+i*11+4  , 10, 10);
        }


        g.setColor(Color.red);

        g.fillRect(3*WIDTH/4-37, HEIGHT - BOTTOM_HEIGHT + 5, 4, BOTTOM_HEIGHT + 5);

        g.setColor(Color.yellow);
        Graphics2D g2 = (Graphics2D)g;
        bird.paint(g2);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", 1, 20));
        g.drawString("LEADERBOARD:",15, HEIGHT - BOTTOM_HEIGHT + 22);
        for (int i=0;i<leaderList.length && i<3;i++){
            g.drawString(leaderList[i][0]+" "+leaderList[i][1],15,HEIGHT-BOTTOM_HEIGHT+40+(18*i));
        }
        g.drawString("START",WIDTH/4-75, HEIGHT - BOTTOM_HEIGHT + 22);
        g.drawString("FINISH",3*WIDTH/4+10, HEIGHT - BOTTOM_HEIGHT + 22);
        g.drawString("TIME:",WIDTH-100, HEIGHT - BOTTOM_HEIGHT + 22);

        g.setFont(new Font("Arial", 1, 80));
        if(bird.finished()){
            if (notDone){
                client.setFinishTime(System.currentTimeMillis()-startTime);
                notDone = false;
            }
            client.requestFinishTimes();
            long finalTimes [] = client.getFinishTimes();
            g.drawString("LEADERBOARD",WIDTH / 4 + 25, 100);
            for (int i=0;i<leaderList.length;i++){
                g.drawString(Integer.toString(i+1) + ".",WIDTH/4-100,175+(75*i));
                g.drawString(playerNameList[i],WIDTH/4+25,175+(75*i));
                g.drawString(Double.toString(((int)finalTimes[i]/10)/100.0),3*WIDTH/4-100,175+(75*i));
            }
        }
        else {
        g.drawString(Integer.toString(bird.getScore()), WIDTH / 2 - 25, 100);
        }
    }
}