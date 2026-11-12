package org.misis.tp.lab3.item;

import org.misis.tp.lab3.core.UnknownCodeError;
import java.util.HashMap;
import java.util.Map;

public class ItemCatalog {
    private final Map<String, Item> templates = new HashMap<>();

    public void register(Item item) {
        templates.put(item.getCode(), item);
    }

    public Item createItem(String code) {
        Item template = templates.get(code);
        if (template == null) {
            throw new UnknownCodeError("Предмет", code);
        }
        return template.copy();
    }

    public Weapon createWeapon(String code) {
        Item item = createItem(code);
        if (!(item instanceof Weapon weapon)) {
            throw new IllegalArgumentException("Предмет '" + code + "' не является оружием");
        }
        return weapon;
    }

    public Armor createArmor(String code) {
        Item item = createItem(code);
        if (!(item instanceof Armor armor)) {
            throw new IllegalArgumentException("Предмет '" + code + "' не является бронёй");
        }
        return armor;
    }
}
