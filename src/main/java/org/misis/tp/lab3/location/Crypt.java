package org.misis.tp.lab3.location;

import org.misis.tp.lab3.enemy.Enemy;
import org.misis.tp.lab3.enemy.Skeleton;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import java.util.List;

public class Crypt extends Location {
    public Crypt(ItemCatalog catalog) {
        super("crypt", "Заброшенный склеп", 3, catalog);
    }

    @Override
    protected Enemy createEnemy() {
        return new Skeleton();
    }

    @Override
    protected List<String> getLootTable() {
        return List.of(DefaultCatalog.BONE_BOW, DefaultCatalog.CRYPT_MAIL, DefaultCatalog.KNIGHT_BLADE);
    }
}
