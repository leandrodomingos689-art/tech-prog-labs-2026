package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.Dice;
import org.misis.tp.lab3.defense.ArmorHandler;

public class Dragon extends AbstractEnemy {
    private static final double RESISTANCE = 0.25;
    private static final double FIRE_BREATH_CHANCE = 0.25;
    private static final int FIRE_BREATH_MULTIPLIER = 2;

    public Dragon() {
        super("dragon", "Дракон", 260, 16, 22, 500, 400, new ArmorHandler(RESISTANCE));
    }

    @Override
    public Hit rollDamage() {
        Hit bite = super.rollDamage();
        if (Dice.getInstance().chance(FIRE_BREATH_CHANCE)) {
            return new Hit(bite.damage() * FIRE_BREATH_MULTIPLIER, true);
        }
        return bite;
    }
}
