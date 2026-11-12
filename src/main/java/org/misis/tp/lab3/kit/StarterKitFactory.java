package org.misis.tp.lab3.kit;

import org.misis.tp.lab3.item.Armor;
import org.misis.tp.lab3.item.Weapon;

public interface StarterKitFactory {
    Weapon createWeapon();
    Armor createArmor();
}
