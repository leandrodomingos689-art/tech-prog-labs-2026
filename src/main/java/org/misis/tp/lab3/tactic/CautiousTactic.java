package org.misis.tp.lab3.tactic;

import org.misis.tp.lab3.combat.Combatant;

public class CautiousTactic implements Tactic {
    @Override public String getCode() { return "cautious"; }
    @Override public String getDisplayName() { return "Осторожность"; }
    @Override public String getDescription() { return "уходить в оборону, когда здоровье опускается ниже половины"; }

    @Override
    public Action chooseAction(int round, Combatant hero, Combatant enemy) {
        return hero.getHealth() < hero.getMaxHealth() / 2 ? Action.DEFEND : Action.ATTACK;
    }
}
