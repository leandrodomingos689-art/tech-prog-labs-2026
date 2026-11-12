package org.misis.tp.lab3.combat;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    private final Combatant hero;
    private final Combatant enemy;
    private final List<BattleEvent> events = new ArrayList<>();
    private int round = 0;

    public Battle(Combatant hero, Combatant enemy) {
        this.hero = hero;
        this.enemy = enemy;
    }

    public BattleReport run() {
        while (hero.isAlive() && enemy.isAlive()) {
            round++;
            strike(hero, Side.HERO, enemy, Side.ENEMY);
            if (enemy.isAlive()) {
                strike(enemy, Side.ENEMY, hero, Side.HERO);
            }
        }
        Outcome outcome = hero.isAlive() ? Outcome.VICTORY : Outcome.DEFEAT;
        return new BattleReport(Participant.of(hero), Participant.of(enemy), outcome, List.copyOf(events));
    }

    private void strike(Combatant attacker, Side source, Combatant defender, Side target) {
        Hit hit = attacker.rollDamage();
        int dealt = defender.receiveHit(hit.damage());
        EventType eventType = hit.critical() ? EventType.CRITICAL_HIT : EventType.HIT;
        events.add(new BattleEvent(round, eventType, source, target, dealt, defender.getHealth()));

        if (!defender.isAlive()) {
            events.add(new BattleEvent(round, EventType.DEATH, source, target, 0, 0));
        }
    }
}
