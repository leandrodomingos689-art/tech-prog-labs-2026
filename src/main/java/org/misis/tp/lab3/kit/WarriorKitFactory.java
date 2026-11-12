package org.misis.tp.lab3.kit;

import org.misis.tp.lab3.item.Armor;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.item.Weapon;

public class WarriorKitFactory implements StarterKitFactory {
    private final ItemCatalog catalog;

    public WarriorKitFactory(ItemCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public Weapon createWeapon() {
        return catalog.createWeapon(DefaultCatalog.TRAINING_SWORD);
    }

    @Override
    public Armor createArmor() {
        return catalog.createArmor(DefaultCatalog.CHAINMAIL);
    }
}
