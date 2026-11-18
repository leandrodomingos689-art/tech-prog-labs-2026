package org.misis.tp.lab3.hero;

import org.junit.jupiter.api.Test;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.kit.StarterKitFactory;
import org.misis.tp.lab3.progress.Achievement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeroBuilderTest {

    @Test
    void buildsHeroWithCustomProgressAndKit() {
        ItemCatalog catalog = DefaultCatalog.create();
        StarterKitFactory starterKit = HeroClass.WARRIOR.starterKit(catalog);

        Hero hero = new Hero.Builder()
                .name("Ланселот")
                .heroClass(HeroClass.WARRIOR)
                .starterKit(starterKit)
                .level(5)
                .experience(120)
                .gold(500)
                .achievements(List.of(Achievement.FIRST_BLOOD))
                .build();

        assertEquals("Ланселот", hero.getName());
        assertEquals(HeroClass.WARRIOR, hero.getHeroClass());
        assertEquals(5, hero.getLevel());
        assertEquals(120, hero.getExperience());
        assertEquals(500, hero.getGold());
        assertEquals(starterKit.createWeapon().getCode(), hero.getWeapon().getCode());
        assertEquals(starterKit.createArmor().getCode(), hero.getArmor().getCode());
        assertEquals(List.of(Achievement.FIRST_BLOOD), hero.getAchievements());
    }
}
