package org.misis.tp.lab3.kit;

import org.misis.tp.lab3.item.Armor;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.item.Weapon;

public class RangerKitFactory implements StarterKitFactory {
    private final ItemCatalog catalog;

    public RangerKitFactory(ItemCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public Weapon createWeapon() {
        return catalog.createWeapon(DefaultCatalog.HUNTING_BOW);
    }

    @Override
    public Armor createArmor() {
        return catalog.createArmor(DefaultCatalog.LEATHER_ARMOR);
    }
}
