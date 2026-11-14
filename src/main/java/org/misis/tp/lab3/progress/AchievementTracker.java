package org.misis.tp.lab3.progress;

import org.misis.tp.lab3.event.GameEvent;
import org.misis.tp.lab3.event.GameEventListener;
import org.misis.tp.lab3.event.GameEventType;

public class AchievementTracker implements GameEventListener {
    @Override
    public void onEvent(GameEvent event) {
        if (event.type() == GameEventType.VICTORY) {
            event.hero().unlock(Achievement.FIRST_BLOOD);
            if ("dragon".equals(event.expedition().battle().enemy().code())) {
                event.hero().unlock(Achievement.DRAGON_SLAYER);
            }
            if (event.expedition().rewards().loot() != null &&
                    !event.expedition().rewards().loot().getEnchantments().isEmpty()) {
                event.hero().unlock(Achievement.LUCKY);
            }
        } else if (event.type() == GameEventType.LEVEL_UP && event.hero().getLevel() >= 5) {
            event.hero().unlock(Achievement.VETERAN);
        }
    }
}
