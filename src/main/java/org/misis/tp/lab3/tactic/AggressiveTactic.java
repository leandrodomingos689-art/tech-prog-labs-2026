package org.misis.tp.lab3.tactic;

import org.misis.tp.lab3.combat.Combatant;

public class AggressiveTactic implements Tactic {
    @Override public String getCode() { return "aggressive"; }
    @Override public String getDisplayName() { return "Натиск"; }
    @Override public String getDescription() { return "совершать безрассудные удары, пока у врага больше здоровья, чем у героя"; }

    @Override
    public Action chooseAction(int round, Combatant hero, Combatant enemy) {
        return enemy.getHealth() > hero.getHealth() ? Action.POWER_STRIKE : Action.ATTACK;
    }
}
