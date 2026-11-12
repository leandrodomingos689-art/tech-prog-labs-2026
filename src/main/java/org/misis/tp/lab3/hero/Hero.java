package org.misis.tp.lab3.hero;

import org.misis.tp.lab3.combat.Combatant;
import org.misis.tp.lab3.combat.Hit;
import org.misis.tp.lab3.item.Armor;
import org.misis.tp.lab3.item.Weapon;

public class Hero implements Combatant {
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

    private Hero(Builder builder) {
        this.name = builder.name;
        this.heroClass = builder.heroClass;
        this.level = builder.level;
        this.experience = builder.experience;
        this.gold = builder.gold;
        this.weapon = builder.weapon;
        this.armor = builder.armor;
        this.health = getMaxHealth();
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
    public int receiveHit(int damage) {
        if (armor != null) {
            damage = (int) Math.round(damage * (1 - armor.getDefense()));
        }
        int dealt = Math.max(1, damage);
        this.health = Math.max(0, this.health - dealt);
        return dealt;
    }

    @Override public boolean isAlive() { return health > 0; }
    public void restoreHealth() { this.health = getMaxHealth(); }
    public void addGold(int amount) { this.gold = Math.max(0, this.gold + amount); }

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

    public static class Builder {
        private String name;
        private HeroClass heroClass;
        private int level = 1;
        private int experience = 0;
        private int gold = 0;
        private Weapon weapon;
        private Armor armor;

        public Builder name(String name) { this.name = name; return this; }
        public Builder heroClass(HeroClass heroClass) { this.heroClass = heroClass; return this; }
        public Builder level(int level) { this.level = level; return this; }
        public Builder experience(int experience) { this.experience = experience; return this; }
        public Builder gold(int gold) { this.gold = gold; return this; }
        public Builder weapon(Weapon weapon) { this.weapon = weapon; return this; }
        public Builder armor(Armor armor) { this.armor = armor; return this; }

        public Hero build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("У героя должно быть имя");
            }
            if (heroClass == null) {
                throw new IllegalStateException("Не выбран класс героя");
            }
            if (level < 1) {
                throw new IllegalStateException("Уровень должен быть >= 1");
            }
            return new Hero(this);
        }
    }
}
