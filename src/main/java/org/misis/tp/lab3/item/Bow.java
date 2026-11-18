package org.misis.tp.lab3.item;

import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.Dice;

public class Bow extends Weapon {
    private static final int CRITICAL_MULTIPLIER = 2;
    private final double criticalChance;

    public Bow(String code, String name, int basePrice, int baseDamage, double criticalChance) {
        super(code, name, basePrice, baseDamage);
        this.criticalChance = criticalChance;
    }

    private Bow(Bow source) {
        super(source);
        this.criticalChance = source.criticalChance;
    }

    @Override
    public Item copy() { return new Bow(this); }

    @Override
    public Hit rollDamage() {
        if (Dice.getInstance().chance(criticalChance)) {
            return new Hit(getDamage() * CRITICAL_MULTIPLIER, true);
        }
        return new Hit(getDamage(), false);
    }

    @Override
    public String getDescription() {
        return "урон " + getDamage() + ", шанс крита " + Math.round(criticalChance * 100) + "%";
    }
}
