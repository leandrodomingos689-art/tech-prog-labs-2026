package org.misis.tp.lab3.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.hero.HeroClass;
import org.misis.tp.lab3.item.DefaultCatalog;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.progress.Achievement;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileHeroRepositoryTest {

    @TempDir
    Path directory;

    @Test
    void restoresHeroThroughNewRepository() {
        ItemCatalog catalog = DefaultCatalog.create();
        Path file = directory.resolve("heroes.json");
        FileHeroRepository writer = new FileHeroRepository(file, catalog);

        Hero original = new Hero.Builder()
                .name("Артур")
                .heroClass(HeroClass.WARRIOR)
                .starterKit(HeroClass.WARRIOR.starterKit(catalog))
                .level(2)
                .experience(35)
                .gold(175)
                .achievements(List.of(Achievement.FIRST_BLOOD))
                .build();

        writer.save(original);
        FileHeroRepository reader = new FileHeroRepository(file, DefaultCatalog.create());
        Hero restored = reader.findByName("Артур").orElseThrow();

        assertNotSame(original, restored);
        assertEquals("Артур", restored.getName());
        assertEquals(HeroClass.WARRIOR, restored.getHeroClass());
        assertEquals(2, restored.getLevel());
        assertEquals(35, restored.getExperience());
        assertEquals(175, restored.getGold());
        assertEquals(original.getWeapon().getCode(), restored.getWeapon().getCode());
        assertEquals(original.getArmor().getCode(), restored.getArmor().getCode());
        assertEquals(List.of(Achievement.FIRST_BLOOD), restored.getAchievements());
    }

    @Test
    void missingFileMeansEmptyStorage() {
        FileHeroRepository repository = new FileHeroRepository(
                directory.resolve("missing.json"),
                DefaultCatalog.create()
        );
        assertTrue(repository.findAll().isEmpty());
        assertTrue(repository.findByName("Артур").isEmpty());
    }
}
