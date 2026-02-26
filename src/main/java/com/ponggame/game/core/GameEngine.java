package com.ponggame.game.core;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.ponggame.game.skill.*;
import com.ponggame.game.entity.*;
import com.ponggame.game.ui.*;
import com.ponggame.game.system.*;

public class GameEngine {

    int width, height;

    private int uiPanelHeight = 100;
    private int gameHeight; // พื้นที่เล่นจริง
    private int windowHeight; // ความสูงจริงของหน้าต่าง

    // ===== GAME STATES =====
    public static final int TITLE = 0;
    public static final int MODE_SELECT = 1;
    public static final int PLAY = 2;
    public static final int GAME_OVER = 3;
    public static final int SETTINGS = 4;
    public static final int BALL_SPEED_MENU = 5;
    public static final int PAUSE_MENU = 6;
    public static final int SOUND_SETTINGS = 7; // เลขถัดจากของคุณ

    int gameState = TITLE;
    int previousState = TITLE;
    boolean vsCPU = false;

    public Paddle player1, player2;
    Ball ball;
    int score1 = 0, score2 = 0;
    double ballSpeed = 5;

    Color bgColor = new Color(18, 18, 18);
    Color accentColor = new Color(255, 59, 59);
    Color textColor = Color.WHITE;

    // UI Elements
    MenuButton startBtn, settingsBtn, exitBtn;
    MenuButton pvpBtn, cpuBtn, backToTitleBtn;
    MenuButton ballSpeedBtn, backBtn;
    MenuButton continueBtn, pauseSettingsBtn, exitToMenuBtn;

    // Slider for Ball Speed
    int sliderX, sliderY, sliderWidth = 400, sliderHeight = 6, sliderValue = 50;
    boolean dragging = false;

    // ===== SKILL =====
    public SkillManager p1Skills = new SkillManager();
    public SkillManager p2Skills = new SkillManager();
    SkillBox skillBox;
    int lastHit = 1;

    InputHandler input;

    private SoundManager bgMusic;
    // ===== BACKGROUND Pause=====
    private BufferedImage pauseBackground;
    // ===== BACKGROUND Setting=====
    private BufferedImage settingBackground;
    // ===== BACKGROUND GameOver=====
    private BufferedImage gameOverBackground;
    // ===== BACKGROUND Play=====
    private BufferedImage playBackground;
    // ===== PARALLAX BACKGROUND Title=====
    private BufferedImage titleBackground;

    private double bgOffsetX = 0;
    private double bgOffsetY = 0;

    private double targetOffsetX = 0;
    private double targetOffsetY = 0;

    private double parallaxStrength = 40; // ยิ่งมากยิ่งขยับแรง

    private Font pixelFont;

    // Table Margin
    private int tableMargin = 60;

    private int tableLeft;
    private int tableRight;
    private int tableTop;
    private int tableBottom;

    // Sound Manager
    MenuButton soundBtn;
    MenuButton soundBackBtn;
    MenuButton muteBtn;
    // ===== BACKGROUND Sound Setting=====
    private BufferedImage soundSettingBackground;

    int volumeSliderX, volumeSliderY, volumeSliderWidth = 400;
    int volumeSliderValue = 50;
    boolean volumeDragging = false;
    private boolean musicStarted = false;

    public GameEngine(int w, int h) {
        this.width = w;
        this.gameHeight = h; // พื้นที่เล่นจริง
        this.uiPanelHeight = 100;
        this.windowHeight = h + uiPanelHeight;
        this.height = windowHeight; // ใช้วาดทั้งหมด
        // Table Margin
        tableLeft = tableMargin;
        tableRight = width - tableMargin;
        tableTop = tableMargin;
        tableBottom = gameHeight - tableMargin;

        initUI();

        player1 = new Paddle(50, gameHeight / 2 - 60, 20, 120, gameHeight);
        player2 = new Paddle(width - 70, gameHeight / 2 - 60, 20, 120, gameHeight);
        ball = new Ball(width / 2, gameHeight / 2, 15, gameHeight);
        input = new InputHandler(this);
        skillBox = new SkillBox(
                tableLeft,
                tableRight,
                tableTop,
                tableBottom);

        // Load Music
        bgMusic = new SoundManager();

        // ===== LOAD TITLE BACKGROUND =====
        try {
            titleBackground = ImageIO.read(getClass().getResource("/background/title_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/font/PressStart2P-Regular.ttf"))
                    .deriveFont(48f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            playBackground = ImageIO.read(getClass().getResource("/background/play_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            gameOverBackground = ImageIO.read(getClass().getResource("/background/gameOver_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            settingBackground = ImageIO.read(getClass().getResource("/background/setting_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            pauseBackground = ImageIO.read(getClass().getResource("/background/pause_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            soundSettingBackground = ImageIO.read(getClass().getResource("/background/soundSetting_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initUI() {
        startBtn = new MenuButton(width / 2 - 150, 300, 300, 80, "START");
        settingsBtn = new MenuButton(width / 2 - 150, 400, 300, 80, "SETTINGS");
        exitBtn = new MenuButton(width / 2 - 150, 500, 300, 80, "EXIT GAME");

        pvpBtn = new MenuButton(width / 2 - 150, 260, 300, 80, "PLAYER VS PLAYER");
        cpuBtn = new MenuButton(width / 2 - 150, 370, 300, 80, "PLAYER VS CPU");
        backToTitleBtn = new MenuButton(width / 2 - 150, 480, 300, 70, "BACK TO MAIN");

        int buttonWidth = 300;
        int buttonHeight = 80;
        int spacing = 24; // ระยะห่างระหว่างปุ่ม

        int centerX = width / 2 - buttonWidth / 2;

        // คำนวณจุดเริ่มให้ปุ่มทั้ง 3 อยู่กึ่งกลางแนวตั้ง
        int totalHeight = buttonHeight * 3 + spacing * 2;
        int startY = height / 2 - totalHeight + 235;

        ballSpeedBtn = new MenuButton(centerX, startY, buttonWidth, buttonHeight, "BALL SPEED");

        soundBtn = new MenuButton(centerX,
                startY + buttonHeight + spacing,
                buttonWidth, buttonHeight,
                "SOUND");

        backBtn = new MenuButton(centerX,
                startY + (buttonHeight + spacing) * 2,
                buttonWidth, 70,
                "BACK");

        continueBtn = new MenuButton(width / 2 - 150, 260, 300, 70, "CONTINUE");
        pauseSettingsBtn = new MenuButton(width / 2 - 150, 350, 300, 70, "SETTINGS");
        exitToMenuBtn = new MenuButton(width / 2 - 150, 440, 300, 70, "EXIT TO MAIN");

        sliderX = width / 2 - sliderWidth / 2;
        sliderY = 350;

        // Sound Manager
        muteBtn = new MenuButton(width / 2 - 150, 390, 300, 70, "MUTE");
        soundBackBtn = new MenuButton(width / 2 - 150, 470, 300, 70, "BACK");

        volumeSliderX = width / 2 - 200;
        volumeSliderY = 330;
    }

    // ================= UPDATE =================
    public void update() {

        updateButtons();

        // Smooth easing (ทำให้ไม่กระตุก)
        bgOffsetX += (targetOffsetX - bgOffsetX) * 0.1;
        bgOffsetY += (targetOffsetY - bgOffsetY) * 0.1;

        if (gameState != PLAY)
            return;

        applySkillEffects();

        p1Skills.update();
        p2Skills.update();

        ball.update(tableTop, tableBottom);

        // FIXED CURVE SHOT LOGIC
        boolean isBallFrozen = ball.isFrozen();
        boolean curveActive = (p1Skills.hasActive(SkillType.CURVE_SHOT) || p2Skills.hasActive(SkillType.CURVE_SHOT))
                && !isBallFrozen;

        if (curveActive) {
            // FIX: Increased Safe Zone to 100px.
            // The ball will only curve if it is safely away from top/bottom walls.
            // This prevents the jittery/bouncy glitch near walls.
            int margin = 100;

            if (ball.getY() > margin && ball.getY() < gameHeight - ball.getWidth() - margin) {
                if (Math.random() < 0.1) {
                    int randomY = (int) ((Math.random() * 12) - 6);
                    ball.setDy(randomY);
                }
            }
        }

        player1.update();
        if (vsCPU)
            updateCPU();
        else
            player2.update();

        checkCollisions();
    }

    private void applySkillEffects() {
        // Reset defaults
        player1.setSpeed(5);
        player2.setSpeed(5);
        player1.setHeight(100);
        player2.setHeight(100);

        // Reset Reversed state
        player1.reversed = false;
        player2.reversed = false;

        // Reset Freeze state (default to false)
        ball.setFrozen(false);

        // REMOVED: ball.setInvisible(false);

        // --- Movement & Size ---
        if (p1Skills.hasActive(SkillType.SPEED_BOOST))
            player1.setSpeed(10);
        if (p2Skills.hasActive(SkillType.SPEED_BOOST))
            player2.setSpeed(10);
        if (p1Skills.hasActive(SkillType.BIG_PADDLE))
            player1.setHeight(160);
        if (p2Skills.hasActive(SkillType.BIG_PADDLE))
            player2.setHeight(160);
        if (p1Skills.hasActive(SkillType.SLOW_OPPONENT))
            player2.setSpeed(3);
        if (p2Skills.hasActive(SkillType.SLOW_OPPONENT))
            player1.setSpeed(3);

        // --- SKILLS ---

        // 1. FREEZE BALL
        if (p1Skills.hasActive(SkillType.FREEZE_BALL) || p2Skills.hasActive(SkillType.FREEZE_BALL)) {
            ball.setFrozen(true);
        }

        // 2. SMALL OPPONENT
        if (p1Skills.hasActive(SkillType.SMALL_OPPONENT))
            player2.setHeight(30);
        if (p2Skills.hasActive(SkillType.SMALL_OPPONENT))
            player1.setHeight(30);

        // 3. REVERSE CONTROL
        if (p1Skills.hasActive(SkillType.REVERSE_CONTROL))
            player2.reversed = true;
        if (p2Skills.hasActive(SkillType.REVERSE_CONTROL))
            player1.reversed = true;

        // 4. TELEPORT
        if (p1Skills.hasActive(SkillType.TELEPORT)) {
            player2.y = (int) (Math.random() * (gameHeight - player2.height));
        }
        if (p2Skills.hasActive(SkillType.TELEPORT)) {
            player1.y = (int) (Math.random() * (gameHeight - player1.height));
        }
    }

    private void checkCollisions() {
        // Paddle 1 Collision
        if (ball.getRect().intersects(player1.getRect())) {
            ball.reverseX();
            ball.setX((int) (player1.x + player1.width + 3));
            lastHit = 1;
            increaseBallSpeed(); // <--- MAKE IT FASTER
        }

        // Paddle 2 Collision
        if (ball.getRect().intersects(player2.getRect())) {
            ball.reverseX();
            ball.setX((int) (player2.x - ball.getWidth() - 3));
            lastHit = 2;
            increaseBallSpeed(); // <--- MAKE IT FASTER
        }

        // Skill Box Collision
        if (ball.getRect().intersects(skillBox.getRect())) {
            SkillType random = SkillType.random();
            if (lastHit == 1)
                p1Skills.addSkill(random);
            else
                p2Skills.addSkill(random);
            skillBox.respawnNearBall(
                    ball.getX(),
                    ball.getY());
        }

        // Scoring
        if (ball.getX() <= 0) {
            score2++;
            ball.reset(width / 2, gameHeight / 2, false);

            // Reset speed to the setting chosen in the menu
            ballSpeed = (sliderValue < 33) ? 3 : (sliderValue < 66) ? 5 : 8;
            ball.setSpeed((int) ballSpeed);

        } else if (ball.getX() >= width - ball.getWidth()) {
            score1++;
            ball.reset(width / 2, gameHeight / 2, true);

            // Reset speed to the setting chosen in the menu
            ballSpeed = (sliderValue < 33) ? 3 : (sliderValue < 66) ? 5 : 8;
            ball.setSpeed((int) ballSpeed);
        }

        if (score1 >= 10 || score2 >= 10)
            gameState = GAME_OVER;
    }

    // ================= DRAW =================
    public void draw(Graphics2D g2) {

        // วาดสีพื้นหลังเฉพาะตอนเล่นเกม
        if (gameState == PLAY) {
            g2.setColor(bgColor);
            g2.fillRect(0, 0, width, height);
        }

        switch (gameState) {
            case TITLE -> drawTitle(g2);
            case MODE_SELECT -> drawModeSelect(g2);
            case SETTINGS -> drawSettings(g2);
            case BALL_SPEED_MENU -> drawSpeedMenu(g2);
            case PLAY -> drawPlay(g2);
            case PAUSE_MENU -> drawPause(g2);
            case GAME_OVER -> drawGameOver(g2);
            case SOUND_SETTINGS -> drawSoundSettings(g2);
        }
    }

    private void drawTitle(Graphics2D g2) {

        if (titleBackground != null) {

            int drawX = (int) (-bgOffsetX - 50);
            int drawY = (int) (-bgOffsetY - 50);

            g2.drawImage(titleBackground,
                    drawX,
                    drawY,
                    width + 100,
                    height + 100,
                    null);
        }

        // Dark overlay
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, width, height);

        g2.setFont(pixelFont);

        // Main text
        g2.setColor(Color.WHITE);
        int textX = width / 2 - 220;
        int textY = 200;

        // Shadow
        g2.setColor(Color.BLACK);
        g2.drawString("PING PONG", textX + 4, textY + 4);

        // Main
        g2.setColor(Color.WHITE);
        g2.drawString("PING PONG", textX, textY);

        startBtn.draw(g2);
        settingsBtn.draw(g2);
        exitBtn.draw(g2);
    }

    private void drawModeSelect(Graphics2D g2) {
        if (settingBackground != null) {

            int drawX = (int) (-bgOffsetX - 50);
            int drawY = (int) (-bgOffsetY - 50);

            g2.drawImage(settingBackground,
                    drawX,
                    drawY,
                    width + 100,
                    height + 100,
                    null);
        }
        g2.setColor(textColor);
        g2.setFont(pixelFont.deriveFont(48f));
        String title = "SELECT MODE";

        FontMetrics fmTitle = g2.getFontMetrics();
        int xTitle = (width - fmTitle.stringWidth(title)) / 2;
        int yTitle = 200;
        g2.drawString(title, xTitle, yTitle);
        pvpBtn.draw(g2);
        cpuBtn.draw(g2);
        backToTitleBtn.draw(g2);
    }

    private void drawSettings(Graphics2D g2) {
        if (settingBackground != null) {

            int drawX = (int) (-bgOffsetX - 50);
            int drawY = (int) (-bgOffsetY - 50);

            g2.drawImage(settingBackground,
                    drawX,
                    drawY,
                    width + 100,
                    height + 100,
                    null);
        }
        g2.setColor(textColor);
        g2.setFont(pixelFont.deriveFont(48f));
        String title = "SETTINGS";

        FontMetrics fmTitle = g2.getFontMetrics();
        int xTitle = (width - fmTitle.stringWidth(title)) / 2;
        int yTitle = 200;

        g2.drawString(title, xTitle, yTitle);
        ballSpeedBtn.draw(g2);
        soundBtn.draw(g2);
        backBtn.draw(g2);

    }

    private void drawSpeedMenu(Graphics2D g2) {
        if (settingBackground != null) {

            int drawX = (int) (-bgOffsetX - 50);
            int drawY = (int) (-bgOffsetY - 50);

            g2.drawImage(settingBackground,
                    drawX,
                    drawY,
                    width + 100,
                    height + 100,
                    null);
        }
        g2.setColor(textColor);
        g2.setFont(pixelFont.deriveFont(48f));
        String title = "BALL SPEED";

        FontMetrics fmTitle = g2.getFontMetrics();
        int xTitle = (width - fmTitle.stringWidth(title)) / 2;
        int yTitle = 200;
        g2.drawString(title, xTitle, yTitle);

        g2.setColor(Color.GRAY);
        g2.fillRect(sliderX, sliderY, sliderWidth, sliderHeight);
        int knobX = sliderX + (sliderValue * sliderWidth / 100);
        g2.setColor(accentColor);
        g2.fillOval(knobX - 10, sliderY - 7, 20, 20);

        ballSpeed = (sliderValue < 33) ? 3 : (sliderValue < 66) ? 5 : 8;
        String text = "CURRENT: " + (ballSpeed == 3 ? "LOW" : ballSpeed == 5 ? "NORMAL" : "HIGH");
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
        g2.drawString(text, width / 2 - 120, 450);
        backBtn.draw(g2);
    }

    private void drawPlay(Graphics2D g2) {

        // วาดเฉพาะพื้นที่เกม
        // ===== Background =====
        if (playBackground != null) {
            g2.drawImage(playBackground,
                    0,
                    0,
                    width,
                    gameHeight, // สำคัญ! อย่าใช้ height
                    null);
        } else {
            g2.setColor(bgColor);
            g2.fillRect(0, 0, width, gameHeight);
        }

        player1.draw(g2);
        player2.draw(g2);
        ball.draw(g2);
        skillBox.draw(g2);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 40));
        g2.setColor(Color.WHITE);
        g2.drawString(score1 + " : " + score2, width / 2 - 50, 50);

        drawSkillPanel(g2);
    }

    private void drawPause(Graphics2D g2) {
        if (pauseBackground != null) {

            int drawX = (int) (-bgOffsetX - 50);
            int drawY = (int) (-bgOffsetY - 50);

            g2.drawImage(pauseBackground,
                    drawX,
                    drawY,
                    width + 100,
                    height + 100,
                    null);
        } else {
            g2.setColor(bgColor);
            g2.fillRect(0, 0, width, gameHeight);
        }
        g2.setColor(textColor);
        g2.setFont(pixelFont.deriveFont(48f));
        String title = "PAUSED";

        FontMetrics fmTitle = g2.getFontMetrics();
        int xTitle = (width - fmTitle.stringWidth(title)) / 2;
        int yTitle = 200;
        g2.drawString(title, xTitle, yTitle);
        continueBtn.draw(g2);
        pauseSettingsBtn.draw(g2);
        exitToMenuBtn.draw(g2);
    }

    private void drawGameOver(Graphics2D g2) {

        if (gameOverBackground != null) {
            int drawX = (int) (-bgOffsetX - 50);
            int drawY = (int) (-bgOffsetY - 50);

            g2.drawImage(gameOverBackground,
                    drawX,
                    drawY,
                    width + 100,
                    height + 100,
                    null);
        }

        g2.setColor(textColor);

        // ===== GAME OVER =====
        g2.setFont(pixelFont.deriveFont(48f));
        String title = "GAME OVER";

        FontMetrics fmTitle = g2.getFontMetrics();
        int xTitle = (width - fmTitle.stringWidth(title)) / 2;
        int yTitle = 300;

        g2.drawString(title, xTitle, yTitle);

        // ===== SCORE (Clear & Bold) =====
        g2.setFont(pixelFont.deriveFont(50f));
        String scoreText = score1 + " : " + score2;

        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(scoreText)) / 2;
        int y = 360;

        // เงาดำ
        g2.setColor(Color.BLACK);
        g2.drawString(scoreText, x + 3, y + 3);

        // ขอบรอบตัว (วาดรอบ 4 ทิศ)
        g2.drawString(scoreText, x - 2, y);
        g2.drawString(scoreText, x + 2, y);
        g2.drawString(scoreText, x, y - 2);
        g2.drawString(scoreText, x, y + 2);

        // ตัวจริง
        g2.setColor(Color.WHITE);
        g2.drawString(scoreText, x, y);

        // ===== MESSAGE =====
        g2.setFont(pixelFont.deriveFont(20f));
        String msg = "Click anywhere to return to Menu";

        FontMetrics fmMsg = g2.getFontMetrics();
        int xMsg = (width - fmMsg.stringWidth(msg)) / 2;
        int yMsg = 450;

        g2.drawString(msg, xMsg, yMsg);
    }

    // ================= MOUSE =================
    public void handleMouseClick(int x, int y) {
        if (gameState == TITLE) {
            if (startBtn.contains(x, y)) {
                startBtn.pressed = true; // 🎮 ทำให้ปุ่มจม
                startBtn.click(); // 🔊 เล่นเสียง
                gameState = MODE_SELECT;
            }

            if (settingsBtn.contains(x, y)) {
                settingsBtn.pressed = true;
                settingsBtn.click();
                previousState = TITLE;
                gameState = SETTINGS;
            }

            if (exitBtn.contains(x, y)) {
                exitBtn.pressed = true;
                exitBtn.click();
                System.exit(0);
            }

            // Start music only if it's not already playing (assuming SoundManager handles
            // checks or needs a flag)
            if (!musicStarted) {
                bgMusic.playLoop("/sound/Jirat Lorvitayapan - OOPTheme1 2026-02-18 14_43.wav");
                musicStarted = true;
            }
        } else if (gameState == MODE_SELECT) {
            if (pvpBtn.contains(x, y)) {
                vsCPU = false;
                resetGame();
                gameState = PLAY;
            }
            if (cpuBtn.contains(x, y)) {
                vsCPU = true;
                resetGame();
                gameState = PLAY;
            }
            if (backToTitleBtn.contains(x, y))
                gameState = TITLE;
        } else if (gameState == SETTINGS) {
            if (ballSpeedBtn.contains(x, y))
                gameState = BALL_SPEED_MENU;
            if (backBtn.contains(x, y))
                gameState = previousState;
            if (soundBtn.contains(x, y))
                gameState = SOUND_SETTINGS;

        } else if (gameState == BALL_SPEED_MENU) {
            if (new Rectangle(sliderX, sliderY - 10, sliderWidth, 30).contains(x, y)) {
                dragging = true;
                updateSlider(x);
            }
            if (backBtn.contains(x, y))
                gameState = SETTINGS;
        } else if (gameState == PAUSE_MENU) {
            if (continueBtn.contains(x, y))
                gameState = PLAY;
            if (pauseSettingsBtn.contains(x, y)) {
                previousState = PAUSE_MENU;
                gameState = SETTINGS;
            }
            if (exitToMenuBtn.contains(x, y))
                gameState = TITLE;
        } else if (gameState == GAME_OVER) {
            gameState = TITLE;
        } else if (gameState == SOUND_SETTINGS) {

            if (new Rectangle(volumeSliderX, volumeSliderY - 10,
                    volumeSliderWidth, 30).contains(x, y)) {

                volumeDragging = true;
                updateVolumeSlider(x);
            }

            if (muteBtn.contains(x, y)) {
                muteBtn.pressed = true; // ⭐ เพิ่ม
                bgMusic.setMuted(!bgMusic.isMuted());
            }

            if (soundBackBtn.contains(x, y)) {
                soundBackBtn.pressed = true; // ⭐ เพิ่ม
                gameState = SETTINGS;
            }
        }
    }

    public void handleMouseMove(int x, int y) {
        startBtn.hovered = startBtn.contains(x, y);
        settingsBtn.hovered = settingsBtn.contains(x, y);
        exitBtn.hovered = exitBtn.contains(x, y);

        pvpBtn.hovered = pvpBtn.contains(x, y);
        cpuBtn.hovered = cpuBtn.contains(x, y);
        backToTitleBtn.hovered = backToTitleBtn.contains(x, y);

        ballSpeedBtn.hovered = ballSpeedBtn.contains(x, y);
        backBtn.hovered = backBtn.contains(x, y);

        continueBtn.hovered = continueBtn.contains(x, y);
        pauseSettingsBtn.hovered = pauseSettingsBtn.contains(x, y);
        exitToMenuBtn.hovered = exitToMenuBtn.contains(x, y);

        muteBtn.hovered = muteBtn.contains(x, y);
        soundBackBtn.hovered = soundBackBtn.contains(x, y);
        soundBtn.hovered = soundBtn.contains(x, y);

        if (gameState == TITLE) {

            double percentX = (double) x / width - 0.5;
            double percentY = (double) y / height - 0.5;

            targetOffsetX = percentX * parallaxStrength;
            targetOffsetY = percentY * parallaxStrength;
        }
    }

    public void handleMouseRelease() {

        dragging = false;
        volumeDragging = false;

        startBtn.pressed = false;
        settingsBtn.pressed = false;
        exitBtn.pressed = false;

        pvpBtn.pressed = false;
        cpuBtn.pressed = false;
        backToTitleBtn.pressed = false;

        ballSpeedBtn.pressed = false;
        backBtn.pressed = false;

        continueBtn.pressed = false;
        pauseSettingsBtn.pressed = false;
        exitToMenuBtn.pressed = false;

        muteBtn.pressed = false;
        soundBackBtn.pressed = false;
    }

    public void handleMouseDrag(int x) {

        if (dragging)
            updateSlider(x);

        if (volumeDragging && gameState == SOUND_SETTINGS)
            updateVolumeSlider(x);
    }

    private void updateSlider(int x) {
        sliderValue = (x - sliderX) * 100 / sliderWidth;
        if (sliderValue < 0)
            sliderValue = 0;
        if (sliderValue > 100)
            sliderValue = 100;
    }

    // FIX 3: Fixed CPU logic. It was only moving up because of Math.abs logic.
    private void updateCPU() {
        int center = player2.y + player2.height / 2;
        int speed = player2.getSpeed(); // Use paddle's current speed (affected by skills)
        if (speed == 0)
            speed = 5; // Fallback if speed is 0

        // Add a small deadzone (10px) to prevent jittering
        if (ball.y < center - 10) {
            player2.y -= speed;
        } else if (ball.y > center + 10) {
            player2.y += speed;
        }

        // Bounds checking
        if (player2.y < 0)
            player2.y = 0;
        if (player2.y + player2.height > gameHeight)
            player2.y = gameHeight - player2.height;
    }

    private void increaseBallSpeed() {
        // Increase speed by 0.5 every hit
        ballSpeed += 0.5;

        // Cap the speed at 20 so it doesn't become impossible to play
        if (ballSpeed > 20.0) {
            ballSpeed = 20.0;
        }

        // Update the actual ball object (cast double to int for the ball's internal
        // logic)
        ball.setSpeed((int) ballSpeed);
    }

    private void resetGame() {
        score1 = 0;
        score2 = 0;
        player1.y = gameHeight / 2 - 60;
        player2.y = gameHeight / 2 - 60;
        p1Skills.inventory.clear();
        p1Skills.activeSkills.clear();
        p2Skills.inventory.clear();
        p2Skills.activeSkills.clear();
        ball.reset(width / 2, gameHeight / 2, true);
        ball.setSpeed(ballSpeed);
        skillBox.respawnNearBall(
                ball.getX(),
                ball.getY());
    }

    public InputHandler getInput() {
        return input;
    }

    public void openPauseMenu() {
        if (gameState == PLAY)
            gameState = PAUSE_MENU;
    }

    public boolean isPlaying() {
        return gameState == PLAY;
    }

    public boolean isVsCPU() {
        return vsCPU;
    }

    private void updateButtons() {

        startBtn.update();
        settingsBtn.update();
        exitBtn.update();

        pvpBtn.update();
        cpuBtn.update();
        backToTitleBtn.update();

        ballSpeedBtn.update();
        backBtn.update();

        continueBtn.update();
        pauseSettingsBtn.update();
        exitToMenuBtn.update();

        soundBtn.update();
        soundBackBtn.update();
        muteBtn.update();
    }

    private void drawSkillPanel(Graphics2D g2) {

        int panelY = gameHeight;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, panelY, width, uiPanelHeight);

        g2.setColor(accentColor);
        g2.fillRect(0, panelY, width, 4);

        p1Skills.draw(g2, 60, panelY + 20, "Q", "E");
        p2Skills.draw(g2, width - 220, panelY + 20, "O", "P");
    }

    private void updateVolumeSlider(int x) {
        volumeSliderValue = (x - volumeSliderX) * 100 / volumeSliderWidth;

        if (volumeSliderValue < 0)
            volumeSliderValue = 0;
        if (volumeSliderValue > 100)
            volumeSliderValue = 100;

        if (!bgMusic.isMuted()) {
            bgMusic.setVolume(volumeSliderValue / 100f);
        }
    }

    private void drawSoundSettings(Graphics2D g2) {

        if (soundSettingBackground != null) {
            g2.drawImage(soundSettingBackground, 0, 0, width, height, null);
        }

        g2.setColor(textColor);
        g2.setFont(pixelFont.deriveFont(46f));

        String title = "SOUND SETTINGS";
        FontMetrics fmTitle = g2.getFontMetrics();
        int xTitle = (width - fmTitle.stringWidth(title)) / 2;

        g2.drawString(title, xTitle, 200);

        // ===== VOLUME TEXT =====
        g2.setFont(pixelFont.deriveFont(20f));
        g2.drawString("Volume: " + volumeSliderValue + "%", width / 2 - 110, 300);

        // ===== SLIDER =====
        g2.setColor(Color.GRAY);
        g2.fillRect(volumeSliderX, volumeSliderY, volumeSliderWidth, 6);

        int knobX = volumeSliderX + (volumeSliderValue * volumeSliderWidth / 100);
        g2.setColor(accentColor);
        g2.fillOval(knobX - 10, volumeSliderY - 7, 20, 20);

        // ===== MUTE BUTTON =====
        muteBtn.text = bgMusic.isMuted() ? "UNMUTE" : "MUTE";
        muteBtn.draw(g2);

        // ===== BACK =====
        soundBackBtn.draw(g2);
    }
}