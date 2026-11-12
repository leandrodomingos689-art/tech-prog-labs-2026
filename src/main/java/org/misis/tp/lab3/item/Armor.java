package org.misis.tp.lab3.item;

import java.util.List;

public class Armor extends Item {
    private static final double MAX_DEFENSE = 0.75;
    private final double baseDefense;

    public Armor(String code, String name, int basePrice, double baseDefense) {
        super(code, name, basePrice);
        this.baseDefense = baseDefense;
    }

    private Armor(Armor source) {
        super(source);
        this.baseDefense = source.baseDefense;
    }

    @Override
    public Item copy() { return new Armor(this); }

    public double getDefense() {
        double bonus = getEnchantments().stream().mapToDouble(Enchantment::bonusDefense).sum();
        return Math.min(baseDefense + bonus, MAX_DEFENSE);
    }

    @Override
    public List<Enchantment> getPossibleEnchantments() {
        return Enchantment.FOR_ARMOR;
    }

    @Override
    public String getDescription() {
        return "защита " + Math.round(getDefense() * 100) + "%";
    }
}
