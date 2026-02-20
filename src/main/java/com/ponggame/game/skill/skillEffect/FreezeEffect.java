package com.ponggame.game.skill.skillEffect;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class FreezeEffect {

    private BufferedImage freezeImage;
    private boolean active = false;
    private long endTime = 0;

    public FreezeEffect() {
        try {
            freezeImage = ImageIO.read(
                    getClass().getResource("/assets/freeze_ball.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void activate(int durationMillis) {
        active = true;
        endTime = System.currentTimeMillis() + durationMillis;
    }

    public void update() {
        if (active && System.currentTimeMillis() > endTime) {
            active = false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void draw(Graphics2D g2, int x, int y, int width, int height) {

        if (!active) return;

        g2.drawImage(freezeImage, x, y, width, height, null);

        g2.setColor(new Color(150, 220, 255, 80));
        g2.fillOval(x - 5, y - 5, width + 10, height + 10);
    }
}
