package org.misis.tp.lab3.camp;

import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.item.Item;
import org.misis.tp.lab3.item.ItemCatalog;

import java.util.List;

public class Merchant {
    private final ItemCatalog catalog;
    private final List<String> itemCodes;

    public Merchant(ItemCatalog catalog, List<String> itemCodes) {
        this.catalog = catalog;
        this.itemCodes = List.copyOf(itemCodes);
    }

    public List<Item> getShowcase() {
        return itemCodes.stream()
                .map(catalog::createItem)
                .toList();
    }

    public int getSellPrice(Item item) {
        return Math.max(1, item.getPrice() / 2);
    }

    public Item buy(Hero hero, String code) {
        if (!itemCodes.contains(code)) {
            throw new IllegalArgumentException("Товар не продается: " + code);
        }
        Item item = catalog.createItem(code);
        hero.spendGold(item.getPrice());
        hero.addItem(item);
        return item;
    }

    public int sell(Hero hero, Item item) {
        if (!hero.getInventory().contains(item)) {
            throw new IllegalArgumentException("Предмет не найден в инвентаре");
        }
        int price = getSellPrice(item);
        hero.removeItem(item);
        hero.addGold(price);
        return price;
    }
}
