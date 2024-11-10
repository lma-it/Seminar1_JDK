package lesson1Game;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    SettingsWindow settings = new SettingsWindow(this);
    Map map = new Map();
    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_WIDTH = 507;
    private int sizeOfField = settings.getSizeOfFiled();
    private int wLen = settings.getwLen();
    JButton btnStart = new JButton("New Game");
    JButton btnExit = new JButton("Exit");

    public int getSizeOfField() {
        return sizeOfField;
    }

    public int getwLen() {
        return wLen;
    }

    GameWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setTitle("TicTacToe");
        setResizable(false);


        // Лямбда представление слушателя для кнопки Выход.
        btnExit.addActionListener(e -> System.exit(0));

        // Представление в виде блока кода
//        btnStart.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                settings.setVisible(true);
//            }
//        });

        // Лямбда представление слушателя для кнопки Старт.

        btnStart.addActionListener(e -> settings.setVisible(true));

        JPanel panButton = new JPanel(new GridLayout(1, 2));
        panButton.add(btnStart);
        panButton.add(btnExit);
        add(panButton, BorderLayout.SOUTH);
        add(map);


        setVisible(true);

    }

    public void startNewGame(int mode, int fSzX, int fSzY, int wLen) {
        map.startNewGame(mode, fSzX, fSzY, wLen);
    }
}
