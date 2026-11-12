package org.misis.tp.lab3.item;

import java.util.List;

public record Enchantment(String code, String name, int bonusDamage, double bonusDefense, int bonusPrice) {
    public static final Enchantment SHARP = new Enchantment("sharp", "Острое", 3, 0, 20);
    public static final Enchantment FIERY = new Enchantment("fiery", "Огненное", 6, 0, 50);
    public static final Enchantment STURDY = new Enchantment("sturdy", "Прочное", 0, 0.05, 20);
    public static final Enchantment WARDING = new Enchantment("warding", "Охранное", 0, 0.10, 50);

    public static final List<Enchantment> FOR_WEAPONS = List.of(SHARP, FIERY);
    public static final List<Enchantment> FOR_ARMOR = List.of(STURDY, WARDING);
}
