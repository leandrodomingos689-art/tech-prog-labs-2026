package org.misis.tp.lab3;

import org.misis.tp.lab3.combat.*;
import org.misis.tp.lab3.core.Dice;
import org.misis.tp.lab3.hero.*;
import org.misis.tp.lab3.item.*;
import org.misis.tp.lab3.kit.StarterKitFactory;
import org.misis.tp.lab3.location.*;

public class Main {
    public static void main(String[] args) {
        ItemCatalog catalog = DefaultCatalog.create();

        System.out.println("===== Одиночка =====");
        Dice d1 = Dice.getInstance();
        Dice d2 = Dice.getInstance();
        System.out.println("Dice.getInstance() возвращает один и тот же объект: " + (d1 == d2));

        System.out.println("\n===== Прототип =====");
        Item copy1 = catalog.createItem(DefaultCatalog.IRON_SWORD);
        Item copy2 = catalog.createItem(DefaultCatalog.IRON_SWORD);
        copy2.enchant(Enchantment.FIERY);
        Item copy3 = catalog.createItem(DefaultCatalog.IRON_SWORD);
        System.out.println("Первая копия:        " + copy1.getName() + " (" + copy1.getDescription() + ")");
        System.out.println("Вторая копия:        " + copy2.getName() + " (" + copy2.getDescription() + ")");
        System.out.println("Новая копия шаблона: " + copy3.getName() + " (" + copy3.getDescription() + ")");
        System.out.println("Копии - разные объекты: " + (copy1 != copy2));

        System.out.println("\n===== Абстрактная фабрика =====");
        for (HeroClass hc : HeroClass.values()) {
            StarterKitFactory kit = hc.starterKit(catalog);
            Weapon w = kit.createWeapon();
            Armor a = kit.createArmor();
            System.out.println(hc.getDisplayName() + ": " + w.getName() + " (" + w.getDescription() + "), " + a.getName() + " (" + a.getDescription() + ")");
        }

        System.out.println("\n===== Строитель =====");
        StarterKitFactory warriorKit = HeroClass.WARRIOR.starterKit(catalog);
        Hero arthur = new Hero.Builder()
                .name("Артур")
                .heroClass(HeroClass.WARRIOR)
                .level(3)
                .gold(50)
                .weapon(warriorKit.createWeapon())
                .armor(warriorKit.createArmor())
                .build();

        System.out.println(arthur.getName() + ", " + arthur.getHeroClass().getDisplayName() + " " + arthur.getLevel() + " ур. | здоровье " + arthur.getMaxHealth() + " | опыт " + arthur.getExperience() + "/" + arthur.getExperienceToNextLevel() + " | золото " + arthur.getGold());
        System.out.println("  Оружие: " + arthur.getWeapon().getName() + " (" + arthur.getWeapon().getDescription() + ")");
        System.out.println("  Броня:  " + arthur.getArmor().getName() + " (" + arthur.getArmor().getDescription() + ")");

        try {
            new Hero.Builder().heroClass(HeroClass.MAGE).build();
        } catch (IllegalStateException e) {
            System.out.println("Герой без имени не создается: " + e.getMessage());
        }

        try {
            new Hero.Builder().name("Мерлин").build();
        } catch (IllegalStateException e) {
            System.out.println("Герой без класса не создается: " + e.getMessage());
        }

        System.out.println("\n===== Фабричный метод =====");
        runExpedition(new Forest(catalog), arthur, 7);
        System.out.println();
        runExpedition(new Crypt(catalog), arthur, 7);
    }

    private static void runExpedition(Location location, Hero hero, int seed) {
        ExpeditionReport report = location.explore(hero, seed);
        BattleReport battle = report.battle();
        System.out.println("=== " + location.getDisplayName() + " (сид " + report.seed() + ") ===");
        System.out.println(battle.hero().name() + " встречает врага: " + battle.enemy().name() + " (" + battle.enemy().maxHealth() + " здоровья)");

        for (BattleEvent ev : battle.events()) {
            String attackerName = (ev.source() == Side.HERO) ? battle.hero().name() : battle.enemy().name();
            String defenderName = (ev.target() == Side.HERO) ? battle.hero().name() : battle.enemy().name();
            int defenderMaxHp = (ev.target() == Side.HERO) ? battle.hero().maxHealth() : battle.enemy().maxHealth();

            if (ev.type() == EventType.HIT || ev.type() == EventType.CRITICAL_HIT) {
                System.out.println("[раунд " + ev.round() + "] " + attackerName + " -> " + defenderName + ": " + ev.amount() + " урона, осталось " + ev.targetHealthAfter() + "/" + defenderMaxHp);
            } else if (ev.type() == EventType.DEATH) {
                System.out.println(defenderName + " повержен!");
            }
        }

        if (battle.outcome() == Outcome.VICTORY) {
            System.out.println("Победа! +" + report.rewards().experience() + " опыта, +" + report.rewards().gold() + " золота");
        }
    }
}
