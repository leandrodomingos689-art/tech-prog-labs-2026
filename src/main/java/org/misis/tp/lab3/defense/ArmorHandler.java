package org.misis.tp.lab3.defense;

public class ArmorHandler extends DamageHandler {
    private final double defense;
    public ArmorHandler(double defense) { this.defense = defense; }

    @Override
    public void handle(Damage damage) {
        damage.reduceTo(Math.max(1, (int) Math.round(damage.getAmount() * (1 - defense))));
        passToNext(damage);
    }
}
