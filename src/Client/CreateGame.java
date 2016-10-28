package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Celso on 10/28/2016.
 */
public class CreateGame {
    private JPanel CreateGameView;
    private JButton backButton;
    private JButton continueButton;
    private JComboBox obstacles;

    public CreateGame() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    public static void main(String[] args) {
        JFrame frame1 = new JFrame("CreateGame");
        frame1.setContentPane(new CreateGame().CreateGameView);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.pack();
        frame1.setVisible(true);
    }
}

