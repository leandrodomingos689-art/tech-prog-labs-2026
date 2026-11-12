package org.misis.tp.lab3.enemy;

import org.misis.tp.lab3.combat.Combatant;

public interface Enemy extends Combatant {
    int getExperienceReward();
    int rollGoldReward();
}
