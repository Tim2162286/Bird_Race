import javax.swing.*;

public class MainMenu extends JFrame {
    private JButton jbtCreateGame = new JButton("Create Game");
    private JButton jbtJoinGame = new JButton("Join Game");
    private JButton jbtBack = new JButton("back");
    private JButton jbtContinue = new JButton("continue");
    JPanel pnlHolder;

    public MainMenu(){
        Box box = Box.createVerticalBox();
        box.add(jbtCreateGame,JFrame.CENTER_ALIGNMENT);
        box.add(jbtJoinGame);
        add(box);
    }

    public static void main(String[] args) {
        MainMenu frame = new MainMenu();
        frame.setTitle("Bird Race");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}