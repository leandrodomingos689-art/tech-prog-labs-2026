package org.misis.tp.lab3.event;

import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.location.ExpeditionReport;

public record GameEvent(GameEventType type, Hero hero, ExpeditionReport expedition) {}
