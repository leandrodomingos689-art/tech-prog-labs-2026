package org.misis.tp.lab3.tactic;

import org.misis.tp.lab3.combat.Combatant;

public interface Tactic {
    String getCode();
    String getDisplayName();
    String getDescription();
    Action chooseAction(int round, Combatant hero, Combatant enemy);
}
