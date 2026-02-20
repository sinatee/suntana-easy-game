package com.ponggame.game.ui;

import java.awt.*;

public class MenuButton {

    Rectangle bounds;
    String text;

    public boolean hovered = false;
    boolean pressed = false;

    float scale = 1f;

    // ===== Modern Color Palette =====
    private final Color NORMAL_BG = new Color(30, 30, 30);     // เทาเข้ม
    private final Color HOVER_BG  = new Color(255, 59, 59);    // แดง modern
    private final Color BORDER    = new Color(60, 60, 60);     // ขอบ
    private final Color TEXT_COLOR = new Color(230, 230, 230); // ขาวนวล

    public MenuButton(int x, int y, int w, int h, String t) {
        bounds = new Rectangle(x, y, w, h);
        text = t;
    }

    public void update() {

        float target = 1f;

        if (hovered)
            target = 1.08f;  // ขยายเล็กน้อยดู modern กว่า 1.1

        scale += (target - scale) * 0.15f;
    }

    public void draw(Graphics2D g2) {

        int w = (int)(bounds.width * scale);
        int h = (int)(bounds.height * scale);
        int x = bounds.x + (bounds.width - w)/2;
        int y = bounds.y + (bounds.height - h)/2;

        // ===== SHADOW =====
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(x + 4, y + 6, w, h, 30, 30);

        // ===== BACKGROUND =====
        if (hovered)
            g2.setColor(HOVER_BG);
        else
            g2.setColor(NORMAL_BG);

        g2.fillRoundRect(x, y, w, h, 30, 30);

        // ===== BORDER =====
        g2.setColor(BORDER);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(x, y, w, h, 30, 30);

        // ===== TEXT =====
        g2.setColor(TEXT_COLOR);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));

        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (w - fm.stringWidth(text)) / 2;
        int ty = y + (h + fm.getAscent()) / 2 - 4;

        g2.drawString(text, tx, ty);
    }

    public boolean contains(int mx, int my) {
        return bounds.contains(mx, my);
    }
}
