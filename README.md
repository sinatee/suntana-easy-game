# suntana-easy-game
```mermaid
<pre>
classDiagram
    %% Abstraction & Polymorphism
    class GameState {
        <<abstract>>
        +update() void
        +render(Graphics g) void
    }
    class MenuState {
        +update() void
        +render(Graphics g) void
    }
    class PlayState {
        -ball: Ball
        -player: Player
        -ai: AIController
        +update() void
        +render(Graphics g) void
    }

    %% Inheritance
    GameState <|-- MenuState
    GameState <|-- PlayState

    %% Encapsulation & Inheritance
    class Paddle {
        -x: int
        -y: int
        -speed: int
        +moveUp() void
        +moveDown() void
        +getY() int
        +setY(int y) void
    }
    class Player {
        -score: int
        +handleInput() void
    }
    class AIController {
        -difficulty: int
        +calculateMove(Ball b) void
    }

    Paddle <|-- Player
    Paddle <|-- AIController

    %% Encapsulation
    class Ball {
        -x: int
        -y: int
        -velocityX: int
        -velocityY: int
        +updatePosition() void
        +checkCollision() void
        +getX() int
    }

    PlayState --> Ball : contains
    PlayState --> Player : contains
    PlayState --> AIController : contains


classDiagram
    %% Abstraction
    class State {
        <<interface>>
        +update() void
        +draw(Graphics2D g2) void
    }

    %% Polymorphism
    class PlayState {
        +update() void
        +draw(Graphics2D g2) void
    }
    class MenuState {
        +update() void
        +draw(Graphics2D g2) void
    }
    
    State <|.. PlayState
    State <|.. MenuState

    %% Encapsulation
    class Ball {
        -double x
        -double y
        -double speedX
        -double speedY
        +update() void
        +draw(Graphics2D g2) void
        +getX() double
    }

    class Paddle {
        #int x
        #int y
        #int speed
        +update() void
        +draw(Graphics2D g2) void
    }

    %% Inheritance
    class Player {
        -int skillCount
        +keyPressed(KeyEvent e) void
        +useSkill() boolean
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

classDiagram
    class Skill {
        <<abstract>>
        #long duration
        #long startTime
        #boolean active
        +activate() void
        +isExpired() boolean
        +applyEffect()* void
        +getShortName()* String
    }

    class FreezeBallSkill {
        +applyEffect() void
        +getShortName() String
    }

    class SpeedBoostSkill {
        +applyEffect() void
        +getShortName() String
    }
    
    class TeleportSkill {
        +applyEffect() void
        +getShortName() String
    }

    class SkillManager {
        -ArrayList~Skill~ inventory
        -ArrayList~Skill~ activeSkills
        +addSkill(Skill s) void
        +useSkill(int index) void
        +update() void
    }

    %% Inheritance
    Skill <|-- FreezeBallSkill
    Skill <|-- SpeedBoostSkill
    Skill <|-- TeleportSkill

    %% Composition
    SkillManager o-- Skill : manages


classDiagram
    %% 1. INHERITANCE (การสืบทอด)
    class JPanel { <<built-in>> }
    class GamePanel {
        -GameEngine engine
        +startGameThread() void
        +run() void
        #paintComponent(Graphics g) void
    }
    JPanel <|-- GamePanel

    class Paddle {
        #int x, y, width, height
        #int speed
        +update() void
        +draw(Graphics2D g2) void
    }
    class Player {
        -int upKey, downKey, skillKey
        +keyPressed(KeyEvent e) void
    }
    Paddle <|-- Player

    %% 2. ABSTRACTION (นามธรรม)
    class Runnable {
        <<interface>>
        +run() void
    }
    Runnable <|.. GamePanel

    class State {
        <<interface>>
        +update() void
        +draw(Graphics2D g2) void
    }

    %% 3. POLYMORPHISM (พหุสัณฐาน)
    class MenuState {
        +update() void
        +draw(Graphics2D g2) void
    }
    class PlayState {
        +update() void
        +draw(Graphics2D g2) void
    }
    State <|.. MenuState
    State <|.. PlayState

    %% 4. ENCAPSULATION (การห่อหุ้มข้อมูล)
    class GameEngine {
        -State currentState
        -Ball ball
        -int score1, score2
        +update() void
        +draw(Graphics2D g2) void
        +getScore1() int
        +getScore2() int
        +changeState(State newState) void
    }
    
    class Ball {
        -double x, y
        -double speedX, speedY
        +update() void
        +getX() double
        +setX(double x) void
    }

    GameEngine *-- State : manages
    GameEngine *-- Ball : contains
    GameEngine *-- Paddle : contains


classDiagram
    %% Abstraction & Polymorphism
    class KeyListener { <<interface>> }
    class MouseListener { <<interface>> }
    class MouseMotionListener { <<interface>> }

    class InputHandler {
        -GameEngine engine
        +keyPressed(KeyEvent e) void
        +mouseClicked(MouseEvent e) void
        +mouseDragged(MouseEvent e) void
    }
    
    KeyListener <|.. InputHandler
    MouseListener <|.. InputHandler
    MouseMotionListener <|.. InputHandler

    %% Encapsulation (Good Example)
    class SoundManager {
        -Clip clip
        -FloatControl volumeControl
        +playLoop(String filePath) void
        +stop() void
    }

    class PixelSound {
        +playClick()$ void
    }

    %% Encapsulation (Suggested Fix: Use Singleton instead of public static)
    class Config {
        -static Config instance
        -float masterVolume
        -int aiDifficulty
        -Config()
        +getInstance()$ Config
        +getAiDifficulty() int
        +setAiDifficulty(int diff) void
    }

    class SaveManager {
        +saveSettings()$ void
        +loadSettings()$ void
    }
    SaveManager ..> Config : saves/loads

    %% Encapsulation & Logic separation
    class PhysicsEngine {
        -double gravity
        -double friction
        +applyFriction(Ball ball) void
        +checkCollision(Ball ball, Paddle p) boolean
    }


classDiagram
    %% 1. INHERITANCE (การสืบทอด)
    class JButton { <<Swing Built-in>> }
    class JPanel { <<Swing Built-in>> }

    class PixelArcadeButton {
        -boolean hovered
        -boolean pressed
        -float glowAlpha
        +PixelArcadeButton(String text)
        -loadPixelFont() void
        -startGlowAnimation() void
    }
    JButton <|-- PixelArcadeButton : extends

    class SettingPanel {
        +SettingPanel()
        -createSlider(String text, int x, int y) JSlider
    }
    JPanel <|-- SettingPanel : extends

    %% 2. ENCAPSULATION (การห่อหุ้ม)
    class MenuButton {
        -Color COL_BASE
        -float hoverAnimProgress
        +Rectangle bounds
        +click() void
        +draw(Graphics2D g) void
    }
    
    class PixelParticle {
        <<Inner Class>>
        ~int x, y, life, speed
        ~update() void
    }
    MenuButton *-- PixelParticle : encapsulates

    %% 3. ABSTRACTION & POLYMORPHISM (จุดที่สามารถพัฒนาเพิ่มได้)
    class Drawable {
        <<Interface (Suggestion)>>
        +draw(Graphics g)* void
    }

    class ScoreBoard {
        ~int leftScore
        ~int rightScore
        +draw(Graphics g) void
    }

    class PauseOverlay {
        +draw(Graphics g) void
    }

    Drawable <|.. ScoreBoard : implements (แนะนำ)
    Drawable <|.. PauseOverlay : implements (แนะนำ)
    Drawable <|.. MenuButton : implements (แนะนำ)
<pre>
