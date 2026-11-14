package org.misis.tp.lab3.location;

import org.misis.tp.lab3.combat.BattleReport;

public record ExpeditionReport(
        int seed,
        String locationCode,
        String tacticCode,
        BattleReport battle,
        Rewards rewards
) {}
