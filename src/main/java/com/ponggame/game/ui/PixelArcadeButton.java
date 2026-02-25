package com.ponggame.game.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.ponggame.game.system.PixelSound;

public class PixelArcadeButton extends JButton {

    private boolean hovered = false;
    private boolean pressed = false;
    private float glowAlpha = 0f;

    public PixelArcadeButton(String text) {
        super(text);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);

        loadPixelFont();
        startGlowAnimation();

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovered = true; }
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
            }
            public void mousePressed(MouseEvent e) {
                pressed = true;
                PixelSound.playClick();
            }
            public void mouseReleased(MouseEvent e) { pressed = false; }
        });
    }

    private void loadPixelFont() {
        try {
            Font pixelFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf")
            ).deriveFont(18f);

            setFont(pixelFont);
        } catch (Exception e) {
            setFont(new Font("Monospaced", Font.BOLD, 18));
        }
    }

    private void startGlowAnimation() {
        Timer timer = new Timer(16, e -> {
            glowAlpha += 0.03f;
            if (glowAlpha > 1f) glowAlpha = 0f;
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        int offset = pressed ? 3 : 0;

        Color mainColor = hovered
                ? new Color(255, 120, 0)
                : new Color(255, 150, 0);

        // Glow
        if (hovered) {
            g2.setColor(new Color(255, 180, 0, (int)(120 * glowAlpha)));
            g2.fillRoundRect(0, 0, w, h, 8, 8);
        }

        // Shadow
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);

        // Main stepped body
        g2.setColor(mainColor);
        drawSteppedRect(g2, 6, 6 + offset, w - 12, h - 12);

        // Highlight
        g2.setColor(Color.WHITE);
        g2.fillRect(6, 6 + offset, w - 12, 4);
        g2.fillRect(6, 6 + offset, 4, h - 12);

        // Border
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(4));
        drawSteppedRect(g2, 6, 6 + offset, w - 12, h - 12);

        // Text
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(getText());
        int th = fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.drawString(getText(),
                (w - tw) / 2,
                (h + th) / 2 - 4 + offset);
    }

    private void drawSteppedRect(Graphics2D g2, int x, int y, int w, int h) {

        int step = 8;
        Polygon p = new Polygon();

        p.addPoint(x + step, y);
        p.addPoint(x + w - step, y);
        p.addPoint(x + w, y + step);
        p.addPoint(x + w, y + h - step);
        p.addPoint(x + w - step, y + h);
        p.addPoint(x + step, y + h);
        p.addPoint(x, y + h - step);
        p.addPoint(x, y + step);

        g2.fillPolygon(p);
    }
}