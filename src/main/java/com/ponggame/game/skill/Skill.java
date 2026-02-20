package com.ponggame.game.skill;

public class Skill {

    public SkillType type;
    public long duration;
    public long startTime;
    public boolean active = false;

    public Skill(SkillType type, long duration) {
        this.type = type;
        this.duration = duration;
    }

    public void activate() {
        active = true;
        startTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        if (!active)
            return false;

        if (duration == 0)
            return true; // instant skill

        return System.currentTimeMillis() - startTime > duration;
    }

    public String getShortName() {
        switch (type) {
            case SPEED_BOOST: return "SPD";
            case SLOW_OPPONENT: return "SLOW";
            case BIG_PADDLE: return "BIG";
            case SMALL_OPPONENT: return "SMALL";
            case TELEPORT: return "TP";
            case REVERSE_CONTROL: return "REV";
            case FREEZE_BALL: return "FRZ";
            case CURVE_SHOT: return "CRV";
        }
        return "";
    }
}