package com.ponggame.game.system;

import java.awt.event.*;
import com.ponggame.game.core.GameEngine;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

    GameEngine engine;

    public InputHandler(GameEngine e) {
        engine = e;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // 🔥 กด ESC เพื่อเปิด Pause Menu
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            engine.openPauseMenu();
            return;
        }

        // รับปุ่มเฉพาะตอนกำลังเล่นเท่านั้น
        if (!engine.isPlaying())
            return;

        if (e.getKeyCode() == KeyEvent.VK_W)
            engine.player1.up = true;

        if (e.getKeyCode() == KeyEvent.VK_S)
            engine.player1.down = true;

        if (!engine.isVsCPU()) {

            if (e.getKeyCode() == KeyEvent.VK_UP)
                engine.player2.up = true;

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                engine.player2.down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_Q)
            engine.p1Skills.useSkill(0);

        if (e.getKeyCode() == KeyEvent.VK_E)
            engine.p1Skills.useSkill(1);

        if (e.getKeyCode() == KeyEvent.VK_O)
            engine.p2Skills.useSkill(0);

        if (e.getKeyCode() == KeyEvent.VK_P)
            engine.p2Skills.useSkill(1);

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (!engine.isPlaying())
            return;

        if (e.getKeyCode() == KeyEvent.VK_W)
            engine.player1.up = false;

        if (e.getKeyCode() == KeyEvent.VK_S)
            engine.player1.down = false;

        if (!engine.isVsCPU()) {

            if (e.getKeyCode() == KeyEvent.VK_UP)
                engine.player2.up = false;

            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                engine.player2.down = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        engine.handleMouseClick(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        engine.handleMouseMove(e.getX(), e.getY());
    }

    // ADDED: This is required for the Ball Speed slider to drag
    @Override
    public void mouseDragged(MouseEvent e) {
        engine.handleMouseDrag(e.getX());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        engine.handleMouseRelease();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}