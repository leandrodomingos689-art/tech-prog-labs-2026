package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.defense.Damage;

public abstract class EnemyDecorator implements Enemy {
    protected final Enemy wrapped;
    protected EnemyDecorator(Enemy wrapped) { this.wrapped = wrapped; }

    @Override public String getCode() { return wrapped.getCode(); }
    @Override public String getName() { return wrapped.getName(); }
    @Override public int getHealth() { return wrapped.getHealth(); }
    @Override public int getMaxHealth() { return wrapped.getMaxHealth(); }
    @Override public int getExperienceReward() { return wrapped.getExperienceReward(); }
    @Override public boolean isAlive() { return wrapped.isAlive(); }
    @Override public Hit rollDamage() { return wrapped.rollDamage(); }
    @Override public Damage receiveHit(int amount) { return wrapped.receiveHit(amount); }
    @Override public int rollGoldReward() { return wrapped.rollGoldReward(); }
}
