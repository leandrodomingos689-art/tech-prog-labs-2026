package org.misis.tp.lab3.storage;

import org.misis.tp.lab3.hero.Hero;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CachingHeroRepository implements HeroRepository {
    private final HeroRepository target;
    private Map<String, Hero> cache;

    public CachingHeroRepository(HeroRepository target) { this.target = target; }

    private Map<String, Hero> cached() {
        if (cache == null) {
            cache = new LinkedHashMap<>();
            for (Hero hero : target.findAll()) cache.put(hero.getName(), hero);
        }
        return cache;
    }

    @Override public Optional<Hero> findByName(String name) { return Optional.ofNullable(cached().get(name)); }
    @Override public List<Hero> findAll() { return List.copyOf(cached().values()); }

    @Override
    public void save(Hero hero) {
        target.save(hero);
        cached().put(hero.getName(), hero);
    }
}
