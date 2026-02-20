package com.ponggame.game.entity;

import java.awt.*;
import java.util.Random;
import com.ponggame.game.skill.skillEffect.*;

public class Ball {

    // Coordinates are double for smooth movement
    public double x, y;
    int size;

    double speedX;
    double speedY;

    double baseSpeed = 5.0;
    int panelHeight;

    private boolean invisible = false;
    private boolean isFrozen = false; // New flag controlled by GameEngine

    FreezeEffect freezeEffect = new FreezeEffect();

    Random random = new Random();

    public Ball(int centerX, int centerY, int size, int panelHeight) {
        this.size = size;
        this.panelHeight = panelHeight;
        reset(centerX, centerY, true);
    }

    public void update() {
        // Update the visual effect timer (for the ice graphic)
        freezeEffect.update();

        // Only move coordinates if NOT frozen
        if (!isFrozen) {
            x += speedX;
            y += speedY;
        }

        // Bounce off top/bottom walls (only if moving)
        if (!isFrozen) {
            if (y <= 0 || y >= panelHeight - size) {
                speedY *= -1;
            }
        }
    }

    public void draw(Graphics2D g2) {
        
        // FIX: If invisible, draw NOTHING (not even ice)
        if (invisible) return;

        // If Frozen (and not invisible), draw ice
        if (isFrozen || freezeEffect.isActive()) {
            freezeEffect.draw(g2, (int)x, (int)y, size, size);
            return;
        }

        // Normal drawing
        g2.setColor(Color.WHITE);
        g2.fillOval((int)x, (int)y, size, size);
    }

    public void reverseX() {
        speedX *= -1;
    }

    public void reset(int centerX, int centerY, boolean serveRight) {
        x = centerX - size / 2.0;
        y = centerY - size / 2.0;

        speedX = serveRight ? baseSpeed : -baseSpeed;
        speedY = random.nextBoolean() ? baseSpeed : -baseSpeed;

        // Reset freeze state
        isFrozen = false;
        freezeEffect = new FreezeEffect();
    }

    public Rectangle getRect() {
        return new Rectangle((int)x, (int)y, size, size);
    }

    public void setSpeed(double ballSpeed) {
        baseSpeed = ballSpeed;
        speedX = (speedX < 0) ? -ballSpeed : ballSpeed;
        speedY = (speedY < 0) ? -ballSpeed : ballSpeed;
    }

    public void setInvisible(boolean value) {
        invisible = value;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public int getX() { return (int)x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return (int)y; }
    public void setY(int y) { this.y = y; }
    public int getDy() { return (int)speedY; }
    public void setDy(int dy) { this.speedY = dy; }
    public int getWidth() { return size; }

    // NEW: Method to let GameEngine control the freeze
    public void setFrozen(boolean frozen) {
        this.isFrozen = frozen;
        if (frozen) {
            freezeEffect.activate(1500); // Show ice graphic for 1.5s
        }
    }
    
    public boolean isFrozen() {
        return isFrozen;
    }
}