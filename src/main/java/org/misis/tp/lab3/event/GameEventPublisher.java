package org.misis.tp.lab3.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GameEventPublisher {
    private final Map<GameEventType, List<GameEventListener>> listeners = new EnumMap<>(GameEventType.class);

    public void subscribe(GameEventType type, GameEventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(GameEventType type, GameEventListener listener) {
        List<GameEventListener> list = listeners.get(type);
        if (list != null) list.remove(listener);
    }

    public void publish(GameEvent event) {
        List<GameEventListener> list = listeners.get(event.type());
        if (list != null) {
            for (GameEventListener listener : List.copyOf(list)) {
                listener.onEvent(event);
            }
        }
    }
}
