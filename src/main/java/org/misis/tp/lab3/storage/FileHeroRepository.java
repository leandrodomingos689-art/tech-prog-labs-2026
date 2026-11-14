package org.misis.tp.lab3.storage;

import org.misis.tp.lab3.hero.Hero;
import org.misis.tp.lab3.hero.HeroClass;
import org.misis.tp.lab3.item.Armor;
import org.misis.tp.lab3.item.Enchantment;
import org.misis.tp.lab3.item.Item;
import org.misis.tp.lab3.item.ItemCatalog;
import org.misis.tp.lab3.item.Weapon;
import org.misis.tp.lab3.progress.Achievement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileHeroRepository implements HeroRepository {
    private final Path file;
    private final ItemCatalog catalog;

    public FileHeroRepository(Path file, ItemCatalog catalog) {
        this.file = file;
        this.catalog = catalog;
    }

    @Override
    public Optional<Hero> findByName(String name) {
        return findAll().stream().filter(h -> h.getName().equals(name)).findFirst();
    }

    @Override
    public List<Hero> findAll() {
        if (!Files.exists(file)) return List.of();
        try {
            String text = Files.readString(file, StandardCharsets.UTF_8).trim();
            if (text.isEmpty() || text.equals("[]")) return List.of();
            return parseHeroes(text);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла сохранений", e);
        }
    }

    @Override
    public void save(Hero hero) {
        List<Hero> all = new ArrayList<>(findAll().stream()
                .filter(other -> !other.getName().equals(hero.getName()))
                .toList());
        all.add(hero);

        String json = all.stream()
                .map(this::toJson)
                .collect(Collectors.joining(",\n", "[\n", "\n]\n"));
        try {
            if (file.getParent() != null) Files.createDirectories(file.getParent());
            Files.writeString(file, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи файла сохранений", e);
        }
    }

    private String toJson(Hero hero) {
        String inv = hero.getInventory().stream().map(this::itemToJson).collect(Collectors.joining(", "));
        String ach = hero.getAchievements().stream().map(a -> "\"" + escape(a.getCode()) + "\"").collect(Collectors.joining(", "));
        return """
                  {
                    "name": "%s",
                    "heroClass": "%s",
                    "level": %d,
                    "experience": %d,
                    "gold": %d,
                    "weapon": %s,
                    "armor": %s,
                    "inventory": [%s],
                    "achievements": [%s]
                  }""".formatted(
                escape(hero.getName()), escape(hero.getHeroClass().getCode()),
                hero.getLevel(), hero.getExperience(), hero.getGold(),
                itemToJson(hero.getWeapon()), itemToJson(hero.getArmor()), inv, ach
        );
    }

    private String itemToJson(Item item) {
        String ench = item.getEnchantments().stream()
                .map(e -> "\"" + escape(e.code()) + "\"")
                .collect(Collectors.joining(", "));
        return "{\"code\": \"%s\", \"enchantments\": [%s]}".formatted(escape(item.getCode()), ench);
    }

    private List<Hero> parseHeroes(String json) {
        List<Hero> result = new ArrayList<>();
        Matcher blockMatcher = Pattern.compile("\\{\\s*\"name\"[\\s\\S]*?\"achievements\"\\s*:\\s*\\[[^\\]]*\\]\\s*\\}").matcher(json);
        while (blockMatcher.find()) {
            String block = blockMatcher.group();
            Item weapon = parseSingleItem(extractObject(block, "weapon"));
            Item armor = parseSingleItem(extractObject(block, "armor"));
            if (!(weapon instanceof Weapon w) || !(armor instanceof Armor a)) {
                throw new IllegalStateException("Некорректное снаряжение героя");
            }
            List<Item> inventory = parseItemArray(extractArray(block, "inventory"));
            List<Achievement> achievements = parseStringArray(extractArray(block, "achievements")).stream()
                    .map(Achievement::byCode).toList();

            result.add(new Hero.Builder()
                    .name(extractString(block, "name"))
                    .heroClass(HeroClass.byCode(extractString(block, "heroClass")))
                    .level(extractInt(block, "level"))
                    .experience(extractInt(block, "experience"))
                    .gold(extractInt(block, "gold"))
                    .weapon(w).armor(a)
                    .inventory(inventory).achievements(achievements)
                    .build());
        }
        return result;
    }

    private Item parseSingleItem(String obj) {
        Item item = catalog.createItem(extractString(obj, "code"));
        for (String enchCode : parseStringArray(extractArray(obj, "enchantments"))) {
            item.enchant(Enchantment.byCode(enchCode));
        }
        return item;
    }

    private List<Item> parseItemArray(String arr) {
        List<Item> list = new ArrayList<>();
        Matcher m = Pattern.compile("\\{[^}]*\\}").matcher(arr);
        while (m.find()) list.add(parseSingleItem(m.group()));
        return list;
    }

    private List<String> parseStringArray(String arr) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("\"([^\"]*)\"").matcher(arr);
        while (m.find()) list.add(m.group(1));
        return list;
    }

    private String extractString(String src, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"").matcher(src);
        return m.find() ? m.group(1) : "";
    }

    private int extractInt(String src, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+)").matcher(src);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    private String extractObject(String src, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\{[^}]*\\})").matcher(src);
        return m.find() ? m.group(1) : "{}";
    }

    private String extractArray(String src, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\[[^\\]]*\\])").matcher(src);
        return m.find() ? m.group(1) : "[]";
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
