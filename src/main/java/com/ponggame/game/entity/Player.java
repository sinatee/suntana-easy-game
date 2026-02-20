package com.ponggame.game.entity;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {

    int x, y;
    int width = 20;
    int height = 100;
    int speed = 6;

    int upKey, downKey, skillKey;

    boolean up, down;

    int skillCount = 0;

    public Player(int x, int y, int up, int down, int skill) {
        this.x = x;
        this.y = y;
        upKey = up;
        downKey = down;
        skillKey = skill;
    }

    public void update() {
        if (up) y -= speed;
        if (down) y += speed;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == upKey) up = true;
        if (e.getKeyCode() == downKey) down = true;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == upKey) up = false;
        if (e.getKeyCode() == downKey) down = false;
    }

    public void addSkill() { skillCount++; }

    public boolean useSkill() {
        if (skillCount > 0) {
            skillCount--;
            return true;
        }
        return false;
    }

    public void drawSkillBar(Graphics2D g, int x, int y) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, skillCount * 20, 10);
    }

    public void moveUp(){ y -= speed; }
    public void moveDown(){ y += speed; }
}
