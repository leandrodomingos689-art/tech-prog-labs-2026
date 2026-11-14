package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.combat.Hit;

public class LegendaryEnemy extends EnemyDecorator {
    public LegendaryEnemy(Enemy wrapped) { super(wrapped); }
    @Override public String getName() { return "Легендарный " + super.getName(); }
    @Override public Hit rollDamage() {
        Hit hit = super.rollDamage();
        return new Hit(hit.damage() * 2, hit.critical());
    }
    @Override public int getExperienceReward() { return super.getExperienceReward() * 2; }
    @Override public int rollGoldReward() { return super.rollGoldReward() * 2; }
}
