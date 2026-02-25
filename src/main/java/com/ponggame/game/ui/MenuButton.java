package com.ponggame.game.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.ponggame.game.system.PixelSound;

public class MenuButton {

    Rectangle bounds;
    String text;

    public boolean hovered = false;
    public boolean pressed = false;

    // Resolution multiplier: 1 "game pixel" = 3 screen pixels
    private final int P = 3; 

    // --- Expanded Pixel Palette ---
    private final Color COL_OUTLINE  = new Color(40, 20, 0);
    private final Color COL_BASE     = new Color(235, 130, 30);
    private final Color COL_HOVER    = new Color(255, 180, 50);   // Brighter Orange
    private final Color COL_HIGHLIGHT= new Color(255, 220, 120);
    private final Color COL_SHADE    = new Color(160, 70, 20);
    private final Color COL_TEXT     = new Color(255, 255, 255);
    
    // Animation State
    private float hoverAnimProgress = 0.0f; // 0.0 to 1.0
    private float cursorSlideX = 0.0f;      // Current X offset for arrow
    private float targetCursorX = 0.0f;
    
    // Particle System
    private List<PixelParticle> particles;
    private Random random = new Random();

    private Font pixelFont;

    public MenuButton(int x, int y, int w, int h, String t) {
        bounds = new Rectangle(x, y, w, h);
        text = t;
        particles = new ArrayList<>();
        loadPixelFont();
    }

    private void loadPixelFont() {
        try {
            pixelFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf")
            ).deriveFont(18f); 
        } catch (Exception e) {
            pixelFont = new Font("Monospaced", Font.BOLD, 18);
        }
    }

    public void update() {
        // 1. Animate Hover Progress (Smooth fade in/out)
        if (hovered) {
            hoverAnimProgress = Math.min(1.0f, hoverAnimProgress + 0.15f);
            targetCursorX = 0; // Slide cursor into view
        } else {
            hoverAnimProgress = Math.max(0.0f, hoverAnimProgress - 0.15f);
            targetCursorX = -P * 5; // Slide cursor out to the left
        }

        // 2. Animate Cursor Slide (Lerp for smooth movement)
        // We move 20% of the distance each frame
        cursorSlideX += (targetCursorX - cursorSlideX) * 0.25f;

        // 3. Update Particles
        if (hovered && random.nextInt(5) == 0) { // Spawn chance
            spawnParticle();
        }
        
        // Update existing particles
        for (int i = particles.size() - 1; i >= 0; i--) {
            PixelParticle p = particles.get(i);
            p.update();
            if (p.life <= 0) {
                particles.remove(i);
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        int x = bounds.x;
        int y = bounds.y;
        int w = bounds.width;
        int h = bounds.height;

        // 1. Movement Offset when pressed
        if (pressed) {
            x += P;
            y += P;
        }

        // 2. Draw Border (Outline)
        g2.setColor(COL_OUTLINE);
        g2.fillRect(x, y, w, h);

        // 3. Draw Animated Background
        // Interpolate color based on hoverAnimProgress
        Color currentBg = lerpColor(COL_BASE, COL_HOVER, hoverAnimProgress);
        g2.setColor(currentBg);
        g2.fillRect(x + P, y + P, w - P*2, h - P*2);

        // 4. Draw Bevel (3D Effect)
        // Top/Left Highlight
        g2.setColor(COL_HIGHLIGHT);
        g2.fillRect(x + P, y + P, w - P*2, P);          
        g2.fillRect(x + P, y + P, P, h - P*2);          
        
        // Bottom/Right Shadow
        g2.setColor(COL_SHADE);
        g2.fillRect(x + P, y + h - P*2, w - P*2, P);    
        g2.fillRect(x + w - P*2, y + P, P, h - P*2);    

        // 5. Draw Particles (Behind text/UI elements but above background)
        for(PixelParticle p : particles) {
            p.draw(g2);
        }

        // 6. Draw Cursor (Animated Slide)
        // Only draw if the animation is somewhat visible
        if (cursorSlideX > -P * 4) {
            int arrowX = x + P * 3 + (int)cursorSlideX; // Apply slide offset
            int arrowY = y + (h / 2) - P; 
            
            // Add a slight vertical bounce using sine wave
            int bounce = (int)(Math.sin(System.currentTimeMillis() / 100.0) * P);
            
            drawTriangleCursor(g2, arrowX, arrowY + bounce);
        }
        
        // 7. Draw Text
        g2.setFont(pixelFont);
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text);
        int th = fm.getHeight();

        // Center text (Move up slightly if hovered)
        int yOffset = (int)(hoverAnimProgress * -P); // Hop effect
        int tx = x + (w - tw) / 2;
        int ty = y + (h - th) / 2 + fm.getAscent() + yOffset;

        // Text Shadow
        g2.setColor(COL_OUTLINE);
        g2.drawString(text, tx + P/2, ty + P/2);
        
        // Main Text
        g2.setColor(COL_TEXT);
        g2.drawString(text, tx, ty);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    // Helper to interpolate between two colors
    private Color lerpColor(Color c1, Color c2, float t) {
        int r = (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * t);
        int g = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t);
        int b = (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t);
        return new Color(r, g, b);
    }

    private void spawnParticle() {
        // Spawn near edges
        int px = bounds.x + random.nextInt(bounds.width);
        int py = bounds.y + bounds.height - P*2; // Start at bottom
        particles.add(new PixelParticle(px, py));
    }

    // Draws a triangle shape ">"
    private void drawTriangleCursor(Graphics2D g, int x, int y) {
        g.setColor(Color.WHITE);
        
        drawPixel(g, x, y); // Top point
        drawPixel(g, x, y + P);
        drawPixel(g, x + P, y + P);
        
        drawPixel(g, x, y + P*2);
        drawPixel(g, x + P, y + P*2);
        drawPixel(g, x + P*2, y + P*2); // Center point
        
        drawPixel(g, x, y + P*3);
        drawPixel(g, x + P, y + P*3);
        
        drawPixel(g, x, y + P*4); // Bottom point
    }

    private void drawPixel(Graphics2D g, int x, int y) {
        g.fillRect(x, y, P, P);
    }

    public boolean contains(int mx, int my) {
        return bounds.contains(mx, my);
    }

    public void click() {
        PixelSound.playClick();
    }

    // --- Simple Inner Class for Particles ---
    private class PixelParticle {
        int x, y;
        int life = 60; // Frames
        int speed = 1; 
        
        public PixelParticle(int x, int y) {
            this.x = x;
            this.y = y;
            this.speed = random.nextInt(2) + 1;
        }
        
        void update() {
            y -= speed; // Float up
            x += random.nextInt(3) - 1; // Slight horizontal wobble
            life--;
        }
        
        void draw(Graphics2D g) {
            // Fade out
            int alpha = (int)(255 * ((float)life / 60.0));
            // Clamp alpha to max 180 so it's not too harsh
            alpha = Math.min(alpha, 180); 
            
            // Draw a small 1x1 pixel (size P)
            g.setColor(new Color(255, 255, 255, alpha));
            g.fillRect(x, y, P, P);
        }
    }
}