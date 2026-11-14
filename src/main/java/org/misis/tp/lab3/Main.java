package org.misis.tp.lab3;

import org.misis.tp.lab3.combat.Outcome;
import org.misis.tp.lab3.game.GameFacade;
import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.hero.HeroClass;
import org.misis.tp.lab3.item.Item;
import org.misis.tp.lab3.location.ExpeditionReport;
import org.misis.tp.lab3.location.Location;
import org.misis.tp.lab3.tactic.Tactic;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final ConsoleView view = new ConsoleView();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Path saveFile = Path.of("saves", "heroes.json");
        System.out.println("Файл сохранения: " + saveFile.toAbsolutePath());
        GameFacade game = new GameFacade(saveFile);
        Hero hero = chooseHero(game);
        System.out.println(hero.getName() + " разбивает лагерь на опушке мистического леса.");

        while (true) {
            System.out.println();
            view.printHero(hero);
            System.out.println("1 - отправиться в экспедицию, 2 - снаряжение, 3 - торговец, 4 - журнал, 0 - выйти из игры");
            switch (readLine()) {
                case "1" -> goOnExpedition(hero, game);
                case "2" -> manageEquipment(hero, game);
                case "3" -> visitMerchant(hero, game);
                case "4" -> {
                    for (ExpeditionReport report : game.getJournal(hero.getName())) {
                        System.out.println(view.summarize(
                                report,
                                game.getLocation(report.locationCode()).getDisplayName(),
                                game.getTactic(report.tacticCode()).getDisplayName()
                        ));
                    }
                }
                case "0" -> System.exit(0);
                default -> System.out.println("Такого варианта нет");
            }
        }
    }

    private static Hero chooseHero(GameFacade game) {
        List<Hero> heroes = game.getHeroes();
        if (heroes.isEmpty()) return createHero(game);
        System.out.println("Выберите сохраненного героя (0 - создать нового):");
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            System.out.println((i + 1) + " - " + h.getName() + ", " + h.getHeroClass().getDisplayName() + " " + h.getLevel() + " ур.");
        }
        int index = readChoice(heroes.size());
        return index >= 0 ? heroes.get(index) : createHero(game);
    }

    private static Hero createHero(GameFacade game) {
        System.out.println("Создайте своего героя!");
        while (true) {
            System.out.println("Выберите класс:");
            HeroClass[] classes = game.getHeroClasses();
            for (int i = 0; i < classes.length; i++) {
                HeroClass hc = classes[i];
                List<Item> kit = game.getStarterKit(hc.getCode());
                System.out.println((i + 1) + " - " + hc.getDisplayName() + " (здоровье " + hc.getBaseHealth() + "): " +
                        view.describe(kit.get(0)) + ", " + view.describe(kit.get(1)));
            }
            int classIndex = readChoice(classes.length);
            if (classIndex < 0) continue;

            System.out.println("Введите имя:");
            String name = readLine();
            try {
                return game.createHero(name, classes[classIndex].getCode());
            } catch (Exception e) {
                System.out.println("Не получилось создать героя: " + e.getMessage());
            }
        }
    }

    private static void goOnExpedition(Hero hero, GameFacade game) {
        List<Location> all = game.getLocations();
        System.out.println("Куда отправимся?");
        for (int i = 0; i < all.size(); i++) {
            Location loc = all.get(i);
            System.out.println((i + 1) + " - " + loc.getDisplayName() + " (рекомендуемый уровень " + loc.getRecommendedLevel() + ")");
        }
        int index = readChoice(all.size());
        if (index < 0) return;

        Location location = all.get(index);
        System.out.println("Выберите тактику:");
        List<Tactic> tactics = game.getTactics();
        for (int i = 0; i < tactics.size(); i++) {
            Tactic t = tactics.get(i);
            System.out.println((i + 1) + " - " + t.getDisplayName() + ": " + t.getDescription());
        }
        int tacticIndex = readChoice(tactics.size());
        if (tacticIndex < 0) return;

        ExpeditionReport report = game.explore(hero.getName(), location.getCode(), tactics.get(tacticIndex).getCode());
        System.out.println();
        view.printReport(report, location.getDisplayName());

        if (report.battle().outcome() == Outcome.VICTORY && "dragon".equals(report.battle().enemy().code())) {
            System.out.println("\nДракон повержен! Барды еще долго будут петь о подвиге героя по имени " + hero.getName() + ".");
        }
    }

    private static void manageEquipment(Hero hero, GameFacade game) {
        List<Item> inventory = hero.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Инвентарь пуст");
            return;
        }
        System.out.println("Что экипировать? (0 - назад)");
        printItems(inventory);
        int index = readChoice(inventory.size());
        if (index < 0) return;

        Item item = game.equip(hero.getName(), index);
        System.out.println("Экипировано: " + view.describe(item));
    }

    private static void visitMerchant(Hero hero, GameFacade game) {
        System.out.println("Торговец: \"Чего желаешь?\" (золото: " + hero.getGold() + ")");
        System.out.println("1 - купить, 2 - продать, 0 - назад");
        switch (readLine()) {
            case "1" -> buy(hero, game);
            case "2" -> sell(hero, game);
        }
    }

    private static void buy(Hero hero, GameFacade game) {
        List<Item> showcase = game.getShowcase();
        for (int i = 0; i < showcase.size(); i++) {
            Item item = showcase.get(i);
            System.out.println((i + 1) + " - " + view.describe(item) + ": " + item.getPrice() + " золота");
        }
        int index = readChoice(showcase.size());
        if (index < 0) return;

        try {
            Item item = game.buy(hero.getName(), showcase.get(index).getCode());
            System.out.println("Куплено: " + view.describe(item));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sell(Hero hero, GameFacade game) {
        List<Item> inventory = hero.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Продавать нечего (экипированные предметы продать нельзя)");
            return;
        }
        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            System.out.println((i + 1) + " - " + view.describe(item) + ": " + game.getSellPrice(item) + " золота");
        }
        int index = readChoice(inventory.size());
        if (index < 0) return;

        Item item = inventory.get(index);
        int price = game.sell(hero.getName(), index);
        System.out.println("Продано: " + item.getName() + " за " + price + " золота");
    }

    private static void printItems(List<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + " - " + view.describe(items.get(i)));
        }
    }

    private static String readLine() {
        if (!scanner.hasNextLine()) System.exit(0);
        return scanner.nextLine().trim();
    }

    private static int readChoice(int count) {
        try {
            int choice = Integer.parseInt(readLine());
            if (choice >= 1 && choice <= count) return choice - 1;
        } catch (NumberFormatException ignored) {}
        return -1;
    }
}
