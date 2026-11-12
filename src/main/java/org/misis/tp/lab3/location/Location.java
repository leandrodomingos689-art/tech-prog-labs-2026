package org.misis.tp.lab3.location;

import org.misis.tp.lab3.combat.Battle;
import org.misis.tp.lab3.combat.BattleReport;
import org.misis.tp.lab3.combat.Outcome;
import org.misis.tp.lab3.core.Dice;
import org.misis.tp.lab3.enemy.Enemy;
import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.item.Item;
import org.misis.tp.lab3.item.ItemCatalog;
import java.util.List;

public abstract class Location {
    private static final double LOOT_CHANCE = 0.6;
    private static final double ENCHANT_CHANCE = 0.3;

    private final String code;
    private final String displayName;
    private final int recommendedLevel;
    private final ItemCatalog catalog;

    protected Location(String code, String displayName, int recommendedLevel, ItemCatalog catalog) {
        this.code = code;
        this.displayName = displayName;
        this.recommendedLevel = recommendedLevel;
        this.catalog = catalog;
    }

    protected abstract Enemy createEnemy();
    protected abstract List<String> getLootTable();

    public ExpeditionReport explore(Hero hero, Integer seed) {
        Dice dice = Dice.getInstance();
        int usedSeed = (seed != null) ? seed : dice.nextSeed();
        dice.reseed(usedSeed);

        hero.restoreHealth();
        Enemy enemy = createEnemy();
        BattleReport battle = new Battle(hero, enemy).run();

        Rewards rewards = (battle.outcome() == Outcome.VICTORY) ? reward(hero, enemy, dice) : punish(hero);
        return new ExpeditionReport(usedSeed, code, battle, rewards);
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
        int levelsGained = hero.addExperience(exp);
        return new Rewards(exp, gold, loot, levelsGained);
    }

    private Rewards punish(Hero hero) {
        int lostGold = hero.getGold() / 2;
        hero.addGold(-lostGold);
        return new Rewards(0, -lostGold, null, 0);
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
    public int getRecommendedLevel() { return recommendedLevel; }
}
