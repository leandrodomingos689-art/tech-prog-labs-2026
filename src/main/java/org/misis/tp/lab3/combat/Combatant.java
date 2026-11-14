package org.misis.tp.lab3.combat;

import org.misis.tp.lab3.defense.Damage;

public interface Combatant {
    String getCode();
    String getName();
    int getHealth();
    int getMaxHealth();
    boolean isAlive();
    Hit rollDamage();
    Damage receiveHit(int damage);
}
