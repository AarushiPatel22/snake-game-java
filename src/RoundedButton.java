import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);

        setFont(new Font("SansSerif", Font.BOLD, 18));
        setForeground(Color.BLACK);
        setBackground(new Color(70, 255, 140));

        setFocusPainted(false);
        setBorderPainted(false);

        setContentAreaFilled(false);
        setOpaque(false);

        setPreferredSize(new Dimension(220, 55));
        setMaximumSize(new Dimension(220, 55));
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(getBackground());

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                20,
                20
        );

        g2.dispose();

        super.paintComponent(g);
    }
}