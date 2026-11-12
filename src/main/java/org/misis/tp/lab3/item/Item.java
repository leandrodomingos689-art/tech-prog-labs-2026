package org.misis.tp.lab3.item;

import java.util.ArrayList;
import java.util.List;

public abstract class Item {
    private final String code;
    private final String name;
    private final int basePrice;
    private final List<Enchantment> enchantments;

    protected Item(String code, String name, int basePrice) {
        this.code = code;
        this.name = name;
        this.basePrice = basePrice;
        this.enchantments = new ArrayList<>();
    }

    protected Item(Item source) {
        this.code = source.code;
        this.name = source.name;
        this.basePrice = source.basePrice;
        this.enchantments = new ArrayList<>(source.enchantments);
    }

    public abstract Item copy();
    public abstract List<Enchantment> getPossibleEnchantments();
    public abstract String getDescription();

    public void enchant(Enchantment enchantment) {
        if (!getPossibleEnchantments().contains(enchantment)) {
            throw new IllegalArgumentException("Зачарование '" + enchantment.name() + "' не подходит для предмета '" + name + "'");
        }
        enchantments.add(enchantment);
    }

    public String getCode() { return code; }

    public String getName() {
        if (enchantments.isEmpty()) return name;
        String enchNames = String.join(", ", enchantments.stream().map(Enchantment::name).toList());
        return name + " [" + enchNames + "]";
    }

    public List<Enchantment> getEnchantments() { return List.copyOf(enchantments); }

    public int getPrice() {
        int bonus = enchantments.stream().mapToInt(Enchantment::bonusPrice).sum();
        return basePrice + bonus;
    }
}
