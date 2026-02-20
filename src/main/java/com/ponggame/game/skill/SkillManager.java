package com.ponggame.game.skill;

import java.awt.*;
import java.util.ArrayList;

public class SkillManager {

    public ArrayList<Skill> inventory = new ArrayList<>();
    public ArrayList<Skill> activeSkills = new ArrayList<>();

    public void addSkill(SkillType type) {
        if (inventory.size() >= 2)
            return;

        long duration = 5000;

        if (type == SkillType.TELEPORT)
            duration = 0;
        if (type == SkillType.FREEZE_BALL)
            duration = 1500;

        inventory.add(new Skill(type, duration));
    }

    public void useSkill(int index) {
        if (index >= inventory.size())
            return;

        Skill s = inventory.remove(index);
        s.activate();

        if (s.duration > 0)
            activeSkills.add(s);
        else
            activeSkills.add(s); // instant but handled in engine
    }

    public void update() {
        activeSkills.removeIf(Skill::isExpired);
    }

    public boolean hasActive(SkillType type) {
        for (Skill s : activeSkills)
            if (s.type == type)
                return true;
        return false;
    }

    public void draw(Graphics2D g2, int x, int y) {

        for (int i = 0; i < 2; i++) {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(x + i * 60, y, 50, 50);

            if (i < inventory.size()) {
                g2.setColor(Color.WHITE);
                g2.drawString(inventory.get(i).getShortName(), x + 10 + i * 60, y + 30);
            }
        }
    }

    public void activateFirst() {

        if (inventory.isEmpty())
            return;

        useSkill(0); // ใช้สกิลช่องแรก
    }

}
