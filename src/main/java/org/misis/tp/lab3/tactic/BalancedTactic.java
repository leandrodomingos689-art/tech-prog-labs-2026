package org.misis.tp.lab3.tactic;

import org.misis.tp.lab3.combat.Combatant;

public class BalancedTactic implements Tactic {
    @Override public String getCode() { return "balanced"; }
    @Override public String getDisplayName() { return "Баланс"; }
    @Override public String getDescription() { return "обычный удар каждый раунд"; }
    @Override public Action chooseAction(int round, Combatant hero, Combatant enemy) { return Action.ATTACK; }
}
