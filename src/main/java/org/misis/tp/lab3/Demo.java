package org.misis.tp.lab3;

import org.misis.tp.lab3.combat.Outcome;
import org.misis.tp.lab3.core.Dice;
import org.misis.tp.lab3.defense.*;
import org.misis.tp.lab3.enemy.*;
import org.misis.tp.lab3.event.GameEvent;
import org.misis.tp.lab3.event.GameEventPublisher;
import org.misis.tp.lab3.event.GameEventType;
import org.misis.tp.lab3.game.GameFacade;
import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.hero.HeroClass;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.item.Weapon;
import org.misis.tp.lab3.location.*;
import org.misis.tp.lab3.progress.Achievement;
import org.misis.tp.lab3.progress.AchievementTracker;
import org.misis.tp.lab3.progress.ExpeditionJournal;
import org.misis.tp.lab3.storage.CachingHeroRepository;
import org.misis.tp.lab3.storage.FileHeroRepository;
import org.misis.tp.lab3.tactic.AggressiveTactic;
import org.misis.tp.lab3.tactic.BalancedTactic;
import org.misis.tp.lab3.tactic.CautiousTactic;
import org.misis.tp.lab3.tactic.Tactic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Demo {
    private static final ConsoleView view = new ConsoleView();
    private static final ItemCatalog catalog = DefaultCatalog.create();
    private static final Dice dice = Dice.getInstance();

    private static Hero newHero(String name, HeroClass heroClass, int level) {
        return new Hero.Builder()
                .name(name)
                .heroClass(heroClass)
                .starterKit(heroClass.starterKit(catalog))
                .level(level)
                .build();
    }

    private static void printHits(String title, DamageHandler chain, int count) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Damage damage = new Damage(30);
            chain.handle(damage);
            results.add(damage.isEvaded() ? "уклонение"
                    : damage.getAbsorbed() > 0 ? "поглощено " + damage.getAbsorbed() + ", прошло " + damage.getAmount()
                    : "прошло " + damage.getAmount());
        }
        System.out.println(title + ": " + String.join("; ", results));
    }

    public static void main(String[] args) throws IOException {
        System.out.println("===== Цепочка обязанностей =====");
        printHits("Броня 30%", new ArmorHandler(0.3), 2);
        DamageHandler armor = new ArmorHandler(0.3);
        armor.setNext(new BarrierHandler(30));
        printHits("Броня 30% -> барьер 30", armor, 3);
        dice.reseed(3);
        DamageHandler chain = new EvasionHandler(0.5);
        chain.setNext(new ArmorHandler(0.3)).setNext(new BarrierHandler(30));
        printHits("Уклонение 50% -> броня 30% -> барьер 30", chain, 3);

        System.out.println("\n===== Стратегия =====");
        for (Tactic tactic : List.of(new BalancedTactic(), new AggressiveTactic(), new CautiousTactic())) {
            Hero mage = newHero("Мерлин", HeroClass.MAGE, 2);
            ExpeditionReport report = new Crypt(catalog).explore(mage, 9, tactic);
            String result = report.battle().outcome() == Outcome.VICTORY ? "победа" : "поражение";
            int lastRound = report.battle().events().get(report.battle().events().size() - 1).round();
            System.out.println(tactic.getDisplayName() + ": " + result + ", раундов " + lastRound +
                    ", у героя осталось " + mage.getHealth() + "/" + mage.getMaxHealth() + " здоровья");
        }

        System.out.println("\n===== Декоратор =====");
        List<Enemy> enemies = List.of(
                new Goblin(),
                new LegendaryEnemy(new Goblin()),
                new WindfuryEnemy(new Goblin()),
                new LegendaryEnemy(new WindfuryEnemy(new Goblin())),
                new ArmoredEnemy(new Goblin()),
                new ArmoredEnemy(new LegendaryEnemy(new WindfuryEnemy(new Dragon())))
        );
        for (Enemy enemy : enemies) {
            dice.reseed(5);
            String hits = List.of(1, 2, 3).stream()
                    .map(i -> String.valueOf(enemy.rollDamage().damage()))
                    .collect(Collectors.joining(", "));
            Damage damage = enemy.receiveHit(20);
            System.out.println(enemy.getName() + ": удары " + hits + ", награда " + enemy.getExperienceReward() +
                    " опыта; входящий урон 20 -> " + damage.getAmount() + ", здоровье " + enemy.getHealth() + "/" + enemy.getMaxHealth());
        }

        System.out.println("\n===== Адаптер =====");
        Weapon bow = (Weapon) catalog.createItem(DefaultCatalog.ELVEN_BOW);
        HauntedWeapon haunted = new HauntedWeapon(bow);
        System.out.println("Предмет: " + view.describe(bow));
        System.out.println("Враг:    " + haunted.getName() + " (" + haunted.getMaxHealth() + " здоровья, награда " + haunted.getExperienceReward() + " опыта)");
        HauntedManor manor = new HauntedManor(catalog);
        view.printReport(manor.explore(newHero("Леголас", HeroClass.RANGER, 2), 55, new CautiousTactic()), manor.getDisplayName());

        System.out.println("\n===== Наблюдатель =====");
        GameEventPublisher events = new GameEventPublisher();
        ExpeditionJournal journal = new ExpeditionJournal();
        events.subscribe(GameEventType.VICTORY, new AchievementTracker());
        events.subscribe(GameEventType.VICTORY, journal);
        events.subscribe(GameEventType.DEFEAT, journal);
        events.subscribe(GameEventType.VICTORY, event -> System.out.println("Событие " + event.type() + ": герой " + event.hero().getName()));
        Hero warrior = newHero("Артур", HeroClass.WARRIOR, 1);
        ExpeditionReport report = new Forest(catalog).explore(warrior, 7);
        GameEventType type = report.battle().outcome() == Outcome.VICTORY ? GameEventType.VICTORY : GameEventType.DEFEAT;
        events.publish(new GameEvent(type, warrior, report));
        System.out.println("Достижения героя: " + warrior.getAchievements().stream().map(Achievement::getDisplayName).collect(Collectors.joining(", ")));
        System.out.println("Записей в журнале: " + journal.getEntries(warrior.getName()).size());

        Path directory = Files.createTempDirectory("lab-demo-");
        try {
            System.out.println("\n===== Заместитель =====");
            Path path = directory.resolve("heroes.json");
            FileHeroRepository file = new FileHeroRepository(path, catalog);
            file.save(warrior);
            System.out.println("Содержимое файла:");
            System.out.print(Files.readString(path));
            System.out.println("Без заместителя два поиска дают один и тот же объект: " +
                    (file.findByName("Артур").orElseThrow() == file.findByName("Артур").orElseThrow()));
            CachingHeroRepository cached = new CachingHeroRepository(file);
            Hero hero = cached.findByName("Артур").orElseThrow();
            System.out.println("С заместителем два поиска дают один и тот же объект: " +
                    (hero == cached.findByName("Артур").orElseThrow()));
            hero.addGold(100);
            cached.save(hero);
            System.out.println("После сохранения через заместитель золото героя в файле: " +
                    file.findByName("Артур").orElseThrow().getGold());

            System.out.println("\n===== Фасад =====");
            GameFacade game = new GameFacade(directory.resolve("game.json"));
            game.createHero("Гэндальф", "mage");
            dice.reseed(11);
            ExpeditionReport expedition = game.explore("Гэндальф", "forest", "cautious");
            System.out.println("Экспедиция через фасад: " +
                    view.summarize(expedition, game.getLocation(expedition.locationCode()).getDisplayName(),
                            game.getTactic(expedition.tacticCode()).getDisplayName()));
            view.printHero(game.getHero("Гэндальф"));
        } finally {
            try (var stream = Files.walk(directory)) {
                stream.sorted(Comparator.reverseOrder()).forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException ignored) {}
                });
            }
        }
    }
}
