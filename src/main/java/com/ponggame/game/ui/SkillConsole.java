package com.ponggame.game.ui;

import javax.swing.*;
import java.awt.*;
import com.ponggame.game.core.GameEngine;

public class SkillConsole extends JPanel {
    private GameEngine engine;

    public SkillConsole(GameEngine engine) {
        this.engine = engine;
        // Make the console 100 pixels tall
        this.setPreferredSize(new Dimension(1000, 100)); 
        this.setBackground(new Color(45, 45, 45)); // Dark Grey background
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Only draw skills if the game is actually running
        if (engine.isPlaying()) {
            // Draw Player 1 Skills on the left side of THIS panel
            engine.p1Skills.draw(g2, 20, 20); 
            
            // Draw Player 2 Skills on the right side of THIS panel
            engine.p2Skills.draw(g2, getWidth() - 140, 20);
        }
    }
}