package org.misis.tp.lab3.item;

import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.Dice;

public class Staff extends Weapon {
    private final double scatter;

    public Staff(String code, String name, int basePrice, int baseDamage, double scatter) {
        super(code, name, basePrice, baseDamage);
        this.scatter = scatter;
    }

    private Staff(Staff source) {
        super(source);
        this.scatter = source.scatter;
    }

    @Override
    public Item copy() { return new Staff(this); }

    private int getMinDamage() { return (int) Math.round(getDamage() * (1 - scatter)); }
    private int getMaxDamage() { return (int) Math.round(getDamage() * (1 + scatter)); }

    @Override
    public Hit rollDamage() {
        return new Hit(Dice.getInstance().roll(getMinDamage(), getMaxDamage()), false);
    }

    @Override
    public String getDescription() {
        return "урон " + getMinDamage() + "-" + getMaxDamage();
    }
}
