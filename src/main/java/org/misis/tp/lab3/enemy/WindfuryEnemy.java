package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.combat.Hit;

public class WindfuryEnemy extends EnemyDecorator {
    public WindfuryEnemy(Enemy wrapped) { super(wrapped); }
    @Override public String getName() { return "Неистовый " + super.getName(); }
    @Override public Hit rollDamage() {
        Hit first = super.rollDamage(), second = super.rollDamage();
        return new Hit(first.damage() + second.damage(), first.critical() || second.critical());
    }
    @Override public int getExperienceReward() { return withBonus(super.getExperienceReward()); }
    @Override public int rollGoldReward() { return withBonus(super.rollGoldReward()); }
    private int withBonus(int reward) { return reward + (reward * 50 / 100); }
}
