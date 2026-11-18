package org.misis.tp.lab3.hero;

import org.misis.tp.lab3.UnknownCodeError;
import org.misis.tp.lab3.defense.ArmorHandler;
import org.misis.tp.lab3.defense.BarrierHandler;
import org.misis.tp.lab3.defense.DamageHandler;
import org.misis.tp.lab3.defense.EvasionHandler;
import org.misis.tp.lab3.item.Armor;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.kit.MageKitFactory;
import org.misis.tp.lab3.kit.RangerKitFactory;
import org.misis.tp.lab3.kit.StarterKitFactory;
import org.misis.tp.lab3.kit.WarriorKitFactory;
import java.util.function.Function;

public enum HeroClass {
    WARRIOR("warrior", "Воин", 120, WarriorKitFactory::new),
    RANGER("ranger", "Следопыт", 100, RangerKitFactory::new),
    MAGE("mage", "Маг", 85, MageKitFactory::new);

    private final String code;
    private final String displayName;
    private final int baseHealth;
    private final Function<ItemCatalog, StarterKitFactory> factoryConstructor;

    HeroClass(String code, String displayName, int baseHealth, Function<ItemCatalog, StarterKitFactory> factoryConstructor) {
        this.code = code;
        this.displayName = displayName;
        this.baseHealth = baseHealth;
        this.factoryConstructor = factoryConstructor;
    }

    public StarterKitFactory starterKit(ItemCatalog catalog) {
        return factoryConstructor.apply(catalog);
    }

    public DamageHandler createDefense(Armor armor, int level) {
        double evasion = this == RANGER ? 0.15 : 0;
        int barrier = this == MAGE ? 30 : 0;
        if (this == WARRIOR && level >= 3) {
            barrier = 20;
        } else if (this == RANGER && level >= 5) {
            barrier = 15;
        } else if (this == MAGE && level >= 3) {
            evasion = 0.10;
        }
        double defenseValue = armor != null ? armor.getDefense() : 0.0;
        DamageHandler armorHandler = new ArmorHandler(defenseValue);
        DamageHandler first = armorHandler;
        if (evasion > 0) {
            first = new EvasionHandler(evasion);
            first.setNext(armorHandler);
        }
        if (barrier > 0) {
            armorHandler.setNext(new BarrierHandler(barrier));
        }
        return first;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
    public int getBaseHealth() { return baseHealth; }

    public static HeroClass byCode(String code) {
        for (HeroClass hc : values()) {
            if (hc.code.equals(code)) return hc;
        }
        throw new UnknownCodeError("Класс героя", code);
    }
}
