package org.misis.tp.lab3.game;

import org.misis.tp.lab3.camp.Merchant;
import org.misis.tp.lab3.combat.Outcome;
import org.misis.tp.lab3.event.GameEvent;
import org.misis.tp.lab3.event.GameEventPublisher;
import org.misis.tp.lab3.event.GameEventType;
import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.hero.HeroClass;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.Item;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.kit.StarterKitFactory;
import org.misis.tp.lab3.location.*;
import org.misis.tp.lab3.progress.AchievementTracker;
import org.misis.tp.lab3.progress.ExpeditionJournal;
import org.misis.tp.lab3.storage.CachingHeroRepository;
import org.misis.tp.lab3.storage.FileHeroRepository;
import org.misis.tp.lab3.tactic.AggressiveTactic;
import org.misis.tp.lab3.tactic.BalancedTactic;
import org.misis.tp.lab3.tactic.CautiousTactic;
import org.misis.tp.lab3.tactic.Tactic;

import java.nio.file.Path;
import java.util.List;

public class GameFacade {
    private final ItemCatalog catalog = DefaultCatalog.create();
    private final LocationRegistry registry = new LocationRegistry(List.of(
            new Forest(catalog),
            new HauntedManor(catalog),
            new Crypt(catalog),
            new DragonLair(catalog)
    ));
    private final List<Tactic> tactics = List.of(
            new BalancedTactic(),
            new AggressiveTactic(),
            new CautiousTactic()
    );
    private final Merchant merchant = new Merchant(catalog, List.of(
            DefaultCatalog.IRON_SWORD,
            DefaultCatalog.ELVEN_BOW,
            DefaultCatalog.EMBER_STAFF,
            DefaultCatalog.CHAINMAIL,
            DefaultCatalog.PLATE_ARMOR
    ));
    private final CachingHeroRepository repository;
    private final GameEventPublisher events = new GameEventPublisher();
    private final ExpeditionJournal journal = new ExpeditionJournal();

    public GameFacade(Path saveFile) {
        this.repository = new CachingHeroRepository(new FileHeroRepository(saveFile, catalog));
        AchievementTracker achievements = new AchievementTracker();
        events.subscribe(GameEventType.VICTORY, achievements);
        events.subscribe(GameEventType.LEVEL_UP, achievements);
        events.subscribe(GameEventType.VICTORY, journal);
        events.subscribe(GameEventType.DEFEAT, journal);
    }

    public HeroClass[] getHeroClasses() { return HeroClass.values(); }

    public List<Item> getStarterKit(String code) {
        StarterKitFactory kit = HeroClass.byCode(code).starterKit(catalog);
        return List.of(kit.createWeapon(), kit.createArmor());
    }

    public List<Location> getLocations() { return registry.all(); }
    public Location getLocation(String code) { return registry.get(code); }
    public List<Tactic> getTactics() { return tactics; }

    public Tactic getTactic(String code) {
        return tactics.stream()
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестная тактика: " + code));
    }

    public List<Item> getShowcase() { return merchant.getShowcase(); }
    public int getSellPrice(Item item) { return merchant.getSellPrice(item); }
    public List<Hero> getHeroes() { return repository.findAll(); }

    public Hero getHero(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Героя с именем " + name + " нет"));
    }

    public Hero createHero(String name, String code) {
        if (repository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Герой с именем " + name + " уже есть");
        }
        HeroClass heroClass = HeroClass.byCode(code);
        Hero hero = new Hero.Builder()
                .name(name)
                .heroClass(heroClass)
                .starterKit(heroClass.starterKit(catalog))
                .build();
        repository.save(hero);
        return hero;
    }

    public ExpeditionReport explore(String name, String locationCode, String tacticCode) {
        Hero hero = getHero(name);
        ExpeditionReport expedition = getLocation(locationCode).explore(hero, null, getTactic(tacticCode));
        GameEventType outcomeType = expedition.battle().outcome() == Outcome.VICTORY
                ? GameEventType.VICTORY
                : GameEventType.DEFEAT;
        events.publish(new GameEvent(outcomeType, hero, expedition));
        if (expedition.rewards().levelsGained() > 0) {
            events.publish(new GameEvent(GameEventType.LEVEL_UP, hero, expedition));
        }
        repository.save(hero);
        return expedition;
    }

    public Item equip(String name, int index) {
        Hero hero = getHero(name);
        Item item = hero.getInventory().get(index);
        hero.equip(item);
        repository.save(hero);
        return item;
    }

    public Item buy(String name, String code) {
        Hero hero = getHero(name);
        Item item = merchant.buy(hero, code);
        repository.save(hero);
        return item;
    }

    public int sell(String name, int index) {
        Hero hero = getHero(name);
        int price = merchant.sell(hero, hero.getInventory().get(index));
        repository.save(hero);
        return price;
    }

    public List<ExpeditionReport> getJournal(String name) {
        return journal.getEntries(name);
    }
}
