import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class GameBoard extends JPanel {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 650;

    private static final int CELL_SIZE = 25;

    private final ArrayList<Point> snake;
    private final Timer timer;
    private final Random random;

    private final Consumer<Integer> scoreUpdater;

    private Point food;

    private int score = 0;
    private static int highScore = 0;

    private int directionX = CELL_SIZE;
    private int directionY = 0;

    private boolean gameOver = false;
    private boolean paused = false;


    public GameBoard(Consumer<Integer> scoreUpdater) {

        this.scoreUpdater = scoreUpdater;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(18, 20, 28));

        setBorder(
                BorderFactory.createLineBorder(
                        new Color(65, 75, 90),
                        2
                )
        );

        snake = new ArrayList<>();
        random = new Random();

        createInitialSnake();
        generateFood();
        setupKeyBindings();

        timer = new Timer(110, e -> moveSnake());
        timer.start();
    }


    private void createInitialSnake() {

        snake.clear();

        snake.add(new Point(400, 325));
        snake.add(new Point(375, 325));
        snake.add(new Point(350, 325));
    }


    private void setupKeyBindings() {

        InputMap inputMap =
                getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        ActionMap actionMap = getActionMap();


        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");

        actionMap.put("moveUp", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!gameOver && !paused &&
                        directionY != CELL_SIZE) {

                    directionX = 0;
                    directionY = -CELL_SIZE;
                }
            }
        });


        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");

        actionMap.put("moveDown", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!gameOver && !paused &&
                        directionY != -CELL_SIZE) {

                    directionX = 0;
                    directionY = CELL_SIZE;
                }
            }
        });


        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");

        actionMap.put("moveLeft", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!gameOver && !paused &&
                        directionX != CELL_SIZE) {

                    directionX = -CELL_SIZE;
                    directionY = 0;
                }
            }
        });


        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");

        actionMap.put("moveRight", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!gameOver && !paused &&
                        directionX != -CELL_SIZE) {

                    directionX = CELL_SIZE;
                    directionY = 0;
                }
            }
        });


        // SPACE = Pause / Resume
        inputMap.put(
                KeyStroke.getKeyStroke("SPACE"),
                "pauseGame"
        );

        actionMap.put(
                "pauseGame",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (!gameOver) {

                            paused = !paused;

                            if (paused) {
                                timer.stop();
                            } else {
                                timer.start();
                            }

                            repaint();
                        }
                    }
                }
        );


        // R = Restart
        inputMap.put(KeyStroke.getKeyStroke("R"), "restartGame");

        actionMap.put(
                "restartGame",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (gameOver) {
                            restartGame();
                        }
                    }
                }
        );


        // M = Main Menu
        inputMap.put(KeyStroke.getKeyStroke("M"), "mainMenu");

        actionMap.put(
                "mainMenu",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (gameOver) {

                            JFrame frame =
                                    (JFrame) SwingUtilities
                                            .getWindowAncestor(
                                                    GameBoard.this
                                            );

                            frame.setContentPane(
                                    new GamePanel()
                            );

                            frame.revalidate();
                            frame.repaint();
                        }
                    }
                }
        );
    }


    private void moveSnake() {

        if (gameOver || paused) {
            return;
        }

        Point head = snake.get(0);

        Point newHead = new Point(
                head.x + directionX,
                head.y + directionY
        );


        // Wall collision
        if (newHead.x < 0 ||
                newHead.x >= WIDTH ||
                newHead.y < 0 ||
                newHead.y >= HEIGHT) {

            endGame();
            return;
        }


        // Self collision
        for (Point segment : snake) {

            if (newHead.equals(segment)) {

                endGame();
                return;
            }
        }


        snake.add(0, newHead);


        // Food collision
        if (newHead.equals(food)) {

            score++;

            if (score > highScore) {
                highScore = score;
            }

            scoreUpdater.accept(score);

            generateFood();

        } else {

            snake.remove(snake.size() - 1);
        }


        repaint();
    }


    private void generateFood() {

        int maxX = WIDTH / CELL_SIZE;
        int maxY = HEIGHT / CELL_SIZE;

        Point newFood;

        do {

            int x =
                    random.nextInt(maxX) * CELL_SIZE;

            int y =
                    random.nextInt(maxY) * CELL_SIZE;

            newFood = new Point(x, y);

        } while (snake.contains(newFood));

        food = newFood;
    }


    private void endGame() {

        gameOver = true;
        timer.stop();
        repaint();
    }


    private void restartGame() {

        createInitialSnake();

        directionX = CELL_SIZE;
        directionY = 0;

        score = 0;
        scoreUpdater.accept(score);

        gameOver = false;
        paused = false;

        generateFood();

        timer.start();

        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        // =========================
        // GRID
        // =========================

        g2.setColor(
                new Color(32, 36, 48)
        );

        for (int x = 0;
             x < WIDTH;
             x += CELL_SIZE) {

            g2.drawLine(x, 0, x, HEIGHT);
        }

        for (int y = 0;
             y < HEIGHT;
             y += CELL_SIZE) {

            g2.drawLine(0, y, WIDTH, y);
        }


        // =========================
        // FOOD SHADOW
        // =========================

        g2.setColor(
                new Color(90, 25, 30)
        );

        g2.fillOval(
                food.x + 5,
                food.y + 6,
                CELL_SIZE - 8,
                CELL_SIZE - 8
        );


        // =========================
        // APPLE
        // =========================

        g2.setColor(
                new Color(255, 75, 85)
        );

        g2.fillOval(
                food.x + 3,
                food.y + 3,
                CELL_SIZE - 7,
                CELL_SIZE - 7
        );


        // Apple highlight
        g2.setColor(
                new Color(255, 170, 175)
        );

        g2.fillOval(
                food.x + 7,
                food.y + 6,
                5,
                5
        );


        // =========================
        // SNAKE
        // =========================

        for (int i = 0;
             i < snake.size();
             i++) {

            Point segment = snake.get(i);


            if (i == 0) {

                // Snake head
                g2.setColor(
                        new Color(120, 255, 175)
                );

            } else {

                // Snake body
                g2.setColor(
                        new Color(65, 220, 130)
                );
            }


            g2.fillRoundRect(
                    segment.x + 2,
                    segment.y + 2,
                    CELL_SIZE - 4,
                    CELL_SIZE - 4,
                    9,
                    9
            );
        }


        // =========================
        // SNAKE EYES
        // =========================

        if (!snake.isEmpty()) {

            Point head = snake.get(0);

            g2.setColor(
                    new Color(18, 20, 28)
            );

            int eyeSize = 4;


            // Moving right
            if (directionX > 0) {

                g2.fillOval(
                        head.x + 16,
                        head.y + 6,
                        eyeSize,
                        eyeSize
                );

                g2.fillOval(
                        head.x + 16,
                        head.y + 15,
                        eyeSize,
                        eyeSize
                );
            }


            // Moving left
            else if (directionX < 0) {

                g2.fillOval(
                        head.x + 5,
                        head.y + 6,
                        eyeSize,
                        eyeSize
                );

                g2.fillOval(
                        head.x + 5,
                        head.y + 15,
                        eyeSize,
                        eyeSize
                );
            }


            // Moving up
            else if (directionY < 0) {

                g2.fillOval(
                        head.x + 6,
                        head.y + 5,
                        eyeSize,
                        eyeSize
                );

                g2.fillOval(
                        head.x + 15,
                        head.y + 5,
                        eyeSize,
                        eyeSize
                );
            }


            // Moving down
            else {

                g2.fillOval(
                        head.x + 6,
                        head.y + 16,
                        eyeSize,
                        eyeSize
                );

                g2.fillOval(
                        head.x + 15,
                        head.y + 16,
                        eyeSize,
                        eyeSize
                );
            }
        }


        // =========================
        // PAUSE OVERLAY
        // =========================

        if (paused && !gameOver) {

            g2.setColor(
                    new Color(0, 0, 0, 170)
            );

            g2.fillRect(
                    0,
                    0,
                    WIDTH,
                    HEIGHT
            );

            drawCenteredText(
                    g2,
                    "PAUSED",
                    HEIGHT / 2 - 20,
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            52
                    ),
                    new Color(255, 220, 100)
            );

            drawCenteredText(
                    g2,
                    "Press SPACE to Continue",
                    HEIGHT / 2 + 30,
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            20
                    ),
                    new Color(210, 210, 220)
            );
        }


        // =========================
        // GAME OVER
        // =========================

        if (gameOver) {

            g2.setColor(
                    new Color(0, 0, 0, 195)
            );

            g2.fillRect(
                    0,
                    0,
                    WIDTH,
                    HEIGHT
            );


            drawCenteredText(
                    g2,
                    "GAME OVER",
                    HEIGHT / 2 - 100,
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            54
                    ),
                    new Color(255, 85, 95)
            );


            drawCenteredText(
                    g2,
                    "FINAL SCORE: " + score,
                    HEIGHT / 2 - 35,
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            22
                    ),
                    Color.WHITE
            );


            drawCenteredText(
                    g2,
                    "HIGH SCORE: " + highScore,
                    HEIGHT / 2 + 5,
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            20
                    ),
                    new Color(80, 255, 150)
            );


            drawCenteredText(
                    g2,
                    "Press R to Restart",
                    HEIGHT / 2 + 80,
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            18
                    ),
                    new Color(210, 210, 220)
            );


            drawCenteredText(
                    g2,
                    "Press M for Main Menu",
                    HEIGHT / 2 + 115,
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            18
                    ),
                    new Color(170, 170, 185)
            );
        }


        g2.dispose();
    }


    private void drawCenteredText(
            Graphics2D g2,
            String text,
            int y,
            Font font,
            Color color
    ) {

        g2.setFont(font);
        g2.setColor(color);

        FontMetrics metrics =
                g2.getFontMetrics();

        int textWidth =
                metrics.stringWidth(text);

        g2.drawString(
                text,
                (WIDTH - textWidth) / 2,
                y
        );
    }
}