package org.misis.tp.lab3.location;

import org.misis.tp.lab3.enemy.Enemy;
import org.misis.tp.lab3.enemy.Goblin;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import java.util.List;

public class Forest extends Location {
    public Forest(ItemCatalog catalog) {
        super("forest", "Мистический лес", 1, catalog);
    }

    @Override
    protected Enemy createEnemy() {
        return new Goblin();
    }

    @Override
    protected List<String> getLootTable() {
        return List.of(DefaultCatalog.GOBLIN_CLEAVER, DefaultCatalog.LEATHER_ARMOR);
    }
}
