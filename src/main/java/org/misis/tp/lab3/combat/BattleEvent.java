package org.misis.tp.lab3.combat;

public record BattleEvent(int round, EventType type, Side source, Side target, int amount, int targetHealthAfter) {}
