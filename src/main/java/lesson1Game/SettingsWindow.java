package lesson1Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SettingsWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 380;
    private static final int WINDOW_WIDTH = 230;
    private static final String SIZE_OF_FIELD = "Выбранный размер поля: ";
    private static final String LENGHT_FOR_WIN = "Выбранная длина для победы: ";
    private static int size;
    private static int wLen;
    private final GameWindow gameWindow;
    private final JRadioButton humanVsAI;

    public int getwLen(){
        return wLen;
    }

    public int getSizeOfFiled() {
        return size;
    }

    SettingsWindow(GameWindow gameWindow){
        this.gameWindow = gameWindow;
        setLocationRelativeTo(gameWindow);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        JButton btnStart = new JButton("Start new game");
        btnStart.addActionListener(e -> startGame());

        JPanel settings = new JPanel(new GridLayout(3, 1));

        JPanel typeGame = new JPanel(new GridLayout(4, 1));
        ButtonGroup typeOfGameGroup = new ButtonGroup();
        humanVsAI = new JRadioButton("Человек против компьютера");
        humanVsAI.setSelected(true);
        JRadioButton humanVsHuman = new JRadioButton("Человек против человека");
        typeOfGameGroup.add(humanVsAI);
        typeOfGameGroup.add(humanVsHuman);
        typeGame.add(new JLabel("Выберите режим игры", SwingConstants.CENTER));
        typeGame.add(humanVsAI);
        typeGame.add(humanVsHuman);
        typeGame.add(new JLabel("--------------------", SwingConstants.CENTER));


        // Размер длины для победы.
        JPanel sizeForWin = new JPanel(new GridLayout(3, 1));
        sizeForWin.add(new JLabel("Выберите длину для победы", SwingConstants.CENTER));
        JLabel lenghtForWin = new JLabel(LENGHT_FOR_WIN, SwingConstants.CENTER);
        sizeForWin.add(lenghtForWin);
        JSlider winSlider = new JSlider(3, 10);
        sizeForWin.add(winSlider);

        winSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                wLen = winSlider.getValue();
                lenghtForWin.setText(LENGHT_FOR_WIN + wLen);
            }
        });

        // Размер игрового поля.
        JPanel sizeOfField = new JPanel(new GridLayout(5, 1));
        sizeOfField.add(new JLabel("Выберите размер поля", SwingConstants.CENTER));
        JLabel currentSize = new JLabel(SIZE_OF_FIELD, SwingConstants.CENTER);
        sizeOfField.add(currentSize);
        JSlider sizeSlider = new JSlider(3, 10);
        sizeOfField.add(sizeSlider);
        sizeOfField.add(new JLabel("-------------------", SwingConstants.CENTER));

        sizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                size = sizeSlider.getValue();
                currentSize.setText(SIZE_OF_FIELD + size);
                winSlider.setMaximum(size);
            }
        });


        settings.add(typeGame);
        settings.add(sizeOfField);
        settings.add(sizeForWin);



        add(settings);
        add(btnStart, BorderLayout.SOUTH);
    }

    private void startGame(){
        setVisible(false);
        gameWindow.startNewGame(humanVsAI.isSelected() ? 0 : 1, size, size, wLen);

    }
}
