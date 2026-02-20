package com.ponggame.game.ui;

import java.awt.*;

public class ScoreBoard {

    int leftScore = 0;
    int rightScore = 0;

    void draw(Graphics g) {

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));

        g.drawString("" + leftScore, 400, 80);
        g.drawString("" + rightScore, 560, 80);
    }
}
