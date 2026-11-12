package org.misis.tp.lab3.combat;

public record Participant(String code, String name, int maxHealth) {
    public static Participant of(Combatant combatant) {
        return new Participant(combatant.getCode(), combatant.getName(), combatant.getMaxHealth());
    }
}
