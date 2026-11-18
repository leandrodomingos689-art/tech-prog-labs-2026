package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.Dice;
import org.misis.tp.lab3.defense.Damage;
import org.misis.tp.lab3.defense.DamageHandler;

public abstract class AbstractEnemy implements Enemy {
    private final String code;
    private final String name;
    private final int maxHealth;
    private final int minDamage;
    private final int maxDamage;
    private final int experienceReward;
    private final int goldReward;
    private final DamageHandler defense;
    private int currentHealth;

    protected AbstractEnemy(String code, String name, int maxHealth, int minDamage, int maxDamage,
                            int experienceReward, int goldReward) {
        this(code, name, maxHealth, minDamage, maxDamage, experienceReward, goldReward, null);
    }

    protected AbstractEnemy(String code, String name, int maxHealth, int minDamage, int maxDamage,
                            int experienceReward, int goldReward, DamageHandler defense) {
        this.code = code;
        this.name = name;
        this.maxHealth = maxHealth;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.experienceReward = experienceReward;
        this.goldReward = goldReward;
        this.defense = defense;
        this.currentHealth = maxHealth;
    }

    @Override public String getCode() { return code; }
    @Override public String getName() { return name; }
    @Override public int getHealth() { return currentHealth; }
    @Override public int getMaxHealth() { return maxHealth; }
    @Override public int getExperienceReward() { return experienceReward; }
    @Override public boolean isAlive() { return currentHealth > 0; }

    @Override
    public Hit rollDamage() {
        return new Hit(Dice.getInstance().roll(minDamage, maxDamage), false);
    }

    @Override
    public Damage receiveHit(int amount) {
        Damage damage = new Damage(amount);
        if (defense != null) {
            defense.handle(damage);
        }
        currentHealth = Math.max(0, currentHealth - damage.getAmount());
        return damage;
    }

    @Override
    public int rollGoldReward() {
        return Dice.getInstance().roll(goldReward / 2, goldReward);
    }
}
