import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame() {

        setTitle("Snake Game");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        add(new GamePanel());

        setVisible(true);
    }
}