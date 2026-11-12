package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.core.Dice;

public abstract class AbstractEnemy implements Enemy {
    private final String code;
    private final String name;
    private final int maxHealth;
    private final int minDamage;
    private final int maxDamage;
    private final int experienceReward;
    private final int goldReward;
    private int health;

    protected AbstractEnemy(String code, String name, int maxHealth, int minDamage, int maxDamage, int experienceReward, int goldReward) {
        this.code = code;
        this.name = name;
        this.maxHealth = maxHealth;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.experienceReward = experienceReward;
        this.goldReward = goldReward;
        this.health = maxHealth;
    }

    @Override
    public Hit rollDamage() {
        return new Hit(Dice.getInstance().roll(minDamage, maxDamage), false);
    }

    @Override
    public int receiveHit(int damage) {
        int dealt = Math.max(1, reduceDamage(damage));
        this.health = Math.max(0, this.health - dealt);
        return dealt;
    }

    protected int reduceDamage(int damage) { return damage; }
    @Override public boolean isAlive() { return health > 0; }
    @Override public int rollGoldReward() { return Dice.getInstance().roll(goldReward / 2, goldReward); }
    @Override public int getExperienceReward() { return experienceReward; }
    @Override public String getCode() { return code; }
    @Override public String getName() { return name; }
    @Override public int getHealth() { return health; }
    @Override public int getMaxHealth() { return maxHealth; }
}
