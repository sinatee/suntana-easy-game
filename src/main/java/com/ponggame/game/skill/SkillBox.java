package com.ponggame.game.skill;

import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class SkillBox {

    int x, y;
    int size = 50;

    int screenWidth;
    int screenHeight;

    int marginX = 150; // ระยะกันขอบซ้ายขวา
    int marginY = 80; // กันบนล่าง

    Image icon;

    Random rand = new Random();

    int left, right, top, bottom;

    public SkillBox(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        try {
            icon = new ImageIcon(
                    getClass().getResource("/assets/skillBox.png")).getImage();
        } catch (Exception e) {
            System.out.println("Skill image not found");
        }
        respawn();
    }

    public void respawn() {

        // โผล่เฉพาะโซนกลางสนาม
        x = left + rand.nextInt(right - left - size);
        y = top + rand.nextInt(bottom - top - size);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, size, size);
    }

    public void respawnNearBall(int ballX, int ballY) {

    int radius = 150;

    int newX = ballX + rand.nextInt(radius * 2) - radius;
    int newY = ballY + rand.nextInt(radius * 2) - radius;

    // กันหลุดขอบโต๊ะ
    if (newX < left) newX = left;
    if (newX > right - size) newX = right - size;

    if (newY < top) newY = top;
    if (newY > bottom - size) newY = bottom - size;

    x = newX;
    y = newY;
}

    public void draw(Graphics2D g2) {

        if (icon != null) {
            g2.drawImage(icon, x, y, size, size, null);
        } else {
            g2.setColor(Color.YELLOW);
            g2.fillRect(x, y, size, size);
        }
    }
}
