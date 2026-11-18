package org.misis.tp.lab3.defense;

import org.misis.tp.lab3.Dice;

public class EvasionHandler extends DamageHandler {
    private final double chance;
    public EvasionHandler(double chance) { this.chance = chance; }

    @Override
    public void handle(Damage damage) {
        if (Dice.getInstance().chance(chance)) {
            damage.evade();
            return;
        }
        passToNext(damage);
    }
}
