package com.ponggame.game.core;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    Thread gameThread;

    final int WIDTH = 1000;
    final int HEIGHT = 600;

    GameEngine engine;

    GamePanel() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        engine = new GameEngine(WIDTH, HEIGHT);

        this.addKeyListener(engine.getInput());
        this.addMouseListener(engine.getInput());
        this.addMouseMotionListener(engine.getInput());

        // We use a helper to ensure focus is requested after the component is visible
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                GamePanel.this.requestFocusInWindow();
            }
        });

        startGameThread();
    }

    void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        // Target: 60 FPS (approx 16.6 million nanoseconds per frame)
        double drawInterval = 1000000000.0 / 60.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {

            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                // 1. Update Game Logic
                engine.update();
                
                // 2. Draw Graphics (Requests Swing to paint)
                repaint();
                
                delta--;
            }
            
            // FIX: Sleep for 1 millisecond to prevent 100% CPU usage.
            // This drastically reduces stuttering and allows the OS to render smoothly.
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Optional: Enable anti-aliasing for smoother lines/text
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        engine.draw(g2);

        g2.dispose();
    }
}