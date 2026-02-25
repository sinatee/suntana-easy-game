# Game - System Architecture

โปรเจกต์นี้มีการนำหลักการ **Object-Oriented Programming (OOP) ทั้ง 4 Concepts** มาประยุกต์ใช้อย่างเต็มรูปแบบ เพื่อให้โครงสร้างโค้ดเป็นระเบียบ ยืดหยุ่น และง่ายต่อการพัฒนาต่อยอด

### ⚙️ Core System, Entities & UI

```mermaid
classDiagram
    %% --- 1. Core Engine & State Pattern ---
    class GamePanel {
        -GameEngine engine
        +startGameThread() void
        +run() void
        #paintComponent(Graphics g) void
    }
    class GameEngine {
        -State currentState
        -Ball ball
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
    
    GamePanel --> GameEngine : runs
    GameEngine *-- State : manages
    State <|.. PlayState
    State <|.. MenuState

    %% --- 2. Game Entities & Physics ---
    class Ball {
        -double x, y
        -double speedX, speedY
        +update() void
        +draw(Graphics2D g2) void
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
    class PhysicsEngine {
        -double gravity
        -double friction
        +checkCollision(Ball b, Paddle p) boolean
    }

    Paddle <|-- Player
    Paddle <|-- AIController
    AIController --> Ball : tracks
    PhysicsEngine --> Ball : applies physics

    %% --- 3. Skill System & UI Components ---
    class Skill {
        <<abstract>>
        #long duration
        +activate() void
        +applyEffect()* void
    }
    class SkillManager {
        -ArrayList~Skill~ activeSkills
        +useSkill(int index) void
    }
    class FreezeBallSkill
    class SpeedBoostSkill
    
    Skill <|-- FreezeBallSkill
    Skill <|-- SpeedBoostSkill
    SkillManager o-- Skill : manages

    class PixelArcadeButton {
        -startGlowAnimation() void
    }
    class SettingPanel {
        -createSlider() JSlider
    }
    class ScoreBoard {
        +draw(Graphics g) void
    }

    JButton <|-- PixelArcadeButton : extends
    JPanel <|-- SettingPanel : extends
