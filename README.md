🧠 FULL SYSTEM CLASS DIAGRAM
classDiagram

%% ================= CORE =================

class GameFrame {
  + GameFrame()
}

class GamePanel {
  - Thread gameThread
  - GameEngine engine
  + run()
  + paintComponent(Graphics)
}

class GameEngine {
  - int gameState
  - Paddle player1
  - Paddle player2
  - Ball ball
  - SkillManager p1Skills
  - SkillManager p2Skills
  - SkillBox skillBox
  - InputHandler input
  - SoundManager bgMusic
  + update()
  + draw(Graphics2D)
  + openPauseMenu()
  + isPlaying()
}

class GameEngineHolder {
  + static Ball ballRef
  + static GameEngine engineRef
}

GameFrame --> GamePanel
GamePanel --> GameEngine
GameEngineHolder --> GameEngine
GameEngineHolder --> Ball

%% ================= ENTITY =================

class Paddle {
  + int x
  + int y
  + boolean up
  + boolean down
  + boolean reversed
  + update()
  + draw(Graphics2D)
  + setSpeed(int)
}

class Ball {
  + double x
  + double y
  - boolean isFrozen
  - FreezeEffect freezeEffect
  + update()
  + draw(Graphics2D)
  + reverseX()
  + setFrozen(boolean)
}

class Player {
  - int skillCount
  + update()
  + draw(Graphics2D)
  + useSkill()
}

class AIController {
  - Paddle paddle
  - Ball ball
  + update(double)
}

GameEngine *-- Paddle
GameEngine *-- Ball
AIController --> Paddle
AIController --> Ball
Ball *-- FreezeEffect

%% ================= SKILL =================

class SkillManager {
  + ArrayList~Skill~ inventory
  + ArrayList~Skill~ activeSkills
  + addSkill(SkillType)
  + useSkill(int)
  + hasActive(SkillType)
  + update()
}

class Skill {
  + SkillType type
  + long duration
  + boolean active
  + activate()
  + isExpired()
}

class SkillType {
  <<enumeration>>
  SPEED_BOOST
  SLOW_OPPONENT
  BIG_PADDLE
  SMALL_OPPONENT
  TELEPORT
  REVERSE_CONTROL
  FREEZE_BALL
  CURVE_SHOT
}

class SkillBox {
  + respawn()
  + draw(Graphics2D)
}

GameEngine *-- SkillManager
GameEngine *-- SkillBox
SkillManager --> Skill
Skill --> SkillType

%% ================= SYSTEM =================

class InputHandler {
  - GameEngine engine
}

class SoundManager {
  - Clip clip
  + playLoop(String)
  + stop()
}

class PixelSound {
  + playClick()
}

class SaveManager {
  + saveSettings()
  + loadSettings()
}

class Config {
  + static masterVolume
  + static bgmVolume
  + static sfxVolume
  + static aiDifficulty
}

GameEngine --> InputHandler
GameEngine --> SoundManager
MenuButton --> PixelSound
SaveManager --> Config

%% ================= UI =================

class MenuButton {
  - Rectangle bounds
  + boolean hovered
  + boolean pressed
  + update()
  + draw(Graphics2D)
}

class PixelArcadeButton {
}

class PauseOverlay {
  + draw(Graphics)
}

class ScoreBoard {
  + draw(Graphics)
}

class SettingPanel {
}

GameEngine --> MenuButton
GameEngine --> PauseOverlay
SettingPanel --> Config

%% ================= STATE =================

class GameState {
  <<enumeration>>
  MENU
  PLAYING
  PAUSED
  SETTINGS
  GAME_OVER
}

GameEngine --> GameState

🧩 Design Structure
Core Layer
 ├── GameEngine (Main Logic Controller)
 ├── GamePanel (Game Loop)
 └── GameFrame (Window)

Entity Layer
 ├── Ball
 ├── Paddle
 ├── Player
 └── AIController

Skill System
 ├── SkillManager
 ├── Skill
 ├── SkillType (Enum)
 ├── SkillBox
 └── FreezeEffect

System Layer
 ├── InputHandler
 ├── SoundManager
 ├── PixelSound
 ├── SaveManager
 └── Config

UI Layer
 ├── MenuButton
 ├── PixelArcadeButton
 ├── PauseOverlay
 ├── ScoreBoard
 └── SettingPanel
