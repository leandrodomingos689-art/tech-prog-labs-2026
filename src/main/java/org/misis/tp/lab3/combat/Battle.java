package org.misis.tp.lab3.combat;

import org.misis.tp.lab3.defense.Damage;
import org.misis.tp.lab3.tactic.Action;
import org.misis.tp.lab3.tactic.BalancedTactic;
import org.misis.tp.lab3.tactic.Tactic;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    private final Combatant hero;
    private final Combatant enemy;
    private final Tactic tactic;
    private final List<BattleEvent> events = new ArrayList<>();
    private int round = 0;

    public Battle(Combatant hero, Combatant enemy) {
        this(hero, enemy, new BalancedTactic());
    }

    public Battle(Combatant hero, Combatant enemy, Tactic tactic) {
        this.hero = hero;
        this.enemy = enemy;
        this.tactic = tactic;
    }

    public BattleReport run() {
        while (hero.isAlive() && enemy.isAlive()) {
            round++;
            Action action = tactic.chooseAction(round, hero, enemy);
            if (action != Action.ATTACK) {
                emit(new BattleEvent(
                        round,
                        action == Action.POWER_STRIKE ? EventType.POWER_STRIKE : EventType.DEFEND,
                        Side.HERO,
                        action == Action.POWER_STRIKE ? Side.ENEMY : Side.HERO,
                        0,
                        0
                ));
            }
            strike(hero, Side.HERO, enemy, Side.ENEMY, action.getAttackMultiplier());
            if (enemy.isAlive()) {
                strike(enemy, Side.ENEMY, hero, Side.HERO, action.getIncomingMultiplier());
            }
        }

        return new BattleReport(
                Participant.of(hero),
                Participant.of(enemy),
                hero.isAlive() ? Outcome.VICTORY : Outcome.DEFEAT,
                List.copyOf(events)
        );
    }

    private void strike(Combatant attacker, Side source, Combatant defender, Side target, double multiplier) {
        Hit hit = attacker.rollDamage();
        Damage damage = defender.receiveHit(Math.max(1, (int) Math.round(hit.damage() * multiplier)));
        if (damage.isEvaded()) {
            emit(new BattleEvent(round, EventType.EVADE, source, target, 0, defender.getHealth()));
            return;
        }
        if (damage.getAbsorbed() > 0) {
            emit(new BattleEvent(round, EventType.ABSORB, source, target, damage.getAbsorbed(), defender.getHealth()));
        }
        if (damage.getAmount() > 0) {
            EventType type = hit.critical() ? EventType.CRITICAL_HIT : EventType.HIT;
            emit(new BattleEvent(round, type, source, target, damage.getAmount(), defender.getHealth()));
        }
        if (!defender.isAlive()) {
            emit(new BattleEvent(round, EventType.DEATH, source, target, 0, 0));
        }
    }

    private void emit(BattleEvent event) {
        events.add(event);
    }
}
