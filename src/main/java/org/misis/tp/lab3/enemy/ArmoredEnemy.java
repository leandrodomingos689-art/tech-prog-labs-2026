package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.defense.Damage;

public class ArmoredEnemy extends EnemyDecorator {
    public ArmoredEnemy(Enemy wrapped) { super(wrapped); }
    @Override public String getName() { return "Бронированный " + super.getName(); }
    @Override public Damage receiveHit(int amount) {
        return super.receiveHit(Math.max(1, (int) Math.round(amount * 0.5)));
    }
}
