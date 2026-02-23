package com.ponggame.game.core;

import javax.swing.*;
import java.awt.*;
import com.ponggame.game.ui.SkillConsole; // Create this file next!

public class GameFrame extends JFrame {

    public GameFrame() {
        this.setTitle("Ping Pong Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 1. Initialize the Game Panel first
        GamePanel gamePanel = new GamePanel();
        
        // 2. Initialize the Skill Console using the same Engine
        SkillConsole skillConsole = new SkillConsole(gamePanel.getEngine());

        // 3. Set Layout and add components
        this.setLayout(new BorderLayout());
        this.add(gamePanel, BorderLayout.CENTER); // Game logic area
        this.add(skillConsole, BorderLayout.SOUTH); // UI area at the bottom

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}