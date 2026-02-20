package com.ponggame.game.ui;

import javax.swing.*;
import java.awt.*;

public class SettingPanel extends JPanel {

    public SettingPanel() {
        setLayout(null);
        setBackground(new Color(20,20,20));

        JLabel title = new JLabel("SETTINGS");
        title.setBounds(300, 40, 300, 40);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        add(title);

        add(createSlider("Master Volume", 100, 120));
        add(createSlider("BGM Volume", 100, 180));
        add(createSlider("SFX Volume", 100, 240));
    }

    private JSlider createSlider(String text, int x, int y){
        JLabel label = new JLabel(text);
        label.setBounds(x, y-25, 200, 20);
        label.setForeground(Color.WHITE);
        add(label);

        JSlider slider = new JSlider(0,100,70);
        slider.setBounds(x, y, 300, 40);

        return slider;
    }
}
