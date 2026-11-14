package org.misis.tp.lab3.defense;

public class BarrierHandler extends DamageHandler {
    private int strength;
    public BarrierHandler(int strength) { this.strength = strength; }

    @Override
    public void handle(Damage damage) {
        int absorbed = Math.min(strength, damage.getAmount());
        if (absorbed > 0) {
            strength -= absorbed;
            damage.absorb(absorbed);
        }
        passToNext(damage);
    }
}
