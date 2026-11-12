package org.misis.tp.lab3.combat;

public interface Combatant {
    String getCode();
    String getName();
    int getHealth();
    int getMaxHealth();
    boolean isAlive();
    Hit rollDamage();
    int receiveHit(int damage);
}
