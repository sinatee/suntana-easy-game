package com.ponggame.game.entity;

import java.awt.*;

public class Paddle {

    public int x, y, width, height;
    int panelHeight;

    int speed = 6;

    public boolean up;
    public boolean down;
    
    // NEW: Flag for Reverse Control skill
    public boolean reversed = false;

    public Paddle(int x, int y, int w, int h, int panelHeight) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.panelHeight = panelHeight;
    }

    public void update() {

        // If Reversed is active, swap the Up/Down logic
        boolean moveUp = reversed ? down : up;
        boolean moveDown = reversed ? up : down;

        if (moveUp)
            y -= speed;
        if (moveDown)
            y += speed;

        if (y < 0)
            y = 0;
        if (y + height > panelHeight)
            y = panelHeight - height;
    }

    public void draw(Graphics2D g2) {
        // OP CHANGE: If reversed, turn RED so it's visible!
        if (reversed) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.fillRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setHeight(int height) {
        this.height = height;

        if (y + height > panelHeight) {
            y = panelHeight - height;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

}