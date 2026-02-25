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

    public void draw(Graphics2D g2, int x, int y, String key1, String key2) {

    g2.setFont(new Font("Segoe UI", Font.BOLD, 14));

    String[] keys = { key1, key2 };

    for (int i = 0; i < 2; i++) {

        int boxX = x + i * 80;
        int boxY = y;

        // กล่องพื้น
        g2.setColor(new Color(40, 40, 40));
        g2.fillRoundRect(boxX, boxY, 70, 60, 15, 15);

        // ขอบกล่อง
        g2.setColor(new Color(255, 59, 59));
        g2.drawRoundRect(boxX, boxY, 70, 60, 15, 15);

        // ปุ่มกำกับ
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("[" + keys[i] + "]", boxX + 20, boxY + 15);

        if (i < inventory.size()) {
            g2.setColor(Color.YELLOW);
            g2.drawString(
                inventory.get(i).getShortName(),
                boxX + 18,
                boxY + 40
            );
        }
    }
}

    public void activateFirst() {

        if (inventory.isEmpty())
            return;

        useSkill(0); // ใช้สกิลช่องแรก
    }

}
