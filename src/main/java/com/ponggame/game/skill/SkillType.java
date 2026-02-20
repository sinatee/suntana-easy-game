package com.ponggame.game.skill;

import java.util.Random;

public enum SkillType {
    SPEED_BOOST,
    SLOW_OPPONENT,
    BIG_PADDLE,
    SMALL_OPPONENT,
    TELEPORT,
    REVERSE_CONTROL,
    FREEZE_BALL,
    CURVE_SHOT;

    private static final SkillType[] VALUES = values();
    private static final Random RANDOM = new Random();

    public static SkillType random() {
        return VALUES[RANDOM.nextInt(VALUES.length)];
    }
}