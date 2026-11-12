package org.misis.tp.lab3.item;

import org.misis.tp.lab3.combat.Hit;

public class Sword extends Weapon {
    public Sword(String code, String name, int basePrice, int baseDamage) {
        super(code, name, basePrice, baseDamage);
    }

    private Sword(Sword source) { super(source); }

    @Override
    public Item copy() { return new Sword(this); }

    @Override
    public Hit rollDamage() { return new Hit(getDamage(), false); }

    @Override
    public String getDescription() { return "урон " + getDamage(); }
}
