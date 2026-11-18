package org.misis.tp.lab3.storage;

import org.junit.jupiter.api.Test;
import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.hero.HeroClass;
import org.misis.tp.lab3.item.DefaultCatalog;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CachingHeroRepositoryTest {

    @Test
    void loadsLazily() {
        HeroRepository storage = mock(HeroRepository.class);
        new CachingHeroRepository(storage);
        verifyNoInteractions(storage);
    }

    @Test
    void readsStorageOnlyOnce() {
        HeroRepository storage = mock(HeroRepository.class);
        Hero hero = warrior("Артур");
        when(storage.findAll()).thenReturn(List.of(hero));
        CachingHeroRepository cache = new CachingHeroRepository(storage);

        Hero first = cache.findByName("Артур").orElse(null);
        Hero second = cache.findByName("Артур").orElse(null);

        assertSame(hero, first);
        assertSame(first, second);
        verify(storage, times(1)).findAll();
    }

    @Test
    void cachesEmptyStorage() {
        HeroRepository storage = mock(HeroRepository.class);
        when(storage.findAll()).thenReturn(List.of());
        CachingHeroRepository cache = new CachingHeroRepository(storage);

        assertTrue(cache.findByName("Никто").isEmpty());
        assertTrue(cache.findByName("Никто").isEmpty());
        verify(storage, times(1)).findAll();
    }

    @Test
    void savesToStorageAndUpdatesCache() {
        HeroRepository storage = mock(HeroRepository.class);
        Hero oldHero = warrior("Артур");
        when(storage.findAll()).thenReturn(List.of(oldHero));
        CachingHeroRepository cache = new CachingHeroRepository(storage);

        assertSame(oldHero, cache.findByName("Артур").orElse(null));

        Hero updated = warrior("Артур");
        updated.addGold(100);

        cache.save(updated);

        verify(storage, times(1)).save(same(updated));
        assertSame(updated, cache.findByName("Артур").orElse(null));
        verify(storage, times(1)).findAll();
    }

    private Hero warrior(String name) {
        return new Hero.Builder()
                .name(name)
                .heroClass(HeroClass.WARRIOR)
                .starterKit(HeroClass.WARRIOR.starterKit(DefaultCatalog.create()))
                .build();
    }
}
