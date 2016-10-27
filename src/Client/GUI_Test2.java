package Client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tim on 10/25/2016.
 */
public class GUI_Test2 {
    public static void main(String[] args){
        JFrame mainMenu = new JFrame("Main Menu");
        JButton createGame = new JButton("Create Game");
        JButton joinGame = new JButton("Join Game");
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setSize(500,600);
        JPanel panel = new JPanel();
        LayoutManager layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.add(createGame);
        panel.add(joinGame);
        mainMenu.add(panel);
        mainMenu.setVisible(true);
    }
}
