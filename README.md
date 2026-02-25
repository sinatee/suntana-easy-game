```mermaid
classDiagram

%% ================= CORE =================
class GameFrame {
  + GameFrame()
}

class GamePanel {
  - Thread gameThread
  - GameEngine engine
  + run() void
  + paintComponent(Graphics) void
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
  + update() void
  + draw(Graphics2D) void
  + openPauseMenu() void
  + isPlaying() boolean
}

class GameEngineHolder {
  + static Ball ballRef
  + static GameEngine engineRef
}

GameFrame --> "1" GamePanel
GamePanel --> "1" GameEngine
GameEngineHolder --> GameEngine
GameEngineHolder --> Ball

%% ================= ENTITY =================

class Paddle {
  + int x
  + int y
  + boolean up
  + boolean down
  + boolean reversed
  + update() void
  + draw(Graphics2D) void
  + setSpeed(int) void
}

class Ball {
  + double x
  + double y
  - boolean isFrozen
  - FreezeEffect freezeEffect
  + update() void
  + draw(Graphics2D) void
  + reverseX() void
  + setFrozen(boolean) void
}

class Player {
  - int skillCount
  + update() void
  + draw(Graphics2D) void
  + useSkill() boolean
}

class AIController {
  - Paddle paddle
  - Ball ball
  + update(double) void
}

GameEngine "1" *-- "2" Paddle
GameEngine "1" *-- "1" Ball
GameEngine --> Player
AIController --> Paddle
AIController --> Ball
Ball "1" *-- "1" FreezeEffect

%% ================= SKILL =================

class SkillManager {
  + ArrayList~Skill~ inventory
  + ArrayList~Skill~ activeSkills
  + addSkill(SkillType) void
  + useSkill(int) void
  + hasActive(SkillType) boolean
  + update() void
}

class Skill {
  + SkillType type
  + long duration
  + boolean active
  + activate() void
  + isExpired() boolean
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
  + respawn() void
  + draw(Graphics2D) void
}

GameEngine "1" *-- "2" SkillManager
GameEngine "1" *-- "1" SkillBox
SkillManager "1" --> "0..*" Skill
Skill --> SkillType

%% ================= SYSTEM =================

class InputHandler {
  - GameEngine engine
}

class SoundManager {
  - Clip clip
  + playLoop(String) void
  + stop() void
}

class PixelSound {
  + playClick() void
}

class SaveManager {
  + saveSettings() void
  + loadSettings() void
}

class Config {
  + static float masterVolume
  + static float bgmVolume
  + static float sfxVolume
  + static int aiDifficulty
}

GameEngine --> InputHandler
GameEngine --> SoundManager
MenuButton --> PixelSound
SaveManager --> Config
SettingPanel --> Config

%% ================= UI =================

class MenuButton {
  - Rectangle bounds
  + boolean hovered
  + boolean pressed
  + update() void
  + draw(Graphics2D) void
}

class PixelArcadeButton {
}

class PauseOverlay {
  + draw(Graphics) void
}

class ScoreBoard {
  + draw(Graphics) void
}

class SettingPanel {
}

GameEngine --> MenuButton
GameEngine --> PauseOverlay

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
