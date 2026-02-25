# 🏓 Suntana Easy Game - System Architecture

โปรเจกต์นี้มีการนำหลักการ **Object-Oriented Programming (OOP) ทั้ง 4 Concepts** (Inheritance, Encapsulation, Abstraction, Polymorphism) มาประยุกต์ใช้อย่างเต็มรูปแบบ เพื่อให้โครงสร้างโค้ดเป็นระเบียบ ยืดหยุ่น และง่ายต่อการพัฒนาต่อยอด

```mermaid
classDiagram
    %% ==========================================
    %% 1. Core & State Pattern (Abstraction & Polymorphism)
    %% ==========================================
    class GamePanel {
        -GameEngine engine
        +startGameThread() void
        +run() void
        #paintComponent(Graphics g) void
    }
    class GameEngine {
        -State currentState
        -Ball ball
        -int score1, score2
        +update() void
        +draw(Graphics2D g2) void
        +changeState(State newState) void
    }
    class State {
        <<interface>>
        +update() void
        +draw(Graphics2D g2) void
    }
    class PlayState {
        +update() void
        +draw(Graphics2D g2) void
    }
    class MenuState {
        +update() void
        +draw(Graphics2D g2) void
    }
    
    JPanel <|-- GamePanel
    Runnable <|.. GamePanel
    GamePanel --> GameEngine : runs
    GameEngine *-- State : manages
    State <|.. PlayState
    State <|.. MenuState

    %% ==========================================
    %% 2. Game Entities (Inheritance & Encapsulation)
    %% ==========================================
    class Ball {
        -double x, y
        -double speedX, speedY
        +update() void
        +draw(Graphics2D g2) void
        +getX() double
    }
    class Paddle {
        #int x, y, speed
        +update() void
        +draw(Graphics2D g2) void
    }
    class Player {
        -int skillCount
        +keyPressed(KeyEvent e) void
    }
    class AIController {
        -Ball ball
        +update(double dt) void
    }

    Paddle <|-- Player
    Paddle <|-- AIController
    PlayState --> Ball : uses
    PlayState --> Player : uses
    PlayState --> AIController : uses
    GameEngine *-- Ball : contains
    GameEngine *-- Paddle : contains

    %% ==========================================
    %% 3. Skill System
    %% ==========================================
    class Skill {
        <<abstract>>
        #long duration
        #boolean active
        +activate() void
        +applyEffect()* void
    }
    class FreezeBallSkill
    class SpeedBoostSkill
    class TeleportSkill
    class SkillManager {
        -ArrayList~Skill~ activeSkills
        +useSkill(int index) void
    }

    Skill <|-- FreezeBallSkill
    Skill <|-- SpeedBoostSkill
    Skill <|-- TeleportSkill
    SkillManager o-- Skill : manages

    %% ==========================================
    %% 4. System & Physics
    %% ==========================================
    class InputHandler {
        -GameEngine engine
        +keyPressed(KeyEvent e) void
    }
    class PhysicsEngine {
        -double gravity
        -double friction
        +checkCollision(Ball b, Paddle p) boolean
    }
    class Config {
        -static Config instance
        +getInstance()$ Config
    }
    class SaveManager {
        +saveSettings()$ void
    }
    class SoundManager {
        -Clip clip
        +playLoop(String path) void
    }

    KeyListener <|.. InputHandler
    MouseListener <|.. InputHandler
    SaveManager ..> Config : saves/loads

    %% ==========================================
    %% 5. UI Components
    %% ==========================================
    class PixelArcadeButton {
        -boolean hovered
        -startGlowAnimation() void
    }
    class SettingPanel {
        -createSlider(String text, int x, int y) JSlider
    }
    class MenuButton {
        -Color COL_BASE
        +click() void
    }
    class Drawable {
        <<interface>>
        +draw(Graphics g)* void
    }
    class ScoreBoard
    class PauseOverlay

    JButton <|-- PixelArcadeButton
    JPanel <|-- SettingPanel
    Drawable <|.. ScoreBoard
    Drawable <|.. PauseOverlay
    Drawable <|.. MenuButton
