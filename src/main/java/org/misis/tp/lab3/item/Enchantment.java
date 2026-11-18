package org.misis.tp.lab3.item;

import org.misis.tp.lab3.UnknownCodeError;
import java.util.List;
import java.util.stream.Stream;

public record Enchantment(String code, String name, int bonusDamage, double bonusDefense, int bonusPrice) {
    public static final Enchantment SHARP = new Enchantment("sharp", "Острое", 3, 0, 20);
    public static final Enchantment FIERY = new Enchantment("fiery", "Огненное", 6, 0, 50);
    public static final Enchantment STURDY = new Enchantment("sturdy", "Прочное", 0, 0.05, 20);
    public static final Enchantment WARDING = new Enchantment("warding", "Охранное", 0, 0.10, 50);

    public static final List<Enchantment> FOR_WEAPONS = List.of(SHARP, FIERY);
    public static final List<Enchantment> FOR_ARMOR = List.of(STURDY, WARDING);

    public static Enchantment byCode(String code) {
        return Stream.concat(FOR_WEAPONS.stream(), FOR_ARMOR.stream())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new UnknownCodeError("Зачарование", code));
    }
}
