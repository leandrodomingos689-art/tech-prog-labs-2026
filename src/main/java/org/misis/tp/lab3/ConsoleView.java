package org.misis.tp.lab3;

import org.misis.tp.lab3.combat.*;
import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.item.Enchantment;
import org.misis.tp.lab3.item.Item;
import org.misis.tp.lab3.location.ExpeditionReport;
import org.misis.tp.lab3.location.Rewards;
import org.misis.tp.lab3.progress.Achievement;

import java.util.stream.Collectors;

public class ConsoleView {
    public String describe(Item item) {
        String description = item.getName();
        if (!item.getEnchantments().isEmpty()) {
            String names = item.getEnchantments().stream()
                    .map(Enchantment::name)
                    .collect(Collectors.joining(", "));
            description += " [" + names + "]";
        }
        return description + " (" + item.getDescription() + ")";
    }

    public void printHero(Hero hero) {
        System.out.println(hero.getName() + ", " + hero.getHeroClass().getDisplayName() + " " + hero.getLevel() + " ур. | здоровье " +
                hero.getMaxHealth() + " | опыт " + hero.getExperience() + "/" + hero.getExperienceToNextLevel() + " | золото " + hero.getGold());
        System.out.println("  Оружие: " + describe(hero.getWeapon()));
        System.out.println("  Броня:  " + describe(hero.getArmor()));
        if (!hero.getAchievements().isEmpty()) {
            String ach = hero.getAchievements().stream().map(Achievement::getDisplayName).collect(Collectors.joining(", "));
            System.out.println("  Достижения: " + ach);
        }
    }

    public String summarize(ExpeditionReport report, String locationName, String tacticName) {
        String result = report.battle().outcome() == Outcome.VICTORY ? "победа" : "поражение";
        return locationName + ", " + report.battle().enemy().name() + ", тактика \"" + tacticName + "\": " + result;
    }

    public void printReport(ExpeditionReport report, String locationName) {
        BattleReport battle = report.battle();
        System.out.println("=== " + locationName + " (сид " + report.seed() + ") ===");
        System.out.println(battle.hero().name() + " встречает врага: " + battle.enemy().name() + " (" + battle.enemy().maxHealth() + " здоровья)");
        for (BattleEvent event : battle.events()) {
            printEvent(event, battle);
        }
        printRewards(battle, report.rewards());
    }

    private void printEvent(BattleEvent event, BattleReport battle) {
        Participant source = event.source() == Side.HERO ? battle.hero() : battle.enemy();
        Participant target = event.target() == Side.HERO ? battle.hero() : battle.enemy();

        switch (event.type()) {
            case HIT -> System.out.println("[раунд " + event.round() + "] " + source.name() + " -> " + target.name() + ": " +
                    event.amount() + " урона, осталось " + event.targetHealthAfter() + "/" + target.maxHealth());
            case CRITICAL_HIT -> System.out.println("[раунд " + event.round() + "] " + source.name() + " -> " + target.name() + ": КРИТ! " +
                    event.amount() + " урона, осталось " + event.targetHealthAfter() + "/" + target.maxHealth());
            case EVADE -> System.out.println("[раунд " + event.round() + "] " + source.name() + " -> " + target.name() + ": промах, " + target.name() + " уклоняется");
            case ABSORB -> System.out.println("[раунд " + event.round() + "] " + source.name() + " -> " + target.name() + ": барьер поглощает " + event.amount() + " урона");
            case POWER_STRIKE -> System.out.println("[раунд " + event.round() + "] " + source.name() + " бьет в полную силу");
            case DEFEND -> System.out.println("[раунд " + event.round() + "] " + source.name() + " уходит в оборону");
            case DEATH -> System.out.println(target.name() + " повержен!");
        }
    }

    private void printRewards(BattleReport battle, Rewards rewards) {
        if (battle.outcome() == Outcome.DEFEAT) {
            System.out.println("Поражение... " + battle.hero().name() + " едва уносит ноги и теряет " + (-rewards.gold()) + " золота");
            return;
        }
        System.out.println("Победа! +" + rewards.experience() + " опыта, +" + rewards.gold() + " золота");
        if (rewards.loot() != null) System.out.println("Добыча: " + describe(rewards.loot()));
        if (rewards.levelsGained() > 0) System.out.println("Новый уровень!");
    }
}
