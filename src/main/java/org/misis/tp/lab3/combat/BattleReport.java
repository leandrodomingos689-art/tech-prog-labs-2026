package org.misis.tp.lab3.combat;

import java.util.List;

public record BattleReport(Participant hero, Participant enemy, Outcome outcome, List<BattleEvent> events) {}
