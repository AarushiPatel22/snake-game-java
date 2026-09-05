import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {

    private JLabel scoreLabel;

    public GameScreen() {

        setBackground(new Color(12, 14, 20));
        setLayout(new BorderLayout());

        createHeader();
        createGameBoard();
        createFooter();
    }


    private void createHeader() {

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setBackground(
                new Color(12, 14, 20)
        );

        header.setBorder(
                BorderFactory.createEmptyBorder(
                        22,
                        40,
                        15,
                        40
                )
        );


        JLabel title =
                new JLabel("SNAKE GAME");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                new Color(80, 255, 150)
        );


        JLabel subtitle =
                new JLabel("CORE JAVA • SWING");

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        subtitle.setForeground(
                new Color(140, 150, 165)
        );


        JPanel titlePanel =
                new JPanel(
                        new GridLayout(2, 1)
                );

        titlePanel.setBackground(
                new Color(12, 14, 20)
        );

        titlePanel.add(title);
        titlePanel.add(subtitle);


        scoreLabel =
                new JLabel("SCORE: 000");

        scoreLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        20
                )
        );

        scoreLabel.setForeground(
                Color.WHITE
        );


        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                scoreLabel,
                BorderLayout.EAST
        );

        add(
                header,
                BorderLayout.NORTH
        );
    }


    private void createGameBoard() {

        GameBoard board =
                new GameBoard(score -> {

                    scoreLabel.setText(
                            String.format(
                                    "SCORE: %03d",
                                    score
                            )
                    );
                });


        JPanel centerPanel =
                new JPanel(
                        new GridBagLayout()
                );

        centerPanel.setBackground(
                new Color(12, 14, 20)
        );

        centerPanel.add(board);

        add(
                centerPanel,
                BorderLayout.CENTER
        );
    }


    private void createFooter() {

        JPanel footer =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                25,
                                12
                        )
                );

        footer.setBackground(
                new Color(12, 14, 20)
        );


        JLabel controls =
                new JLabel(
                        "↑ ↓ ← →  Move    •    SPACE  Pause"
                );

        controls.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        controls.setForeground(
                new Color(150, 160, 175)
        );


        footer.add(controls);

        add(
                footer,
                BorderLayout.SOUTH
        );
    }
}