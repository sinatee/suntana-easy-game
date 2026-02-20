package com.ponggame.game.skill;

import java.awt.*;
import java.util.Random;

public class SkillBox {

    int x, y;
    int size = 30;

    int screenWidth;
    int screenHeight;

    int marginX = 150;   // ระยะกันขอบซ้ายขวา
    int marginY = 80;    // กันบนล่าง

    Random rand = new Random();

    public SkillBox(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        respawn();
    }

    public void respawn() {

        // โผล่เฉพาะโซนกลางสนาม
        x = marginX + rand.nextInt(screenWidth - marginX * 2 - size);
        y = marginY + rand.nextInt(screenHeight - marginY * 2 - size);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, size, size);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillRect(x, y, size, size);
    }
}
