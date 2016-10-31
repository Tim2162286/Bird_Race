package Client;

/**
 * Created by Celso on 10/30/2016.
 */
import java.awt.Graphics;
import javax.swing.JPanel;

public class Renderer extends JPanel {
    private static final long serialVersionUID = 1L;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        BirdRace.birdrace.repaint(g);
    }
}