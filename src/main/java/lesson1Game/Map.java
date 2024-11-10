package lesson1Game;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {
    private static final Random RANDOM = new Random();
    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private final int EMPTY_DOT = 0;
    private static final int DOT_PADDING = 10;

    private int gameOverType;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private static final String MSG_WIN_HUMAN = "Победил игрок!";
    private static final String MSG_WIN_AI = "Победил компьютер!";
    private static final String MSG_DRAW = "Ничья!";

    private int panelWidth;
    private int panelHeight;
    private int cellWidth;
    private int cellHeight;

    private final int fieldSizeX = 3;
    private final int fieldSizeY = 3;
    private int[][] field;

    private boolean isGameOver;
    private boolean isInitialized;

    Map() {
        // Уточнить как именно работает этот процесс.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
        isInitialized = false;
    }

    private void initMap() {
        // Так как по умолчанию без инициализации тип int инициализируется как 0.
        field = new int[fieldSizeX][fieldSizeY];
    }

    private boolean isValidCell(int x, int y){
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private boolean isEmptyCell(int x, int y){
        return field[x][y] == EMPTY_DOT;
    }

    private void aiTurn() {
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[x][y] = AI_DOT;
    }


    private boolean checkWin(int character){
        if(field[0][0] == character && field[0][1] == character && field[0][2] == character) return true;
        if(field[1][0] == character && field[1][1] == character && field[1][2] == character) return true;
        if(field[2][0] == character && field[2][1] == character && field[2][2] == character) return true;

        if(field[0][0] == character && field[1][0] == character && field[2][0] == character) return true;
        if(field[0][1] == character && field[1][1] == character && field[2][1] == character) return true;
        if(field[0][2] == character && field[1][2] == character && field[2][2] == character) return true;

        if(field[0][0] == character && field[1][1] == character && field[2][2] == character) return true;
        return field[0][2] == character && field[1][1] == character && field[2][0] == character;
    }

    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeX; i++){
            for (int j = 0; j < fieldSizeY; j++){
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    private void update(MouseEvent e){
        if (isGameOver || !isInitialized) return;
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return;
        field[cellX][cellY] = HUMAN_DOT;
        if(checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return;
        aiTurn();
        repaint();
        checkEndGame(AI_DOT, STATE_WIN_AI);
    }

    private boolean checkEndGame(int dot, int gameOverType){
        if(checkWin(dot)){
            this.gameOverType = gameOverType;
            isGameOver = true;
            repaint();
            return true;
        }
        if(isMapFull()){
            this.gameOverType = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    public void startNewGame(int mode, int fSzX, int fSzY, int wLen){
        System.out.printf("Mode: %s;\nSize: x=%s, y=%s;\nWin Length: %s\n", mode, fSzX, fSzY, wLen);
        initMap();
        isGameOver = false;
        isInitialized = true;
        repaint();
    }

    private void render(Graphics g){
        if (!isInitialized) return;
        panelWidth = getWidth();
        panelHeight = getHeight();
        cellWidth = panelWidth / 3;
        cellHeight = panelHeight / 3;

        g.setColor(Color.BLACK);
        for (int h = 0; h < 3; h++){
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int w = 0; w < 3; w++){
            int x = w * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int y = 0; y < fieldSizeY; y++){
            for (int x = 0; x < fieldSizeX; x++){
                if (field[y][x] == EMPTY_DOT) continue;

                if (field[y][x] == HUMAN_DOT) {
                    g.setColor(Color.BLUE);
                    g.fillOval(
                            y * cellHeight + DOT_PADDING,
                            x * cellWidth + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else if(field[y][x] == AI_DOT){
                    g.setColor(Color.RED);
                    g.fillOval(y * cellHeight + DOT_PADDING,
                            x * cellWidth + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                }else {
                    throw new RuntimeException("Unexpected value " + field[y][x] + " in cell: x=" + y + " y=" + x);
                }
            }
        }

        if (isGameOver) showMessageGameOver(g);
    }


    private void showMessageGameOver(@NotNull Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameOverType) {
            case STATE_DRAW -> g.drawString(MSG_DRAW, 180, getHeight() / 2);
            case STATE_WIN_AI -> {
                g.setFont(new Font("Times new roman", Font.BOLD, 42));
                g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
            }
            case STATE_WIN_HUMAN -> g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
            default -> throw new RuntimeException("Unexpected gameOver state: " + gameOverType);
        }
    }
}
