package org.misis.tp.lab3.tactic;

public enum Action {
    ATTACK(1.0, 1.0),
    POWER_STRIKE(1.5, 1.5),
    DEFEND(0.5, 0.3);

    private final double attackMultiplier;
    private final double incomingMultiplier;

    Action(double attackMultiplier, double incomingMultiplier) {
        this.attackMultiplier = attackMultiplier;
        this.incomingMultiplier = incomingMultiplier;
    }

    public double getAttackMultiplier() { return attackMultiplier; }
    public double getIncomingMultiplier() { return incomingMultiplier; }
}
