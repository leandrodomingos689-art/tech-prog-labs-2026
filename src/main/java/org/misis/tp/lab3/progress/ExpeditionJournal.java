package org.misis.tp.lab3.progress;

import org.misis.tp.lab3.event.GameEvent;
import org.misis.tp.lab3.event.GameEventListener;
import org.misis.tp.lab3.location.ExpeditionReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpeditionJournal implements GameEventListener {
    private final Map<String, List<ExpeditionReport>> entries = new HashMap<>();

    @Override
    public void onEvent(GameEvent event) {
        entries.computeIfAbsent(event.hero().getName(), k -> new ArrayList<>()).add(event.expedition());
    }

    public List<ExpeditionReport> getEntries(String name) {
        return List.copyOf(entries.getOrDefault(name, List.of()));
    }
}
