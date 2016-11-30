package Client;

/**
 * Handles all client server communication, creates and updates game window.
 * @author Tim Cuprak, Celso Mundo, Jonathan Bush
 * @since 10/28/2016.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private Random rand;
    private long startTime;
    private int OBSTACLE_COUNT=50;
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private static final int UPDATE_DELAY = 17;
    private static final int BOTTOM_HEIGHT = 120;
    private Bird bird;
    private String name;
    private int id;
    private boolean notDone = true;
    private int s[] = new int[2];
    private int finalTimes[];
    private String playerNameList[];
    private int playerScoreList[];
    private String leaderList[][];
    private ArrayList<int[]> finalList;
    private int time = 0;
    private boolean isSorted[];
    private ClientMaster client;
    private double scale;
    private JFrame frame;

    /**
     * Sorts game LeaderBoard by score count
     * @param playerNameList contains array with name of players
     * @param ScoreList contains array with with scores
     * @return leaders, array containing players in order of place
     */
    private String[][] getLeaderList(String[] playerNameList, int[] ScoreList){
        int length;
        int max;
        if (playerNameList.length<3)
            length = 2;
        else
            length = 3;
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

    /**
     * Starts new GamePanel per player. Game starts when 2 or more players have joined
     * @param name name of player
     * @param frame new JFrame
     */
    public GamePanel(String name, JFrame frame){
        this.frame = frame;
        this.scale = 1.;
        this.name = name;
        try{
            client = new ClientMaster();
        }
        catch(IOException e){}
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.repaint();
        (new Thread(client)).start();
        while (!client.isReady()){
            try{
                Thread.sleep(200);
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
        while (client.backlog()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}
        }
        id = client.getPlayerId();
        playerNameList = client.getHandles();
        playerScoreList = client.getObstaclesPassed();
        leaderList = getLeaderList(playerNameList.clone(),playerScoreList.clone());
        finalList = new ArrayList(playerNameList.length);
        isSorted = new boolean[playerNameList.length];
        Timer timer = new Timer(UPDATE_DELAY, this);

        timer.start();

        startTime = System.currentTimeMillis();
        (new Thread(bird)).start();
    }

    /**
     * Updates score and time after action performed
     */
    @Override
    public void actionPerformed(ActionEvent e){
        time += UPDATE_DELAY;
        this.repaint();
        if (time >= 500){
            client.updateObstaclesPassed(bird.getScore());
            playerScoreList = client.getObstaclesPassed();
            leaderList = getLeaderList(playerNameList.clone(),playerScoreList.clone());
            client.requestFinishTimes();
            finalTimes = client.getFinishTimes();
            for (int i=0;i<finalTimes.length;i++){
                if (finalTimes[i]!=0 && !isSorted[i]){
                    int sort[] = new int[] {i,finalTimes[i]};
                    finalList.add(sort);
                    isSorted[i] = true;
                }
            }
            time = 0;
        }
    }

    /**
     * SpaceBar makes bird flap after release.
     * Does not flap if bird has crashed
     */
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

    /**
     * Formats time it takes player to reach end
     * @param time current time
     */
    private String formatTime(int time){
        int minutes = (time/10)/6000;
        int seconds = ((time/10)%6000)/100;
        int decimal = (time/10)%100;
        String mm = (minutes==0)?"00":(minutes<10)?"0"+Integer.toString(minutes):Integer.toString(minutes);
        String ss = (seconds==0)?"00":(seconds<10)?"0"+Integer.toString(seconds):Integer.toString(seconds);
        String dd = (decimal==0)?"00":(decimal<10)?"0"+Integer.toString(decimal):Integer.toString(decimal);
        return (mm+":"+ss+"."+dd);
    }

    /**
     * GUI: repainted as objects move
     */
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;

        Rectangle r = frame.getBounds();
        int h = r.height;
        int w = r.width;
        this.scale = Math.min((double)h/(double)HEIGHT, (double)w/(double)WIDTH);
        int newHeight = (int)(HEIGHT*scale);
        int newWidth =  (int)(WIDTH*scale);
        if(w - newWidth > 2) {
            frame.setSize(new Dimension(newWidth, h));
        } else if (h - newHeight > 2) {
            frame.setSize(new Dimension(w, newHeight));
        }
        g2.scale(this.scale, this.scale);

        super.paintComponent(g);

        //Sky
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //Black outline of ground
        g.setColor(Color.black);
        g.fillRect(0, HEIGHT - BOTTOM_HEIGHT, WIDTH, BOTTOM_HEIGHT);

        //Ground
        g.setColor(Color.orange.darker());
        g.fillRect(0, HEIGHT - BOTTOM_HEIGHT + 5, WIDTH, BOTTOM_HEIGHT);

        //Outline of map
        g.setColor(Color.black);
        g.fillRect(WIDTH/4-5, HEIGHT - BOTTOM_HEIGHT, WIDTH/2+10, BOTTOM_HEIGHT);

        //Map: Displays player and opponents position within race
        g.setColor(Color.white);
        g.fillRect(WIDTH/4, HEIGHT - BOTTOM_HEIGHT + 5, WIDTH/2, BOTTOM_HEIGHT);
        for (int i=0;i<playerNameList.length;i++) {
            if (i==id)
                g.setColor(Color.red);
            else
                g.setColor(Color.black);
            g.fillOval(WIDTH/4+1+playerScoreList[i]*(WIDTH/2-42)/ OBSTACLE_COUNT,HEIGHT-BOTTOM_HEIGHT+i*11+4  , 10, 10);
        }

        //Finish Line within Map
        g.setColor(Color.red);
        g.fillRect(3*WIDTH/4-37, HEIGHT - BOTTOM_HEIGHT + 5, 4, BOTTOM_HEIGHT + 5);

        //Color of obstacles
        g.setColor(Color.yellow);

        //Paints birds texture in Bird class
        bird.paint(g2);

        //In-game leaderboard: Displays top 3 players within game
        g.setColor(Color.black);
        g.setFont(new Font("Arial", 1, 20));
        if(!bird.finished()) {
            g.drawString("LEADERBOARD:", 15, HEIGHT - BOTTOM_HEIGHT + 22);
            for (int i = 0; i < leaderList.length && i < 3; i++) {
                g.drawString(leaderList[i][0] + " " + leaderList[i][1], 15, HEIGHT - BOTTOM_HEIGHT + 40 + (18 * i));
            }
        }

        //Text for Map
        g.drawString("START",WIDTH/4-75, HEIGHT - BOTTOM_HEIGHT + 22);
        g.drawString("FINISH",3*WIDTH/4+10, HEIGHT - BOTTOM_HEIGHT + 22);

        //Displays current time within race
        if(!bird.finished()) {
            g.drawString("TIME: " + formatTime((int) (System.currentTimeMillis() - startTime)), WIDTH - 200, HEIGHT - BOTTOM_HEIGHT + 22);
        }
        g.setFont(new Font("Arial", 1, 80));

        //GUI Displays score when game has not ended and final leaderboard when game has ended
        if(bird.finished()){
            if (notDone){
                client.setFinishTime(System.currentTimeMillis()-startTime);
                notDone = false;
            }
            g.drawString("LEADERBOARD",WIDTH / 4 + 25, 100);
            g.setFont(new Font("Arial", 1, 40));
            for (int i=0;i<finalList.size();i++){
                s = finalList.get(i);
                g.drawString(Integer.toString(i+1) + ".",WIDTH/4-100,175+(50*i));
                g.drawString(playerNameList[s[0]],WIDTH/4-60,175+(50*i));
                if (s[1]!=0)
                    g.drawString(formatTime(s[1]),3*WIDTH/4-30,175+(50*i));
                else
                    g.drawString("Not Finished",3*WIDTH/4-30,175+(50*i));

            }
        }
        else {
            g.drawString(Integer.toString(bird.getScore()), WIDTH / 2 - 25, 100);
        }

    }

    /**
     * Bird flaps when mouse clicked
     */
    @Override
    public void mousePressed(MouseEvent e) {
        bird.flap();
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
}