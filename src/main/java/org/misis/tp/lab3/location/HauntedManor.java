package org.misis.tp.lab3.location;

import org.misis.tp.lab3.Dice;
import org.misis.tp.lab3.enemy.Enemy;
import org.misis.tp.lab3.enemy.HauntedWeapon;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.item.Weapon;

import java.util.List;

public class HauntedManor extends Location {
    public static final String CODE = "haunted-manor";

    public HauntedManor(ItemCatalog catalog) {
        super(CODE, "Проклятый особняк", 2, catalog);
    }

    @Override
    protected List<String> getLootTable() {
        return List.of(DefaultCatalog.IRON_SWORD, DefaultCatalog.ELVEN_BOW, DefaultCatalog.EMBER_STAFF);
    }

    @Override
    protected Enemy createEnemy() {
        String weaponCode = Dice.getInstance().pick(getLootTable());
        return new HauntedWeapon((Weapon) catalog.createItem(weaponCode));
    }
}
