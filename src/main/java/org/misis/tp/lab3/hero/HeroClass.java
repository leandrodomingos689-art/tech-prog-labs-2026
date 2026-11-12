package org.misis.tp.lab3.hero;

import org.misis.tp.lab3.core.UnknownCodeError;
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
