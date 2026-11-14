package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.defense.ArmorHandler;

public class Skeleton extends AbstractEnemy {
    private static final double RESISTANCE = 0.3;

    public Skeleton() {
        super("skeleton", "Скелет", 110, 10, 24, 90, 70, new ArmorHandler(RESISTANCE));
    }
}
