package org.misis.tp.lab3.kit;

import org.misis.tp.lab3.item.Armor;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.item.Weapon;

public class MageKitFactory implements StarterKitFactory {
    private final ItemCatalog catalog;

    public MageKitFactory(ItemCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public Weapon createWeapon() {
        return catalog.createWeapon(DefaultCatalog.APPRENTICE_STAFF);
    }

    @Override
    public Armor createArmor() {
        return catalog.createArmor(DefaultCatalog.LINEN_ROBE);
    }
}
