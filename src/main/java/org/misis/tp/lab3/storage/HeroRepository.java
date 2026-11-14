package org.misis.tp.lab3.storage;

import org.misis.tp.lab3.hero.Hero;
import java.util.List;
import java.util.Optional;

public interface HeroRepository {
    Optional<Hero> findByName(String name);
    List<Hero> findAll();
    void save(Hero hero);
}
