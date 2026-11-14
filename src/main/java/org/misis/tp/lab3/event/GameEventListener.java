package org.misis.tp.lab3.event;

@FunctionalInterface
public interface GameEventListener {
    void onEvent(GameEvent event);
}
