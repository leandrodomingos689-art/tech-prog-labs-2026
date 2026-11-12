package org.misis.tp.lab3.location;

import org.misis.tp.lab3.enemy.Dragon;
import org.misis.tp.lab3.enemy.Enemy;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import java.util.List;

public class DragonLair extends Location {
    public DragonLair(ItemCatalog catalog) {
        super("lair", "Логово дракона", 5, catalog);
    }

    @Override
    protected Enemy createEnemy() {
        return new Dragon();
    }

    @Override
    protected List<String> getLootTable() {
        return List.of(DefaultCatalog.DAWN_BLADE, DefaultCatalog.DRAGONSCALE_ARMOR);
    }
}
