package org.misis.tp.lab3.enemy;

public class Skeleton extends AbstractEnemy {
    private static final double RESISTANCE = 0.3;

    public Skeleton() {
        super("skeleton", "Скелет", 110, 10, 24, 90, 70);
    }

    @Override
    protected int reduceDamage(int damage) {
        return (int) Math.round(damage * (1 - RESISTANCE));
    }
}
