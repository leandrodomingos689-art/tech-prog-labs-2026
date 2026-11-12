package org.misis.tp.lab3.item;

import org.misis.tp.lab3.combat.Hit;
import java.util.List;

public abstract class Weapon extends Item {
    private final int baseDamage;

    protected Weapon(String code, String name, int basePrice, int baseDamage) {
        super(code, name, basePrice);
        this.baseDamage = baseDamage;
    }

    protected Weapon(Weapon source) {
        super(source);
        this.baseDamage = source.baseDamage;
    }

    public abstract Hit rollDamage();

    public int getDamage() {
        int bonus = getEnchantments().stream().mapToInt(Enchantment::bonusDamage).sum();
        return baseDamage + bonus;
    }

    @Override
    public List<Enchantment> getPossibleEnchantments() {
        return Enchantment.FOR_WEAPONS;
    }
}
