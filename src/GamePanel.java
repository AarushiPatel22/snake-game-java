import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private final Color BACKGROUND = new Color(15, 15, 20);
    private final Color PRIMARY = new Color(70, 255, 140);
    private final Color SECONDARY = new Color(150, 150, 160);

    public GamePanel() {

        setBackground(BACKGROUND);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createStartScreen();
    }

    private void createStartScreen() {

        add(Box.createVerticalGlue());

        JLabel title = new JLabel("SNAKE");
        title.setFont(new Font("SansSerif", Font.BOLD, 64));
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);

        JLabel subtitle = new JLabel("CLASSIC ARCADE EDITION");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(subtitle);

        add(Box.createRigidArea(new Dimension(0, 35)));

        JLabel scoreLabel = new JLabel("HIGH SCORE  •  000");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        scoreLabel.setForeground(new Color(220, 220, 225));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(scoreLabel);

        add(Box.createRigidArea(new Dimension(0, 50)));

        RoundedButton playButton = new RoundedButton("PLAY GAME");


        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(playButton);
        playButton.addActionListener(e -> {

            JFrame frame =
                    (JFrame) SwingUtilities.getWindowAncestor(this);

            frame.setContentPane(new GameScreen());

            frame.revalidate();
            frame.repaint();

        });

        add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel instruction = new JLabel("Use Arrow Keys to Control the Snake");
        instruction.setForeground(SECONDARY);
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(instruction);

        add(Box.createVerticalGlue());
    }
}