package org.misis.tp.lab3.location;

import org.misis.tp.lab3.combat.Battle;
import org.misis.tp.lab3.combat.BattleReport;
import org.misis.tp.lab3.combat.Outcome;
import org.misis.tp.lab3.core.Dice;
import org.misis.tp.lab3.enemy.ArmoredEnemy;
import org.misis.tp.lab3.enemy.Enemy;
import org.misis.tp.lab3.enemy.LegendaryEnemy;
import org.misis.tp.lab3.enemy.WindfuryEnemy;
import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.item.Item;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.tactic.BalancedTactic;
import org.misis.tp.lab3.tactic.Tactic;

import java.util.List;

public abstract class Location {
    private static final double LOOT_CHANCE = 0.6;
    private static final double ENCHANT_CHANCE = 0.3;

    private final String code;
    private final String displayName;
    private final int recommendedLevel;
    protected final ItemCatalog catalog;

    protected Location(String code, String displayName, int recommendedLevel, ItemCatalog catalog) {
        this.code = code;
        this.displayName = displayName;
        this.recommendedLevel = recommendedLevel;
        this.catalog = catalog;
    }

    protected abstract Enemy createEnemy();
    protected abstract List<String> getLootTable();

    public ExpeditionReport explore(Hero hero, Integer seed) {
        return explore(hero, seed, new BalancedTactic());
    }

    public ExpeditionReport explore(Hero hero, Integer seed, Tactic tactic) {
        Dice dice = Dice.getInstance();
        int usedSeed = (seed != null) ? seed : dice.nextSeed();
        dice.reseed(usedSeed);

        hero.rest();
        Enemy enemy = createEnemy();
        if (dice.chance(0.2)) {
            enemy = makeElite(enemy, dice);
        }
        BattleReport battle = new Battle(hero, enemy, tactic).run();

        Rewards rewards = (battle.outcome() == Outcome.VICTORY) ? reward(hero, enemy, dice) : punish(hero);
        return new ExpeditionReport(usedSeed, code, tactic.getCode(), battle, rewards);
    }

    private Enemy makeElite(Enemy enemy, Dice dice) {
        boolean both = dice.chance(0.25);
        if (dice.chance(0.5)) {
            enemy = new LegendaryEnemy(enemy);
            if (both) enemy = new WindfuryEnemy(enemy);
        } else {
            enemy = new WindfuryEnemy(enemy);
            if (both) enemy = new LegendaryEnemy(enemy);
        }
        return dice.chance(0.3) ? new ArmoredEnemy(enemy) : enemy;
    }

    private Rewards reward(Hero hero, Enemy enemy, Dice dice) {
        int exp = enemy.getExperienceReward();
        int gold = enemy.rollGoldReward();
        Item loot = null;

        if (dice.chance(LOOT_CHANCE) && !getLootTable().isEmpty()) {
            String lootCode = dice.pick(getLootTable());
            loot = catalog.createItem(lootCode);
            if (dice.chance(ENCHANT_CHANCE) && !loot.getPossibleEnchantments().isEmpty()) {
                loot.enchant(dice.pick(loot.getPossibleEnchantments()));
            }
        }
        hero.addGold(gold);
        if (loot != null) {
            hero.addItem(loot);
        }
        int levelsGained = hero.addExperience(exp);
        return new Rewards(exp, gold, loot, levelsGained);
    }

    private Rewards punish(Hero hero) {
        int lostGold = hero.getGold() / 2;
        hero.spendGold(lostGold);
        return new Rewards(0, -lostGold, null, 0);
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
    public int getRecommendedLevel() { return recommendedLevel; }
}
