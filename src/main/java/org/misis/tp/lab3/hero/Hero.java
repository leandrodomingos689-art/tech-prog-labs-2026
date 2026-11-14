package org.misis.tp.lab3.hero;

import org.misis.tp.lab3.combat.Combatant;
import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.defense.Damage;
import org.misis.tp.lab3.defense.DamageHandler;
import org.misis.tp.lab3.item.Armor;
import org.misis.tp.lab3.item.Item;
import org.misis.tp.lab3.item.Weapon;
import org.misis.tp.lab3.kit.StarterKitFactory;
import org.misis.tp.lab3.progress.Achievement;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Hero implements Combatant {
    private static final int MAX_NAME_LENGTH = 20;
    private static final int STARTING_GOLD = 50;
    private static final int HEALTH_PER_LEVEL = 15;
    private static final int DAMAGE_PER_LEVEL = 3;

    private final String name;
    private final HeroClass heroClass;
    private int level;
    private int experience;
    private int gold;
    private Weapon weapon;
    private Armor armor;
    private int health;
    private DamageHandler defense;
    private final List<Item> inventory = new ArrayList<>();
    private final Set<Achievement> achievements = new LinkedHashSet<>();

    private Hero(Builder builder) {
        this.name = builder.name;
        this.heroClass = builder.heroClass;
        this.level = builder.level;
        this.experience = builder.experience;
        this.gold = builder.gold;
        this.weapon = builder.weapon;
        this.armor = builder.armor;
        this.inventory.addAll(builder.inventory);
        this.achievements.addAll(builder.achievements);
        rest();
    }

    @Override
    public Hit rollDamage() {
        int levelBonus = (level - 1) * DAMAGE_PER_LEVEL;
        if (weapon == null) {
            return new Hit(1 + levelBonus, false);
        }
        Hit baseHit = weapon.rollDamage();
        return new Hit(baseHit.damage() + levelBonus, baseHit.critical());
    }

    @Override
    public Damage receiveHit(int amount) {
        Damage damage = new Damage(amount);
        defense.handle(damage);
        this.health = Math.max(0, this.health - damage.getAmount());
        return damage;
    }

    @Override public boolean isAlive() { return health > 0; }

    public void rest() {
        this.health = getMaxHealth();
        this.defense = heroClass.createDefense(this.armor, this.level);
    }

    public void addGold(int amount) {
        this.gold = Math.max(0, this.gold + amount);
    }

    public void spendGold(int amount) {
        if (amount > this.gold) {
            throw new IllegalStateException("Не хватает золота: нужно " + amount + ", есть " + this.gold);
        }
        this.gold -= amount;
    }

    public void addItem(Item item) { inventory.add(item); }
    public void removeItem(Item item) { inventory.remove(item); }

    public void equip(Item item) {
        if (!inventory.contains(item)) {
            throw new IllegalArgumentException("Предмет не найден в инвентаре");
        }
        inventory.remove(item);
        if (item instanceof Weapon newWeapon) {
            if (this.weapon != null) inventory.add(this.weapon);
            this.weapon = newWeapon;
        } else if (item instanceof Armor newArmor) {
            if (this.armor != null) inventory.add(this.armor);
            this.armor = newArmor;
            this.defense = heroClass.createDefense(this.armor, this.level);
        }
    }

    public void unlock(Achievement achievement) {
        achievements.add(achievement);
    }

    public int addExperience(int amount) {
        this.experience += amount;
        int levelsGained = 0;
        while (this.experience >= getExperienceToNextLevel()) {
            this.experience -= getExperienceToNextLevel();
            this.level++;
            levelsGained++;
        }
        return levelsGained;
    }

    public int getExperienceToNextLevel() { return level * 100; }
    @Override public int getMaxHealth() { return heroClass.getBaseHealth() + (level - 1) * HEALTH_PER_LEVEL; }
    @Override public int getHealth() { return health; }
    @Override public String getCode() { return heroClass.getCode(); }
    @Override public String getName() { return name; }
    public HeroClass getHeroClass() { return heroClass; }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getGold() { return gold; }
    public Weapon getWeapon() { return weapon; }
    public Armor getArmor() { return armor; }
    public List<Item> getInventory() { return List.copyOf(inventory); }
    public List<Achievement> getAchievements() { return List.copyOf(achievements); }

    public static class Builder {
        private String name;
        private HeroClass heroClass;
        private int level = 1;
        private int experience = 0;
        private int gold = STARTING_GOLD;
        private Weapon weapon;
        private Armor armor;
        private List<Item> inventory = List.of();
        private List<Achievement> achievements = List.of();

        public Builder name(String name) { this.name = name; return this; }
        public Builder heroClass(HeroClass heroClass) { this.heroClass = heroClass; return this; }
        public Builder level(int level) { this.level = level; return this; }
        public Builder experience(int experience) { this.experience = experience; return this; }
        public Builder gold(int gold) { this.gold = gold; return this; }
        public Builder weapon(Weapon weapon) { this.weapon = weapon; return this; }
        public Builder armor(Armor armor) { this.armor = armor; return this; }
        public Builder inventory(List<Item> inventory) { this.inventory = List.copyOf(inventory); return this; }
        public Builder achievements(List<Achievement> achievements) { this.achievements = List.copyOf(achievements); return this; }

        public Builder starterKit(StarterKitFactory kit) {
            this.weapon = kit.createWeapon();
            this.armor = kit.createArmor();
            return this;
        }

        public Hero build() {
            if (name == null || name.isBlank()) throw new IllegalStateException("У героя должно быть имя");
            if (name.length() > MAX_NAME_LENGTH) throw new IllegalStateException("Имя героя слишком длинное");
            if (heroClass == null) throw new IllegalStateException("Не выбран класс героя");
            if (weapon == null || armor == null) throw new IllegalStateException("Герою нужны оружие и броня");
            if (level < 1) throw new IllegalStateException("Уровень должен быть >= 1");
            if (gold < 0) throw new IllegalStateException("Золото не может быть отрицательным");
            if (experience < 0) throw new IllegalStateException("Опыт не может быть отрицательным");
            return new Hero(this);
        }
    }
}
